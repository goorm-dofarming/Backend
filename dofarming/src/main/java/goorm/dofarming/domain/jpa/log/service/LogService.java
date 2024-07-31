package goorm.dofarming.domain.jpa.log.service;

import goorm.dofarming.domain.jpa.like.service.LikeService;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.location.repository.LocationRepository;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.domain.jpa.log.entity.LogDTO;
import goorm.dofarming.domain.jpa.log.repository.LogRepository;
import goorm.dofarming.domain.jpa.recommend.entity.Recommend;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.infra.tourapi.domain.*;
import goorm.dofarming.infra.tourapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final LocationRepository locationRepository;
    private final OceanRepository oceanRepository;
    private final MountainRepository mountainRepository;
    private final ActivityRepository activityRepository;
    private final TourRepository tourRepository;
    private final RestaurantRepository restaurantRepository;
    private final CafeRepository cafeRepository;
    private final LikeService likeService;
    private final UserRepository userRepository;

    public LogDTO getLogData(Long logId) {

        List<Object> logsData = new ArrayList<>();
        Log log = logRepository.findById(logId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "로그가 존재하지 않습니다."));

        Long userId = log.getUser().getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 회원입니다."));

        List<Recommend> recommends = log.getRecommends();
        recommends.forEach(recommend -> {
            Long recommendId = recommend.getRecommendId();
            Location location = locationRepository.findById(recommendId)
                    .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "추천 장소가 존재하지 않습니다."));

            int dataType = location.getDataType();
            Long placeId = location.getPlaceId();

            Object recommendData = getRecommendData(dataType, placeId);

//            System.out.println(recommendData);
                logsData.add(recommendData);
        });

        isLiked(logsData, user);

        LogDTO logDTO = new LogDTO(log.getTheme(), log.getAddress(), log.getLongitude(), log.getLatitude(), logsData);

        return logDTO;
    }

    public <T> Object getRecommendData(int dataType, Long placeId) {

        return switch (dataType) {
            case 1 -> oceanRepository.findById(placeId)
                    .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 추천 장소입니다."));
            case 2 -> mountainRepository.findById(placeId)
                    .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 추천 장소입니다."));
            case 3 -> activityRepository.findById(placeId)
                    .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 추천 장소입니다."));
            case 4 -> tourRepository.findById(placeId)
                    .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 추천 장소입니다."));
            case 5 -> restaurantRepository.findById(placeId)
                    .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 추천 장소입니다."));
            case 6 -> cafeRepository.findById(placeId)
                    .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 추천 장소입니다."));
            default -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 추천 장소입니다.");
        };
    }

    private void isLiked(List<Object> recommendList, User user) {
        for (Object location : recommendList) {
            boolean isLiked = false;

            if (location instanceof Ocean ocean) {
                ocean.setLiked(likeService.checkIfAlreadyLiked(user, 1, ocean.getId()));
            } else if (location instanceof Mountain mountain) {
                mountain.setLiked(likeService.checkIfAlreadyLiked(user, 2, mountain.getId()));
            } else if (location instanceof Activity activity) {
                activity.setLiked(likeService.checkIfAlreadyLiked(user, 3, activity.getId()));
            } else if (location instanceof Tour tour) {
                tour.setLiked(likeService.checkIfAlreadyLiked(user, 4, tour.getId()));
            } else if (location instanceof Restaurant restaurant) {
                restaurant.setLiked(likeService.checkIfAlreadyLiked(user, 5, restaurant.getId()));
            } else if (location instanceof Cafe cafe) {
                cafe.setLiked(likeService.checkIfAlreadyLiked(user, 6, cafe.getId()));
            }

        }
    }

    public List<Log> getLogsByUserId(Long userId) {
        return logRepository.find100ByUser_UserId(userId);
    }
}
