package goorm.dofarming.domain.jpa.like.service;

import goorm.dofarming.domain.jpa.like.entity.LikeV2;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.domain.jpa.user.repository.UserRepository;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import goorm.dofarming.infra.tourapi.domain.Cafe;
import goorm.dofarming.infra.tourapi.repository.CafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final CafeRepository cafeRepository;
    private final UserRepository userRepository;

    public LikeV2 like(Long userId, Long placeId, int dataType) {
        Cafe cafe = cafeRepository.findById(placeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "장소를 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원 정보를 찾을 수 없습니다."));

        return LikeV2.like(user, cafe);
    }

}
