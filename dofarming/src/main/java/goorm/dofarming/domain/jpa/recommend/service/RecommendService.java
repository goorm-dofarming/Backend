package goorm.dofarming.domain.jpa.recommend.service;

import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.location.repository.LocationRepository;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.domain.jpa.log.repository.LogRepository;
import goorm.dofarming.domain.jpa.recommend.repository.RecommendRepository;
import goorm.dofarming.domain.jpa.recommend.util.RecommendConfig;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.infra.tourapi.domain.Ocean;
import goorm.dofarming.infra.tourapi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {

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
    private final static double firstRadius = RecommendConfig.firstRadius;
    private final static double secondRadius = RecommendConfig.secondRadius;

    // 바다 포함 추천, 바다관련은 422개 (테마 선택 O)
    public List<Object> recommendWithOcean(List<Integer> themes) {

        ArrayList<Object> recommendList = new ArrayList<>();

        Ocean ocean = randomOcean();
        recommendList.add(ocean);

        // 해수욕장 위치가 핑으로 찍힘
        Double mapX = ocean.getMapX();
        Double mapY = ocean.getMapY();

        // 해당 핑에서 테마가 겹치고 거리 안에 있으면 받아와서 랜덤으로 8가지 뽑고 추천!!
        themes.forEach(theme -> {
            if (getLocationsWithinRadius(theme, mapX, mapY, firstRadius).size() < 8) {
                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, secondRadius);
                recommendList.addAll(randomSelect8(themeLocations));
            } else {
                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, firstRadius);
                recommendList.addAll(randomSelect8(themeLocations));
            }
        });

        return recommendList;
    }

    public List<Object> recommendWithOcean(List<Integer> themes, Long userId) {

        ArrayList<Object> recommendList = new ArrayList<>();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        Ocean ocean = randomOcean();
        recommendList.add(ocean);

        // 해수욕장 위치가 핑으로 찍힘
        Double mapX = ocean.getMapX();
        Double mapY = ocean.getMapY();

        Log log = Log.log(mapX, mapY, "Ocean", user);

        // 해당 핑에서 테마가 겹치고 거리 안에 있으면 받아와서 랜덤으로 8가지 뽑고 추천!!
        themes.forEach(theme -> {
            if (getLocationsWithinRadius(theme, mapX, mapY, firstRadius).size() < 8) {
                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, secondRadius);
//                Location.location();
                recommendList.addAll(randomSelect8(themeLocations));
            } else {
                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, firstRadius);
                recommendList.addAll(randomSelect8(themeLocations));
            }
        });

        return recommendList;
    }


    // 바다 빼고 추천 (테마 선택 O)
    public List<?> recommendWithoutOcean(List<Integer> themes, double mapX, double mapY) {

        ArrayList<Object> recommendList = new ArrayList<>();

        themes.forEach(theme -> {
            if (getLocationsWithinRadius(theme, mapX, mapY, firstRadius).size() < 8) {
                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, secondRadius);
                recommendList.addAll(randomSelect8(themeLocations));
            } else {
                List<?> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, firstRadius);
                recommendList.addAll(randomSelect8(themeLocations));
            }
        });

        return recommendList;
    }

    // 완전 랜덤 추천 (테마 선택 X)
    // 이 경우에는 바다 테마를 검색해보고 주변에 바다가 있으면 6개 중 랜덤 아니면 5개 중 랜덤 2개 선택해서 보내줌.
    public List<?> recommendRandom(double mapX, double mapY) {

        ArrayList<Object> recommendList = new ArrayList<>();
        ArrayList<Integer> selectedThemes = new ArrayList<>();

        // 핑 근처에 바다가 없을 때, 바다를 제외한 테마 중에서 2가지 테마를 골라서 추천
        if (getLocationsWithinRadius(1, mapX, mapY, secondRadius).isEmpty()) randomSelectTheme(selectedThemes, false);
            // 핑 근처제 바다가 있을때, 전체 테마 중에서 2가지 테마를 골라서 추천
        else randomSelectTheme(selectedThemes, true);

        if (getLocationsWithinRadius(selectedThemes.get(0), mapX, mapY, firstRadius).size() < 8) {
            List<?> theme1Locations = getLocationsWithinRadius(selectedThemes.get(0), mapX, mapY, secondRadius);
            recommendList.addAll(theme1Locations);
        } else {
            List<?> theme1Locations = getLocationsWithinRadius(selectedThemes.get(0), mapX, mapY, firstRadius);
            recommendList.addAll(theme1Locations);
        }
        if (getLocationsWithinRadius(selectedThemes.get(1), mapX, mapY, firstRadius).size() < 8) {
            List<?> theme2Locations = getLocationsWithinRadius(selectedThemes.get(1), mapX, mapY, secondRadius);
            recommendList.addAll(theme2Locations);
        } else {
            List<?> theme2Locations = getLocationsWithinRadius(selectedThemes.get(1), mapX, mapY, firstRadius);
            recommendList.addAll(theme2Locations);
        }

        return recommendList;
    }

    // 현재 위치 기반해서 radius 이하의 거리를 찾음.
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

    public String returnDataType(Integer theme) {
        return switch (theme) {
            case 1 -> // Ocean
                    "Ocean";
            case 2 -> // Mountain
                    "Mountain";
            case 3 -> // Activity
                    "Activity";
            case 4 -> // Tour
                    "Tour";
            case 5 -> // Restaurant
                    "Restaurant";
            case 6 -> // Cafe
                    "Cafe";
            default -> throw new IllegalArgumentException("Type Error");
        };
    }

    private Ocean randomOcean() {
        Random random = new Random();
        Long randomOcean = random.nextLong(422) + 1; // 1 ~ 422

        return oceanRepository.findById(randomOcean)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Ocean Not Found"));
    }

    private List<?> randomSelect8(List<?> themeLocations) {
        List<Object> themes = new ArrayList<>(themeLocations);
        if (themes.size() <= 8) return themes;
        else {
            List<Object> selectedItems = new ArrayList<>();
            Collections.shuffle(themes, new Random());
            for (int i = 0; i < 8; i++) {
                selectedItems.add(themes.get(i));
            }
            return selectedItems;
        }
    }

    private static void randomSelectTheme(List<Integer> selectedThemes, boolean withinOcean) {
        Random random = new Random();
        ArrayList<Integer> themes = new ArrayList<>();

        if (withinOcean) {
            for (Integer i = 1; i <= 6; i++) {
                themes.add(i);
            }
        } else {
            for (int i = 2; i <= 6; i++) {
                themes.add(i);
            }
        }

        Collections.shuffle(themes, random);
        selectedThemes.add(themes.get(0));
        selectedThemes.add(themes.get(1));
        return;
    }
}
