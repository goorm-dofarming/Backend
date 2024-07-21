package goorm.dofarming.infra.tourapi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.infra.tourapi.domain.DataType;
import goorm.dofarming.infra.tourapi.dto.Item;
import io.netty.handler.codec.http.HttpScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestApiService {

    private static final String REQUEST_HOST = "apis.data.go.kr";  // API 요청을 위한 호스트 주소
    private static final String REQUEST_PATH = "/B551011/KorService1/locationBasedList1";  // API 요청 경로
    List<String> ACTIVITY_CODE_LIST_CAT3 = Arrays.asList("A02020400", "A02020800", "A02030100", "A02030200", "A02030300", "A02030400", "A04010700");
    List<String> ACTIVITY_CODE_LIST_CAT1 = Arrays.asList("A03"); // 예외) 골프 : cat3 = A03020700

    private final WebClient webClient;  // WebClient 인스턴스
    private final BasicApiService basicApiService;

    // 모든 소분류에 대해서 받아옴.
    public Mono<String> DownloadRestaurantDataByCat3() {
        basicApiService.fetchByCategory(ACTIVITY_CODE_LIST_CAT3, DataType.Activity);
        return basicApiService.buildURL(1, 0, ACTIVITY_CODE_LIST_CAT3); // 확인용 return 값
    }

    // 대분류에서 받아옴
    public Mono<String> DownloadRestaurantDataByCat1() {
        basicApiService.fetchByCategory(ACTIVITY_CODE_LIST_CAT1, DataType.Activity);
        return basicApiService.buildURL(1, 0, ACTIVITY_CODE_LIST_CAT1); // 확인용 return 값
    }



//    주어진 좌표를 기준으로 모든 페이지의 데이터를 가져와 cat3가 "A05020900"인 항목들만 반환
//    현재는 pageNo = 1, numOfRows = 8 설정
    public Mono<List<Item>> fetchTest(double mapX, double mapY, Integer pageNo, Integer numOfRows) {
        // 첫 번째 페이지의 total 데이터로 페이지 계산
        return fetchPage(mapX, mapY, pageNo, numOfRows)
                .flatMap(firstPageItems -> {
                    if (firstPageItems.isEmpty()) {
                        // 첫 페이지에 데이터가 없으면 빈 리스트 반환
                        return Mono.just(firstPageItems);
                    }

                    // totalCount 가져오기 위해 첫 페이지 URL 생성
                    String uri = buildURL(mapX, mapY, pageNo, numOfRows);
                    return webClient.get()
                            .uri(uri)
                            .retrieve()
                            .bodyToMono(String.class)
                            .flatMap(response -> {
                                try {

                                    // total을 통해 전체 페이지 계산
                                    ObjectMapper mapper = new ObjectMapper();
                                    JsonNode root = mapper.readTree(response);
                                    int totalCount = root.path("response").path("body").path("totalCount").asInt();
                                    int totalPages = (totalCount / numOfRows) + 1;

                                    // 나머지 페이지의 데이터를 가져옴
                                    Flux<Item> allItems = Flux.concat(
                                            Flux.fromIterable(firstPageItems),  // 첫 페이지 데이터
                                            getAllPages(mapX, mapY, 2, totalPages, numOfRows)  // 나머지 페이지 데이터
                                    );

                                    return allItems.collectList();  // 모든 데이터를 리스트로 수집
                                } catch (Exception e) {
                                    return Mono.error(e);  // 오류 발생 시 Mono.error 반환
                                }
                            });
                })
                .flatMap(tourItems -> {
                    // cat3가 "A05020900" (일단 카페!!) 인 항목들만 필터링
                    List<Item> filteredItems = tourItems.stream()
                            .filter(item -> "A05020900".equals(item.cat3()))
                            .collect(Collectors.toList());
                    return Mono.just(filteredItems);  // 필터링된 리스트 반환
                });
    }

//    모든 페이지의 데이터를 가져오는 메서드
    private Flux<Item> getAllPages(double mapX, double mapY, int startPage, int totalPages, int numOfRows) {
        return Flux.range(startPage, totalPages - startPage + 1)
                .concatMap(pageNo -> fetchPage(mapX, mapY, pageNo, numOfRows)
                        .flatMapMany(Flux::fromIterable));  // 각 페이지의 데이터를 Flux로 변환하여 연결
    }

    private Mono<List<Item>> fetchPage(double mapX, double mapY, int pageNo, int numOfRows) {
        String uri = buildURL(mapX, mapY, pageNo, numOfRows);  // 요청 URI 생성
        return getDataByURL(uri);  // URI를 사용하여 데이터 가져옴
    }

//    URL 생성하는 메서드
    private String buildURL(double mapX, double mapY, int pageNo, int numOfRows) {
        return UriComponentsBuilder.newInstance()
                .scheme(HttpScheme.HTTP.toString())
                .host(REQUEST_HOST)
                .path(REQUEST_PATH)
                .queryParam("serviceKey", "qR6tI%2BnsyPolriwrrMXbxu3BMliH02gg9FVLp%2F09CsteVUoKGJ5a5PUydtAlvVkSXaalt9mHaOIi51LGXB1iug%3D%3D")
                .queryParam("numOfRows", numOfRows)
                .queryParam("pageNo", pageNo)
                .queryParam("MobileApp", "dofarming")
                .queryParam("MobileOS", "ETC")
                .queryParam("arrange", "O")
                .queryParam("contentTypeId", 39)
                .queryParam("mapX", mapX)
                .queryParam("mapY", mapY)
                .queryParam("radius", 1000)
                .queryParam("_type", "json")
                .queryParam("listYN", "Y")
                .build()
                .toUriString();
    }

//    URL에서 데이터를 가져오는 메서드
    private Mono<List<Item>> getDataByURL(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        // 응답이 비어 있는지 확인
                        if (response == null || response.isEmpty()) {
                            return Mono.error(new Exception("Empty response from server"));
                        }

                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(response);
                        JsonNode items = root.path("response").path("body").path("items").path("item");

                        // 응답 데이터를 Item 리스트로 변환
                        List<Item> tourItems = mapper.readValue(items.toString(), new TypeReference<List<Item>>() {});

                        return Mono.just(tourItems);  // Item 리스트 반환
                    } catch (Exception e) {
//                        return new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Server Error 입니다.");
                        return Mono.error(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "Server Error"));  // 오류 발생 시 Mono.error 반환
                    }
                })
                .doOnError(WebClientResponseException.class, e -> {
                    // 오류 응답 코드 및 본문을 로그에 기록
                    System.err.println("Error response code: " + e.getRawStatusCode());
                    System.err.println("Error response body: " + e.getResponseBodyAsString());
                })
                .doOnError(e -> {
                    // 오류 메시지를 로그에 기록
                    System.err.println("Error: " + e.getMessage());
                });
    }
}