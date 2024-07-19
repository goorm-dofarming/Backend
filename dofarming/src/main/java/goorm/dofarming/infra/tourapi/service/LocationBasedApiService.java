package goorm.dofarming.infra.tourapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.dofarming.infra.tourapi.dto.Item;
import io.netty.handler.codec.http.HttpScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationBasedApiService {

    private static final List<Integer> contentTypeList = new ArrayList<>(Arrays.asList(12, 14, 28, 39));
    private static final String REQUEST_HOST = "apis.data.go.kr";
    private static final String REQUEST_PATH = "/B551011/KorService1/locationBasedList1";
    private static final List<Integer> RANDOM_CONTENT_TYPE_LIST = new ArrayList<>(Arrays.asList(12, 14, 28, 39));

    private final WebClient webClient;

    public Mono<List<Item>> fetchLocationBasedList(double mapX, double mapY, Integer contentTypeId) {
        String uri = UriComponentsBuilder.newInstance()
                .scheme(HttpScheme.HTTP.toString())
                .host(REQUEST_HOST)
                .path(REQUEST_PATH)
                .queryParam("serviceKey", "qR6tI%2BnsyPolriwrrMXbxu3BMliH02gg9FVLp%2F09CsteVUoKGJ5a5PUydtAlvVkSXaalt9mHaOIi51LGXB1iug%3D%3D")
                .queryParam("numOfRows", 8)
                .queryParam("pageNo", 1)
                .queryParam("MobileApp", "dofarming")
                .queryParam("MobileOS", "ETC")
                .queryParam("arrange", "O")
                .queryParam("contentTypeId", contentTypeId)
                .queryParam("mapX", mapX)
                .queryParam("mapY", mapY)
                .queryParam("radius", 2000)
                .queryParam("_type", "json")
                .queryParam("listYN", "Y")
                .build()
                .toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(response);
                        JsonNode items = root.path("response").path("body").path("items").path("item");

                        List<Item> tourItems = mapper.readValue(items.toString(), new TypeReference<List<Item>>() {});

                        return Mono.just(tourItems);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .doOnError(WebClientResponseException.class, e -> {
                    System.err.println("Error response code: " + e.getRawStatusCode());
                    System.err.println("Error response body: " + e.getResponseBodyAsString());
                });
    }

    public Mono<Map<Integer, List<Item>>> fetchLocationBasedLists(double mapX, double mapY, List<Integer> contentTypeIds) {
        if (contentTypeIds.contains(0)) {
            contentTypeIds = getRandomContentTypeIds();
        }

        List<Mono<Map.Entry<Integer, List<Item>>>> calls = contentTypeIds.stream()
                .map(contentTypeId -> fetchLocationBasedList(mapX, mapY, contentTypeId)
                        .map(items -> Map.entry(contentTypeId, items)))
                .collect(Collectors.toList());

        return Flux.merge(calls)
                .collectMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    private List<Integer> getRandomContentTypeIds() {
        List<Integer> randomContentTypeIds = new ArrayList<>(RANDOM_CONTENT_TYPE_LIST);
        Collections.shuffle(randomContentTypeIds);
        return randomContentTypeIds.subList(0, 2);
    }
}
