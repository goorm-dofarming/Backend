package goorm.dofarming.domain.jpa.like.controller;

import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.like.service.LikeService;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like")
    public Like like(
            @AuthenticationPrincipal DofarmingUserDetails user,
            @RequestParam Long placeId,
            @RequestParam int dataType
    ) {
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
        Long userId = user.getUserId();

        return likeService.like(userId, placeId, dataType);
    }

    @GetMapping("/likeList")
    private List<?> likeList(@AuthenticationPrincipal DofarmingUserDetails user) {
        if (user == null) throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "회원정보가 일치하지 않습니다.");
        Long userId = user.getUserId();

        return likeService.getLikeList(userId);
    }
}
