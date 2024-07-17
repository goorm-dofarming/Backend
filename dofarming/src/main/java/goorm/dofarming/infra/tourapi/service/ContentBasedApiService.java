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
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentBasedApiService {

    private static final String REQUEST_HOST = "apis.data.go.kr";
    private static final String REQUEST_PATH = "/B551011/KorService1/locationBasedList1";

    private final WebClient webClient;

    public Mono<List<Item>> fetchMountains(double mapX, double mapY) {
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
                .queryParam("mapX", mapX)
                .queryParam("mapY", mapY)
                .queryParam("radius", 10000)
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

                        // 산 검증 로직 추가 (테스트중)
                        List<Item> filteredItems = tourItems.stream()
                                .filter(item -> "A01".equals(item.cat1()))
                                .collect(Collectors.toList());

                        return Mono.just(filteredItems);
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .doOnError(WebClientResponseException.class, e -> {
                    System.err.println("Error response code: " + e.getRawStatusCode());
                    System.err.println("Error response body: " + e.getResponseBodyAsString());
                });
    }
}
