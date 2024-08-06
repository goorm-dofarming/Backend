package goorm.dofarming.domain.jpa.recommend.service;

import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.image.repository.ImageRepository;
import goorm.dofarming.domain.jpa.like.repository.LikeRepository;
import goorm.dofarming.domain.jpa.like.service.LikeService;
import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
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
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.infra.tourapi.domain.*;
import goorm.dofarming.infra.tourapi.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendRepository recommendRepository;
    private final LogRepository logRepository;
    private final OceanRepository oceanRepository;
    private final MountainRepository mountainRepository;
    private final ActivityRepository activityRepository;
    private final TourRepository tourRepository;
    private final RestaurantRepository restaurantRepository;
    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final ImageRepository imageRepository;
    private final static double firstRadius = RecommendConfig.firstRadius;
    private final static double secondRadius = RecommendConfig.secondRadius;

    // 바다 테마 선택, 바다 테마의 종류는 422가지
    public RecommendDTO recommendOcean(Long userId) {
        User user = getExistUser(userId);

        List<Location> recommendList = new ArrayList<>();
        List<Integer> themes = new ArrayList<>(Arrays.asList(3, 4, 5, 6));

        Ocean ocean = randomOcean();
        recommendList.add(ocean);

        // 해수욕장 위치가 핑으로 찍힘
        Double mapX = ocean.getMapX();
        Double mapY = ocean.getMapY();

        String address = parseAddress(ocean.getAddr());

        // 산 혹은 바다 테마의 경우는 해수욕장 위치가 추천 로그가 된다.
        Log log = logRepository.save(Log.log(mapX, mapY, "1", address, user)); // 로그에 오류가 생겨도 로그는 그대로 남아야함.
//        Log log = saveLog(userId, mapX, mapY, 1); // 로그에 오류가 생겨도 로그는 그대로 남아야함.

        // 해당 핑에서 테마가 겹치고 거리 안에 있으면 받아와서 랜덤으로 2가지 뽑고 추천!!
        return recommendLocation(mapX, mapY, recommendList, themes, log, user.getUserId());
    }

    // 산 테마 선택, 산 테마의 종류는 636가지
    public RecommendDTO recommendMountain(Long userId) {
        
        User user = getExistUser(userId);

        List<Location> recommendList = new ArrayList<>();
        List<Integer> themes = new ArrayList<>(Arrays.asList(3, 4, 5, 6));

        Mountain mountain = randomMountain();
        recommendList.add(mountain);

        // 산의 위치가 핑으로 찍힘
        Double mapX = mountain.getMapX();
        Double mapY = mountain.getMapY();

        String address = parseAddress(mountain.getAddr());

        // 산 혹은 바다 테마의 경우는 해수욕장 위치가 추천 로그가 된다.
        Log log = logRepository.save(Log.log(mapX, mapY, "2", address, user)); // 로그에 오류가 생겨도 로그는 그대로 남아야함.
//        Log log = saveLog(userId, mapX, mapY, 2); // 로그에 오류가 생겨도 로그는 그대로 남아야함.

        // 해당 핑에서 테마가 겹치고 거리 안에 있으면 받아와서 랜덤으로 2가지 뽑고 추천!!
        return recommendLocation(mapX, mapY, recommendList, themes, log, user.getUserId());
    }

    private User getExistUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));
    }

    // ( 바다, 산을 제외한 ) 테마 선택
    public RecommendDTO recommendTheme(int dataType, double mapX, double mapY, Long userId, String address) {

        User user = getExistUser(userId);

        Log log = logRepository.save(Log.log(mapX, mapY, String.valueOf(dataType), address, user)); // 로그에 오류가 생겨도 로그는 그대로 남아야함.
//        Log log = saveLog(userId, mapX, mapY, dataType); // 로그에 오류가 생겨도 로그는 그대로 남아야함.

        List<Location> recommendList = getLocationsWithinRadius(dataType, mapX, mapY, firstRadius);

        if (recommendList.size() < 8) {
            List<Location> themeLocations = getLocationsWithinRadius(dataType, mapX, mapY, secondRadius);
            recommendList = randomSelect(themeLocations, 8);
        } else {
            recommendList = randomSelect(recommendList, 8);
        }

        createRecommend(recommendList, log);

        List<LocationResponse> locations = recommendList.stream()
                .map(location -> {
                    Image image = imageRepository.findTopImageByReviewLike(location.getLocationId())
                            .orElse(null);
                    boolean liked = likeRepository.existsByLocation_LocationIdAndUser_UserIdAndStatus(location.getLocationId(), user.getUserId(), Status.ACTIVE);
                    return LocationResponse.user(liked, image.getImageUrl(), location);
                })
                .collect(Collectors.toList());

        return RecommendDTO.of(log, locations);
    }

    // 완전 랜덤 추천 (테마 선택 X) - 유저 전용
    // 이 경우에는 바다 테마를 검색해보고 주변에 바다가 있으면 6개 중 랜덤 아니면 5개 중 랜덤 2개 선택해서 보내줌.
    public RecommendDTO recommendRandomForUser(double mapX, double mapY, Long userId, String address) {

        User user = getExistUser(userId);

        ArrayList<Location> recommendList = new ArrayList<>();
        List<Integer> themes = new ArrayList<>(Arrays.asList(3, 4, 5, 6));

        Log log = logRepository.save(Log.log(mapX, mapY, "0", address, user)); // 로그에 오류가 생겨도 로그는 그대로 남아야함.
//        Log log = saveLog(userId, mapX, mapY, 0); // 로그에 오류가 생겨도 로그는 그대로 남아야함.

        // 핑 근처에 바다가 있을 때, 테마 추가
        if (!getLocationsWithinRadius(1, mapX, mapY, secondRadius).isEmpty()) {
            themes.add(1);
        }
        // 핑 근처에 산이 있을 때, 테마 추가
        if (!getLocationsWithinRadius(2, mapX, mapY, secondRadius).isEmpty()) {
            themes.add(2);
        }

        return recommendLocation(mapX, mapY, recommendList, themes, log, user.getUserId());
    }

    // 완전 랜덤 추천 - 게스트 전용
    public RecommendDTO recommendRandomForGuest(double mapX, double mapY, String address) {

        List<Integer> themes = new ArrayList<>(Arrays.asList(3, 4, 5, 6));

        // 핑 근처에 바다가 있을 때, 테마 추가
        if (!getLocationsWithinRadius(1, mapX, mapY, secondRadius).isEmpty()) {
            themes.add(1);
        }
        // 핑 근처에 산이 있을 때, 테마 추가
        if (!getLocationsWithinRadius(2, mapX, mapY, secondRadius).isEmpty()) {
            themes.add(2);
        }

        List<Location> recommendList = new ArrayList<>();

        themes.forEach(theme -> {

            List<Location> firstLocations = getLocationsWithinRadius(theme, mapX, mapY, firstRadius);

            if (firstLocations.size() < 2) {
                List<Location> themeLocations = getLocationsWithinRadius(theme, mapX, mapY, secondRadius);
                recommendList.addAll(randomSelect(themeLocations, 2));
            } else {
                recommendList.addAll(randomSelect(firstLocations, 2));
            }
        });

        List<LocationResponse> locations = recommendList.stream()
                .map(location -> {
                            Image image = imageRepository.findTopImageByReviewLike(location.getLocationId())
                                    .orElse(null);
                            return LocationResponse.guest(image.getImageUrl(), location);
                })
                .collect(Collectors.toList());

        return RecommendDTO.guest(address, mapX, mapY, locations);
    }

    private RecommendDTO recommendLocation(double mapX, double mapY, List<Location> recommendList, List<Integer> themes, Log log, Long userId) {
        themes.forEach(theme -> {
            List<Location> firstLocations = getLocationsWithinRadius(theme, mapX, mapY, firstRadius);
            if (firstLocations.size() < 2) {
                List<Location> secondLocations = getLocationsWithinRadius(theme, mapX, mapY, secondRadius);
                recommendList.addAll(randomSelect(secondLocations, 2));
            } else {
                recommendList.addAll(randomSelect(firstLocations, 2));
            }
        });

        createRecommend(recommendList, log);

        List<LocationResponse> locations = recommendList.stream()
                .map(location -> {
                    Image image = imageRepository.findTopImageByReviewLike(location.getLocationId())
                            .orElse(null);
                    boolean liked = likeRepository.existsByLocation_LocationIdAndUser_UserIdAndStatus(location.getLocationId(), userId, Status.ACTIVE);
                    return LocationResponse.user(liked, image.getImageUrl(), location);
                })
                .collect(Collectors.toList());

        return RecommendDTO.of(log, locations);
    }

    // 현재 위치 기반해서 radius 범위 이하의 장소를 찾음.
    @Transactional(readOnly = true)
    public List<Location> getLocationsWithinRadius(Integer theme, double mapX, double mapY, double radius) {

        List<? extends Location> results;

        switch (theme) {
            case 1: // Ocean
                results = oceanRepository.findAllByDistance(mapX, mapY, radius);
                break;
            case 2: // Mountain
                results = mountainRepository.findAllByDistance(mapX, mapY, radius);
                break;
            case 3: // Activity
                results = activityRepository.findAllByDistance(mapX, mapY, radius);
                break;
            case 4: // Tour
                results = tourRepository.findAllByDistance(mapX, mapY, radius);
                break;
            case 5: // Restaurant
                results = restaurantRepository.findAllByDistance(mapX, mapY, radius);
                break;
            case 6: // Cafe
                results = cafeRepository.findAllByDistance(mapX, mapY, radius);
                break;
            default:
                throw new CustomException(ErrorCode.BAD_REQUEST, "존재하지 않는 타입입니다.");
        }

        return new ArrayList<>(results);
    }

    private Ocean randomOcean() {
        Random random = new Random();
        Long randomOceanId = random.nextLong(421) + 22702; // 1 ~ 421

        return oceanRepository.findById(randomOceanId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Ocean Not Found"));
    }

    private Mountain randomMountain() {
        Random random = new Random();
        Long randomMountainId = random.nextLong(636) + 23123; // 1 ~ 636

        return mountainRepository.findById(randomMountainId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "Mountain Not Found"));
    }

    private List<Location> randomSelect(List<Location> themeLocations, int size) {

        List<Location> themes = new ArrayList<>(themeLocations);

        if (themes.size() <= size) return themes;

        Collections.shuffle(themes);

        return themes.subList(0, size);
    }

    private void createRecommend(List<Location> recommendList, Log log) {
        for (Location location : recommendList) {
            recommendRepository.save(Recommend.recommend(log, location));
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
}
