package goorm.dofarming.domain.jpa.recommend.service;

import goorm.dofarming.domain.jpa.like.service.LikeService;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.location.repository.LocationRepository;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.domain.jpa.log.repository.LogRepository;
import goorm.dofarming.domain.jpa.recommend.entity.Recommend;
import goorm.dofarming.domain.jpa.recommend.entity.RecommendDTO;
import goorm.dofarming.domain.jpa.recommend.repository.RecommendRepository;
import goorm.dofarming.domain.jpa.recommend.util.RecommendConfig;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.infra.tourapi.domain.*;
import goorm.dofarming.infra.tourapi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendServiceV2 {

    private final RecommendRepository recommendRepository;
    private final LogRepository logRepository;
    private final LocationRepository locationRepository;
    private final OceanRepository oceanRepository;
    private final MountainRepository mountainRepository;
    private final ActivityRepository activityRepository;
    private final TourRepository tourRepository;
    private final RestaurantRepository restaurantRepository;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final LikeService likeService;
    private final static double firstRadius = RecommendConfig.firstRadius;
    private final static double secondRadius = RecommendConfig.secondRadius;

    // 바다 테마 선택, 바다 테마의 종류는 422가지
    public RecommendDTO recommendOcean(Long userId) {

        ArrayList<Object> recommendList = new ArrayList<>();
        List<Integer> themes = new ArrayList<>(Arrays.asList(3, 4, 5, 6));

        Ocean ocean = randomOcean();
        recommendList.add(ocean);

        // 해수욕장 위치가 핑으로 찍힘
        Double mapX = ocean.getMapX();
        Double mapY = ocean.getMapY();

        // 산 혹은 바다 테마의 경우는 해수욕장 위치가 추천 로그가 된다.
        Log log = saveLog(userId, mapX, mapY, 1); // 로그에 오류가 생겨도 로그는 그대로 남아야함.

        String address = parseAddress(ocean.getAddr());

        // 해당 핑에서 테마가 겹치고 거리 안에 있으면 받아와서 랜덤으로 2가지 뽑고 추천!!
        return recommendLocationTest(mapX, mapY, recommendList, themes, log, address);
    }

    // 산 테마 선택, 산 테마의 종류는 636가지
    public RecommendDTO recommendMountain(Long userId) {

        ArrayList<Object> recommendList = new ArrayList<>();
        List<Integer> themes = new ArrayList<>(Arrays.asList(3, 4, 5, 6));

        Mountain mountain = randomMountain();
        recommendList.add(mountain);

        // 산의 위치가 핑으로 찍힘
        Double mapX = mountain.getMapX();
        Double mapY = mountain.getMapY();

        // 산 혹은 바다 테마의 경우는 해수욕장 위치가 추천 로그가 된다.
        Log log = saveLog(userId, mapX, mapY, 2); // 로그에 오류가 생겨도 로그는 그대로 남아야함.

        String address = parseAddress(mountain.getAddr());

        // 해당 핑에서 테마가 겹치고 거리 안에 있으면 받아와서 랜덤으로 2가지 뽑고 추천!!
        return recommendLocation(mapX, mapY, recommendList, themes, log, address);
    }

    // ( 바다, 산을 제외한 ) 테마 선택
    public RecommendDTO recommendTheme(int dataType, double mapX, double mapY, Long userId, String address) {

        ArrayList<Object> recommendList = new ArrayList<>();
        Log log = saveLog(userId, mapX, mapY, dataType);

        if (getLocationsWithinRadius(dataType, mapX, mapY, firstRadius).size() < 8) {
            List<?> themeLocations = getLocationsWithinRadius(dataType, mapX, mapY, secondRadius);
            recommendList.addAll(randomSelect(themeLocations, 8));
        } else {
            List<?> themeLocations = getLocationsWithinRadius(dataType, mapX, mapY, firstRadius);
            recommendList.addAll(randomSelect(themeLocations, 8));
        }

        createRecommend(recommendList, log);
        return new RecommendDTO(log.getLogId(), address, recommendList);
    }

    // 완전 랜덤 추천 (테마 선택 X) - 유저 전용
    // 이 경우에는 바다 테마를 검색해보고 주변에 바다가 있으면 6개 중 랜덤 아니면 5개 중 랜덤 2개 선택해서 보내줌.
    public RecommendDTO recommendRandomForUser(double mapX, double mapY, Long userId, String address) {

        ArrayList<Object> recommendList = new ArrayList<>();
        List<Integer> themes = new ArrayList<>(Arrays.asList(3, 4, 5, 6));

        Log log = saveLog(userId, mapX, mapY, 0);

        // 핑 근처에 바다가 있을 때, 테마 추가
        if (!getLocationsWithinRadius(1, mapX, mapY, secondRadius).isEmpty()) {
            themes.add(1);
        }
        // 핑 근처에 산이 있을 때, 테마 추가
        if (!getLocationsWithinRadius(2, mapX, mapY, secondRadius).isEmpty()) {
            themes.add(2);
        }

        return recommendLocation(mapX, mapY, recommendList, themes, log, address);
    }

    // 완전 랜덤 추천 - 게스트 전용
    public RecommendDTO recommendRandomForGuest(double mapX, double mapY, String address) {

        ArrayList<Object> recommendList = new ArrayList<>();
        List<Integer> themes = new ArrayList<>(Arrays.asList(3, 4, 5, 6));

        // 핑 근처에 바다가 있을 때, 테마 추가
        if (!getLocationsWithinRadius(1, mapX, mapY, secondRadius).isEmpty()) {
            themes.add(1);
        }
        // 핑 근처에 산이 있을 때, 테마 추가
        if (!getLocationsWithinRadius(2, mapX, mapY, secondRadius).isEmpty()) {
            themes.add(2);
        }

        themes.forEach(theme -> {
            if (getLocationsWithinRadius(theme, mapX, mapY, firstRadius).size() < 2) {
                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, secondRadius);
                recommendList.addAll(randomSelect(themeLocations, 2));
            } else {
                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, firstRadius);
                recommendList.addAll(randomSelect(themeLocations, 2));
            }
        });

        return new RecommendDTO(null, address, recommendList);
    }

    // 테마에 따른 장소 추천
    private RecommendDTO recommendLocation(double mapX, double mapY, ArrayList<Object> recommendList, List<Integer> themes, Log log, String address) {

        Long logId = log.getLogId();
        recommendList.add("logId: " + logId);

        themes.forEach(theme -> {
            if (getLocationsWithinRadius(theme, mapX, mapY, firstRadius).size() < 2) {
                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, secondRadius);
                recommendList.addAll(randomSelect(themeLocations, 2));
            } else {
                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, firstRadius);
                recommendList.addAll(randomSelect(themeLocations, 2));
            }
        });

        createRecommend(recommendList, log);
        return new RecommendDTO(logId, address, recommendList);
    }

    private RecommendDTO recommendLocationTest(double mapX, double mapY, ArrayList<Object> recommendList, List<Integer> themes, Log log, String address) {

        User user = log.getUser();
        Long logId = log.getLogId();

        List<RecommendDTO.Recommendation> recommendations = new ArrayList<>();

        // 모든 항목을 하나의 리스트로 합침
        List<Object> allLocations = new ArrayList<>(recommendList);

        themes.forEach(theme -> {
            List<?> themeLocations;
            if (getLocationsWithinRadius(theme, mapX, mapY, firstRadius).size() < 2) {
                themeLocations = getLocationsWithinRadius(theme, mapX, mapY, secondRadius);
                themeLocations = randomSelect(themeLocations, 2);  // 최대 2개의 항목 선택
            } else {
                themeLocations = getLocationsWithinRadius(theme, mapX, mapY, firstRadius);
                themeLocations = randomSelect(themeLocations, 2);  // 최대 2개의 항목 선택
            }
            allLocations.addAll(themeLocations);
        });

        // allLocations 리스트에서 각 항목 처리
        allLocations.forEach(location -> {
            boolean isLiked = false;

            if (location instanceof Ocean ocean) {
                isLiked = likeService.checkIfAlreadyLiked(user, 1, ocean.getId());
            } else if (location instanceof Mountain mountain) {
                isLiked = likeService.checkIfAlreadyLiked(user, 2, mountain.getId());
            } else if (location instanceof Activity activity) {
                isLiked = likeService.checkIfAlreadyLiked(user, 3, activity.getId());
            } else if (location instanceof Tour tour) {
                isLiked = likeService.checkIfAlreadyLiked(user, 4, tour.getId());
            } else if (location instanceof Restaurant restaurant) {
                isLiked = likeService.checkIfAlreadyLiked(user, 5, restaurant.getId());
            } else if (location instanceof Cafe cafe) {
                isLiked = likeService.checkIfAlreadyLiked(user, 6, cafe.getId());
            }

            recommendations.add(new RecommendDTO.Recommendation(location, isLiked));
        });

        createRecommend(recommendList, log);
        return new RecommendDTO(logId, address, null, recommendations);
    }

    // 현재 위치 기반해서 radius 범위 이하의 장소를 찾음.
    public List<?> getLocationsWithinRadius(Integer theme, double mapX, double mapY, double radius) {
        switch (theme) {
            case 1: // Ocean
                return oceanRepository.findAllByDistance(mapX, mapY, radius);
            case 2: // Mountain
                return mountainRepository.findAllByDistance(mapX, mapY, radius);
            case 3: // Activity
                return activityRepository.findAllByDistance(mapX, mapY, radius);
            case 4: // Tour
                return tourRepository.findAllByDistance(mapX, mapY, radius);
            case 5: // Restaurant
                return restaurantRepository.findAllByDistance(mapX, mapY, radius);
            case 6: // Cafe
                return cafeRepository.findAllByDistance(mapX, mapY, radius);
            default:
                throw new IllegalArgumentException("findLocationById 에서 Type Error");
        }
    }

    private Ocean randomOcean() {
        Random random = new Random();
        Long randomOceanId = random.nextLong(422) + 1; // 1 ~ 422

        return oceanRepository.findById(randomOceanId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Ocean Not Found"));
    }

    private Mountain randomMountain() {
        Random random = new Random();
        Long randomMountainId = random.nextLong(636) + 1; // 1 ~ 636

        return mountainRepository.findById(randomMountainId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Mountain Not Found"));
    }

    private List<?> randomSelect(List<?> themeLocations, int size) {

        List<Object> themes = new ArrayList<>(themeLocations);
        if (themes.size() <= size) return themes;
        else {
            List<Object> selectedItems = new ArrayList<>();
            Collections.shuffle(themes, new Random());
            for (int i = 0; i < size; i++) {
                selectedItems.add(themes.get(i));
            }
            return selectedItems;
        }
    }

    private Log saveLog(Long userId, Double mapX, Double mapY, int dataType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));
        if (dataType == 1) {
            Log log = Log.log(mapX, mapY, "Ocean", user);
            logRepository.save(log);
            return log;
        } else if (dataType == 2) {
            Log log = Log.log(mapX, mapY, "Mountain", user);
            logRepository.save(log);
            return log;
        } else if (dataType == 3) {
            Log log = Log.log(mapX, mapY, "Activity", user);
            logRepository.save(log);
            return log;
        } else if (dataType == 4) {
            Log log = Log.log(mapX, mapY, "TourAttraction", user);
            logRepository.save(log);
            return log;
        } else if (dataType == 5) {
            Log log = Log.log(mapX, mapY, "Restaurant", user);
            logRepository.save(log);
            return log;
        } else if (dataType == 6) {
            Log log = Log.log(mapX, mapY, "Cafe", user);
            logRepository.save(log);
            return log;
        } else if (dataType == 0) {
            Log log = Log.log(mapX, mapY, "Random", user);
            logRepository.save(log);
            return log;
        } else {
            return null;
        }
    }

    private void createRecommend(List<Object> recommendList, Log log) {

        for (Object obj : recommendList) {
            Long id = null;
            Integer dataType = null;

            if (obj instanceof Ocean ocean) {
                id = ocean.getId();
                dataType = ocean.getDataType();
            } else if (obj instanceof Activity activity) {
                id = activity.getId();
                dataType = activity.getDataType();
            } else if (obj instanceof Tour tour) {
                id = tour.getId();
                dataType = tour.getDataType();
            } else if (obj instanceof Restaurant restaurant) {
                id = restaurant.getId();
                dataType = restaurant.getDataType();
            } else if (obj instanceof Cafe cafe) {
                id = cafe.getId();
                dataType = cafe.getDataType();
            }

            if (id != null && dataType != null) {
                Location location = Location.location(id, dataType);
                Recommend recommend = Recommend.recommend(log, location);
                locationRepository.save(location);
                recommendRepository.save(recommend);
            }
        }
    }

    private String parseAddress(String address) {
        String[] parts = address.split(" ");
        if (parts.length >= 3) {
            return parts[0] + " " + parts[1] + " " + parts[2];
        } else {
            return address; // 주소가 3 단락 이하인 경우 전체 주소 반환
        }
    }

    // 바다 테마 선택, 바다관련은 422개
    // 이 버전은 테마를 복수 선택할 수 있을 때, 쓸 수 있는 버전이다.
//    public List<Object> recommendOcean(List<Integer> themes, Long userId) {
//
//        ArrayList<Object> recommendList = new ArrayList<>();
//
//        Ocean ocean = randomOcean();
//        recommendList.add(ocean);
//
//        // 해수욕장 위치가 핑으로 찍힘
//        Double mapX = ocean.getMapX();
//        Double mapY = ocean.getMapY();
//
//        // 바다 테마의 경우는 해수욕장 위치가 추천 로그가 된다.
//        Log log = saveLog(userId, mapX, mapY); // 로그에 오류가 생겨도 로그는 그대로 남아야함.
//
//        // 해당 핑에서 테마가 겹치고 거리 안에 있으면 받아와서 랜덤으로 8가지 뽑고 추천!!
//        themes.forEach(theme -> {
//            if (getLocationsWithinRadius(theme, mapX, mapY, firstRadius).size() < 8) {
//                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, secondRadius);
//                recommendList.addAll(randomSelect8(themeLocations));
//            } else {
//                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, firstRadius);
//                recommendList.addAll(randomSelect8(themeLocations));
//            }
//        });
//
//        // 여기에서 recommendList 로 id, type 받아와서 location 생성 -> 각 recommend 만들면 될듯
//        createRecommend(recommendList, log);
//        return recommendList;
//    }

    // 바다 빼고 추천 (테마 선택 O) - 복수도 테마 선택이 가능한 버전
//    public List<?> recommendWithoutOcean(List<Integer> themes, double mapX, double mapY, Long userId) {
//
//        ArrayList<Object> recommendList = new ArrayList<>();
//        Log log = saveLog(userId, mapX, mapY);
//
//        themes.forEach(theme -> {
//            if (getLocationsWithinRadius(theme, mapX, mapY, firstRadius).size() < 2) {
//                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, secondRadius);
//                recommendList.addAll(randomSelect(themeLocations, 2));
//            } else {
//                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, firstRadius);
//                recommendList.addAll(randomSelect(themeLocations, 2));
//            }
//        });
//
//        createRecommend(recommendList, log);
//        return recommendList;
//    }
}
