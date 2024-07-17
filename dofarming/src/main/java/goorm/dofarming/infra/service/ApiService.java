package goorm.dofarming.infra.service;

import io.netty.handler.codec.http.HttpScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ApiService {

    private static final String REQUEST_HOST = "apis.data.go.kr/B551011/KorService1";
    private static final String REQUEST_PATH = "/locationBasedList1";

    private final WebClient webClient;


    public Mono<String> fetchLocationBasedList(double mapX, double mapY) {

        return webClient.get()
                .uri(UriComponentsBuilder.newInstance()
                        .scheme(HttpScheme.HTTP.toString())
                        .host(REQUEST_HOST)
                        .path(REQUEST_PATH)
                        .queryParam("serviceKey", "qR6tI%2BnsyPolriwrrMXbxu3BMliH02gg9FVLp%2F09CsteVUoKGJ5a5PUydtAlvVkSXaalt9mHaOIi51LGXB1iug%3D%3D")
                        .queryParam("numOfRows", 10)
                        .queryParam("pageNo", 1)
                        .queryParam("MobileApp", "TestApp")
                        .queryParam("MobileOS", "ETC")
                        .queryParam("arrange", "A")
                        .queryParam("contentTypeId", 39)
                        .queryParam("mapX", mapX)
                        .queryParam("mapY", mapY)
                        .queryParam("radius", 1000)
                        .queryParam("_type", "json")
                        .build()
                        .toString()
                )
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(WebClientResponseException.class, e -> {
                    System.err.println("Error response code: " + e.getRawStatusCode());
                    System.err.println("Error response body: " + e.getResponseBodyAsString());
                });
    }
}
