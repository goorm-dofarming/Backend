package goorm.dofarming.domain.jpa.review_like.controller;

import goorm.dofarming.domain.jpa.review_like.service.ReviewLikeService;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping("/reviewLike")
    public ResponseEntity<Void> like(
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter(description = "리뷰 ID") @RequestParam Long reviewId
    ) {
        reviewLikeService.reviewLike(user.getUserId(), reviewId);
        return ResponseEntity.noContent().build();
    }
}
