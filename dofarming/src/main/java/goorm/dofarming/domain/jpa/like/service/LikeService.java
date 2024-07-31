package goorm.dofarming.domain.jpa.like.service;

import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.like.entity.LikeDTO;
import goorm.dofarming.domain.jpa.like.repository.LikeRepository;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.location.repository.LocationRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public List<LikeDTO> getLikeList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원이 존재하지 않습니다."));

        return likeRepository.findAllByUser_UserIdAndStatus(user.getUserId(), Status.ACTIVE)
                .stream().map(like -> {
                    Location location = like.getLocation();
                    return LikeDTO.of(location);
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
