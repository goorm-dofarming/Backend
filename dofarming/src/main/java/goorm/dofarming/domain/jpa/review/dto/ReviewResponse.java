package goorm.dofarming.domain.jpa.review.dto;

import goorm.dofarming.domain.jpa.image.dto.response.ImageResponse;
import goorm.dofarming.domain.jpa.image.entity.Image;
import goorm.dofarming.domain.jpa.review.entity.Review;
import goorm.dofarming.domain.jpa.user.dto.response.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

public record ReviewResponse(
        UserResponse user,
        Long reviewId,
        Double score,
        String content,
        List<ImageResponse> images
) {
    public static ReviewResponse of(Review review) {
        return new ReviewResponse(
                UserResponse.of(review.getUser()),
                review.getReviewId(),
                review.getScore(),
                review.getContent(),
                review.getImages().stream().map(ImageResponse::of).collect(Collectors.toList())
        );
    }
}
