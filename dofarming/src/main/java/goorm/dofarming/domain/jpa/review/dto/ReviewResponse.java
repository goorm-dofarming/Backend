package goorm.dofarming.domain.jpa.review.dto;

import java.util.List;

public record ReviewResponse(
        Long reviewId,
        Double score,
        String content,
        String userImageUrl,
        String userNickname,
        int reviewCount,
        Double averageReviewScore,
        List<String> imageUrls
) {
}
