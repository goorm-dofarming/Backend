package goorm.dofarming.domain.jpa.like.service;

import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.like.entity.LikeDTO;
import goorm.dofarming.domain.jpa.like.repository.LikeRepository;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.infra.tourapi.domain.*;
import goorm.dofarming.infra.tourapi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    public List<Object> getLikeList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원이 존재하지 않습니다."));

        List<Like> likes = user.getLikes();
        List<Object> likedLocations = new ArrayList<>();

        for (Like like : likes) {

            if (like.getStatus() == Status.ACTIVE) {
                if (like.getCafe() != null) {
                    Cafe cafe = like.getCafe();
                    likedLocations.add(new LikeDTO(like.getLikeId(), "Cafe", cafe.getTitle(), cafe.getAddr(), cafe.getTel(), cafe.getImage(), cafe.getMapX(), cafe.getMapY(), cafe.getLikeCount()));
                } else if (like.getOcean() != null) {
                    Ocean ocean = like.getOcean();
                    likedLocations.add(new LikeDTO(like.getLikeId(), "Ocean", ocean.getTitle(), ocean.getAddr(), ocean.getTel(), ocean.getImage(), ocean.getMapX(), ocean.getMapY(), ocean.getLikeCount()));
                } else if (like.getMountain() != null) {
                    Mountain mountain = like.getMountain();
                    likedLocations.add(new LikeDTO(like.getLikeId(), "Mountain", mountain.getTitle(), mountain.getAddr(), mountain.getTel(), mountain.getImage(), mountain.getMapX(), mountain.getMapY(), mountain.getLikeCount()));
                } else if (like.getActivity() != null) {
                    Activity activity = like.getActivity();
                    likedLocations.add(new LikeDTO(like.getLikeId(), "Activity", activity.getTitle(), activity.getAddr(), activity.getTel(), activity.getImage(), activity.getMapX(), activity.getMapY(), activity.getLikeCount()));
                } else if (like.getTour() != null) {
                    Tour tour = like.getTour();
                    likedLocations.add(new LikeDTO(like.getLikeId(), "Tour", tour.getTitle(), tour.getAddr(), tour.getTel(), tour.getImage(), tour.getMapX(), tour.getMapY(), tour.getLikeCount()));
                } else if (like.getRestaurant() != null) {
                    Restaurant restaurant = like.getRestaurant();
                    likedLocations.add(new LikeDTO(like.getLikeId(), "Restaurant", restaurant.getTitle(), restaurant.getAddr(), restaurant.getTel(), restaurant.getImage(), restaurant.getMapX(), restaurant.getMapY(), restaurant.getLikeCount()));
                }
            }
        }
        return likedLocations;
    }

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
