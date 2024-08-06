package goorm.dofarming.domain.jpa.location.service;

import goorm.dofarming.domain.jpa.like.repository.LikeRepository;
import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.location.repository.LocationRepository;
import goorm.dofarming.domain.jpa.review.repository.ReviewRepository;
import goorm.dofarming.domain.jpa.user.dto.response.UserResponse;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public LocationResponse getLocation(Long userId, Long locationId) {
        User user = userRepository.findByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 유저입니다."));

        Location location = locationRepository.findByLocationIdAndStatus(locationId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 장소입니다."));

        boolean liked = likeRepository.existsByLocation_LocationIdAndUser_UserIdAndStatus(location.getLocationId(), user.getUserId(), Status.ACTIVE);
        boolean isReviewed = reviewRepository.existsByLocation_LocationIdAndUser_UserIdAndStatus(location.getLocationId(), user.getUserId(), Status.ACTIVE);
        return LocationResponse.review(liked, isReviewed, location);
    }
}
