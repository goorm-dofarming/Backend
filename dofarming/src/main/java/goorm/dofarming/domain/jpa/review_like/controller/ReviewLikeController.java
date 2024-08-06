package goorm.dofarming.domain.jpa.review_like.controller;

import goorm.dofarming.domain.jpa.review_like.service.ReviewLikeService;
import goorm.dofarming.global.auth.DofarmingUserDetails;
import goorm.dofarming.global.common.error.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ReviewLike", description = "Review Like 관련 API")
@RestController
@RequiredArgsConstructor
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @Operation(
            operationId = "likeReview",
            summary = "리뷰 좋아요/취소 API",
            responses = {
                    @ApiResponse(responseCode = "204", description = "요청이 성공적으로 처리됨", content = @Content),
                    @ApiResponse(responseCode = "404", description = "회원 정보 또는 리뷰를 찾을 수 없음", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @PostMapping("/reviewLike")
    public ResponseEntity<Void> like(
            @Parameter(description = "인증된 사용자 정보") @AuthenticationPrincipal DofarmingUserDetails user,
            @Parameter(description = "리뷰 ID") @RequestParam Long reviewId
    ) {
        reviewLikeService.reviewLike(user.getUserId(), reviewId);
        return ResponseEntity.noContent().build();
    }
}
