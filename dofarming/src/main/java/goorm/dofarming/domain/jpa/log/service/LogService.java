package goorm.dofarming.domain.jpa.log.service;

import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.like.repository.LikeRepository;
import goorm.dofarming.domain.jpa.like.service.LikeService;
import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.location.repository.LocationRepository;
import goorm.dofarming.domain.jpa.log.dto.response.LogResponse;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.domain.jpa.log.repository.LogRepository;
import goorm.dofarming.domain.jpa.recommend.entity.RecommendDTO;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public RecommendDTO getUserLogData(Long userId, Long logId) {
        User user = userRepository.findByUserIdAndStatus(userId, Status.ACTIVE)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 유저입니다."));

        Log log = logRepository.findById(logId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "로그가 존재하지 않습니다."));

        List<LocationResponse> locations = log.getRecommends().stream()
                .map(recommend -> {
                    boolean liked = likeRepository.existsByLocation_LocationIdAndUser_UserIdAndStatus(recommend.getLocation().getLocationId(), user.getUserId(), Status.ACTIVE);
                    return LocationResponse.user(liked, recommend.getLocation());
                })
                .collect(Collectors.toList());

        return RecommendDTO.of(log, locations);
    }

    public RecommendDTO getGuestLogData(Long logId) {
        Log log = logRepository.findById(logId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "로그가 존재하지 않습니다."));

        List<LocationResponse> locations = log.getRecommends().stream()
                .map(recommend -> LocationResponse.guest(recommend.getLocation()))
                .collect(Collectors.toList());

        return RecommendDTO.of(log, locations);
    }

    public List<LogResponse> getLogsByUserId(Long userId) {
        return logRepository.find100ByUser_UserId(userId)
                .stream().map(LogResponse::of).collect(Collectors.toList());
    }
}
