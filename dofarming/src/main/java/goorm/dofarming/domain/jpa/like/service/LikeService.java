package goorm.dofarming.domain.jpa.like.service;

import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.like.repository.LikeRepository;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.infra.tourapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final OceanRepository oceanRepository;
    private final MountainRepository mountainRepository;
    private final ActivityRepository activityRepository;
    private final TourRepository tourRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Like like(Long userId, Long placeId, int dataType) {

        Object location;
        switch (dataType) {
            case 1:
                location = oceanRepository.findById(placeId)
                        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "장소를 찾을 수 없습니다."));
                break;
            case 2:
                location = mountainRepository.findById(placeId)
                        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "장소를 찾을 수 없습니다."));
                break;
            case 3:
                location = activityRepository.findById(placeId)
                        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "장소를 찾을 수 없습니다."));
                break;
            case 4:
                location = tourRepository.findById(placeId)
                        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "장소를 찾을 수 없습니다."));
                break;
            case 5:
                location = restaurantRepository.findById(placeId)
                        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "장소를 찾을 수 없습니다."));
                break;
            case 6:
                location = cafeRepository.findById(placeId)
                        .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "장소를 찾을 수 없습니다."));
                break;
            default:
                throw new CustomException(ErrorCode.INVALID_PARAMETER, "잘못된 데이터 타입입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원 정보를 찾을 수 없습니다."));



        return likeRepository.save(Like.like(user, location));
    }

}
