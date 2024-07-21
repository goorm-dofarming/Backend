package goorm.dofarming.infra.tourapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.infra.tourapi.config.WebClientConfig;
import goorm.dofarming.infra.tourapi.domain.Activity;
import goorm.dofarming.infra.tourapi.domain.DataType;
import goorm.dofarming.infra.tourapi.domain.Restaurant;
import goorm.dofarming.infra.tourapi.domain.Tour;
import goorm.dofarming.infra.tourapi.repository.ActivityRepository;
import goorm.dofarming.infra.tourapi.repository.RestaurantRepository;
import goorm.dofarming.infra.tourapi.repository.TourRepository;
import io.netty.handler.codec.http.HttpScheme;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicApiService {

    private final WebClient webClient;
    private final RestaurantRepository restaurantRepository;
    private final TourRepository tourRepository;
    private final ActivityRepository activityRepository;
    private final ObjectMapper objectMapper;

    private static final String REQUEST_HOST = "apis.data.go.kr/B551011/KorService1";
    private static final String REQUEST_AREA = "/areaBasedList1";
    private static final String SERVICE_KEY = WebClientConfig.getServiceKey();
    public static List<Integer> ERROR_PAGE_LIST = new ArrayList<>();
    private static Integer MAX_PAGE;

    public String fetchByCategory(List<String> CATEGORY_LIST, DataType dataType) {
        log.info("DataType : {}", dataType);
        int cI = 0;
        for (int i = 0; i < CATEGORY_LIST.size(); i++) {
            String category = CATEGORY_LIST.get(i);

            log.info("MAX_PAGE : {}", CalculateMaxPage(CATEGORY_LIST, cI++));

            log.info("category : {}", category);
            for (int pageNo = 1; pageNo <= MAX_PAGE; pageNo++) {
                // 여기서도 Json으로 받아올 수는 있는데 그러면 xml응답일 경우 처리하기 귀찮아서 일단 String으로 받아옴.
                String responseBody = buildURI(pageNo, category).block();
                if (responseBody == null) break;
                log.info("응답 페이지 : {}", pageNo);
                divType(responseBody, pageNo, dataType);
            }

            // 에러난 페이지가 있으면 재시도
            if (!ERROR_PAGE_LIST.isEmpty()) {
                List<Integer> RETRY_ERROR_PAGE_LIST = new ArrayList<>(ERROR_PAGE_LIST);
                ERROR_PAGE_LIST.clear();
                retryErrorPages(RETRY_ERROR_PAGE_LIST, dataType, category);
            }

        }

        // 2번 요청하고도 안되면 문제가 있을 수도 있음.
        if (!ERROR_PAGE_LIST.isEmpty()) {
            log.info("에러 페이지 리스트 : {}", ERROR_PAGE_LIST);
        }

        return buildURI(1, CATEGORY_LIST.get(0)).block(); // 확인용
    }

    private void retryErrorPages(List<Integer> RETRY_ERROR_PAGE_LIST, DataType dataType, String category) {
        for (int pageNo : RETRY_ERROR_PAGE_LIST) {
            String responseBody = buildURI(pageNo, category).block();
            if (responseBody == null) break;
            log.info("응답 페이지 : {}", pageNo);
            divType(responseBody, pageNo, dataType);
        }
    }

    private Mono<String> buildURI(int pageNo, String category) {

        log.info("요청 페이지 : {}", pageNo);
        return webClient.get()
                .uri(UriComponentsBuilder.newInstance()
                        .scheme(HttpScheme.HTTP.toString())
                        .host(REQUEST_HOST)
                        .path(REQUEST_AREA)
                        .queryParam("serviceKey", SERVICE_KEY)
                        .queryParam("numOfRows", 100)
                        .queryParam("pageNo", pageNo)
                        .queryParam("MobileApp", "TestApp")
                        .queryParam("MobileOS", "ETC")
                        .queryParam("_type", "json")
                        .queryParam("listYN", "Y")
                        .queryParam("cat3", category)
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

    private void divType(String responseBody, Integer pageNo, DataType dataType) {
        try {
            if (responseBody.trim().startsWith("{")) { // Json
                parseJson(responseBody, dataType);
            } else if (responseBody.trim().startsWith("<")) { // XML
                log.info("ERROR_PAGE : {}", pageNo);
                System.out.println(responseBody);
                ERROR_PAGE_LIST.add(pageNo); // 에러나는 페이지는 다시 요청해야해서 모와놓아야함.
            } else {
                log.info("Json / XML 과는 다른 오류입니다.");
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "서버 오류입니다.");
        }
    }

    private void parseJson(String responseBody, DataType dataType) {
        try {

//            log.info("[[Json 파싱 시작]]");
            // Json으로 다시 만들어줌.
            JsonNode root = objectMapper.readTree(responseBody);
            // 데이터들을 받아온다. (numOfRows 만큼 item 들이 있음)
            JsonNode items = root.path("response").path("body").path("items").path("item");
//            log.info("[[Json 파싱 완료]]");

//            log.info("items size : {}",items.size());

            // 파싱할 데이터가 없다면 데이터 업데이트 종료
            if (items.isMissingNode()) {
                log.info("[[ MISSING ERROR ]]");
                return;
            }
            if (items.isEmpty()) {
                log.info("[[ SIZE ERROR ]]");
            }

//            log.info("[[item 저장 시작]]");
            // 이제 데이터를 하나하나 분리
            for (JsonNode item : items) {
                String title = item.path("title").asText();
                String addr = item.path("addr1").asText();
                String tel = item.path("tel").asText();
                String image = item.path("firstimage").asText();
                double mapx = item.path("mapx").asDouble();
                double mapy = item.path("mapy").asDouble();

                // 데이터 타입에 맞게 엔티티 객체 생성 및 저장 로직 호출
                saveEntity(title, image, addr, tel, mapx, mapy, dataType);
            }

        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "에러입니다");
        }
    }

    private void saveEntity(String title, String image, String addr, String tel, double mapx, double mapy, DataType dataType) {

        try {
            if (dataType == DataType.Restaurant) {
                Restaurant restaurant = new Restaurant(title, addr, tel, image, mapx, mapy);
                restaurantRepository.save(restaurant);
            } else if (dataType == DataType.Activity) {
                Activity activity = new Activity(title, addr, tel, image, mapx, mapy);
                activityRepository.save(activity);
            } else if (dataType == DataType.Tour) {
                Tour tour = new Tour(title, addr, tel, image, mapx, mapy);
                tourRepository.save(tour);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "데이터 타입이 맞지 않습니다.");
        }

    }

    public Integer CalculateMaxPage(List<String> CATEGORY_LIST, int categoryIndex) {
        String responseBody = buildURI(1, CATEGORY_LIST.get(categoryIndex)).block();
        // Json으로 다시 만들어줌.
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            // 데이터들을 받아온다. (numOfRows 만큼 item 들이 있음)
            Integer numOfRows = root.path("response").path("body").path("numOfRows").asInt();
            Integer totalCount = root.path("response").path("body").path("totalCount").asInt();
            MAX_PAGE = totalCount / numOfRows + 1;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "서버 오류 입니다.");
        }
        return MAX_PAGE;
    }
}
