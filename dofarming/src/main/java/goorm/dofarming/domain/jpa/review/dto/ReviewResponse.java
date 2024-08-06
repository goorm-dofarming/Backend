package goorm.dofarming.domain.jpa.review.dto;

import goorm.dofarming.domain.jpa.image.dto.response.ImageResponse;
import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.domain.jpa.user.dto.response.UserResponse;
import goorm.dofarming.global.common.entity.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ReviewResponse(
        UserResponse user,
        Long reviewId,
        Double score,
        Integer reviewLikeCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String content,
        List<ImageResponse> images,
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
