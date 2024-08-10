package goorm.dofarming.domain.jpa.like.service;

import goorm.dofarming.domain.jpa.chatroom.entity.Region;
import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.image.repository.ImageRepository;
import goorm.dofarming.domain.jpa.like.dto.response.LikeResponse;
import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.like.entity.SortType;
import goorm.dofarming.domain.jpa.like.repository.LikeRepository;
import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.location.repository.LocationRepository;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.infra.tourapi.domain.DataType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ImageRepository imageRepository;

    public List<LikeResponse> getLikeList(Long userId, Long likeId, LocalDateTime updatedAt, Integer likeCount, Double avgScore, String title, List<String> themes, List<Region> regions, SortType sortType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원이 존재하지 않습니다."));

        return likeRepository.search(user.getUserId(), likeId, updatedAt, likeCount, avgScore, title, themes, regions, sortType)
                .stream().map(like -> {
                    String imageUrl = imageRepository.findTopImageByReviewLike(like.getLocation().getLocationId())
                            .map(Image::getImageUrl).orElse("");
                    return LikeResponse.of(imageUrl, like);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void like(Long userId, Long locationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원 정보를 찾을 수 없습니다."));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "장소를 찾을 수 없습니다."));

        likeRepository.findByUser_UserIdAndLocation_LocationId(user.getUserId(), location.getLocationId())
                .ifPresentOrElse(
                        like -> {
                            if (like.getStatus().equals(Status.ACTIVE)) {
                                like.delete();
                            } else {
                                like.active();
                            }
                        },
                        () -> likeRepository.save(Like.like(user, location))
                );
    }
}
