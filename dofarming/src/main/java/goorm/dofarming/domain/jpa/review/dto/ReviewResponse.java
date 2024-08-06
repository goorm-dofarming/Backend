package goorm.dofarming.domain.jpa.review.dto;

import goorm.dofarming.domain.jpa.image.dto.response.ImageResponse;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.domain.jpa.user.dto.response.UserResponse;
import goorm.dofarming.global.common.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "리뷰 응답 정보를 담는 DTO")
public record ReviewResponse(
        @Schema(description = "사용자 정보")
        UserResponse user,

        @Schema(description = "리뷰 ID", example = "1")
        Long reviewId,

        @Schema(description = "점수", example = "4.5")
        Double score,

        @Schema(description = "리뷰 좋아요 수", example = "10")
        Integer reviewLikeCount,

        @Schema(description = "리뷰 생성 시간", example = "2024-08-06T14:34:56")
        LocalDateTime createdAt,

        @Schema(description = "리뷰 수정 시간", example = "2024-08-06T14:58:56")
        LocalDateTime updatedAt,

        @Schema(description = "리뷰 내용", example = "Good!")
        String content,

        @Schema(description = "이미지 리스트")
        List<ImageResponse> images,

        @Schema(description = "좋아요 여부", example = "true")
        boolean liked
) {
    public static ReviewResponse of(boolean liked, Review review) {
        return new ReviewResponse(
                UserResponse.of(review.getUser()),
                review.getReviewId(),
                review.getScore(),
                review.getReviewLikeCount(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                review.getContent(),
                review.getImages().stream().filter(image -> image.getStatus().equals(Status.ACTIVE)).map(ImageResponse::of).collect(Collectors.toList()),
                liked
        );
    }
}
