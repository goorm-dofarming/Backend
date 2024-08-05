package goorm.dofarming.domain.jpa.review.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewResponse(
        Long reviewId,
        Double score,
        String content,
        LocalDateTime createdAt,
        Long userId,
        String userImageUrl,
        String userNickname,
        int reviewCount,
        Double averageReviewScore,
        List<String> imageUrls
) {
}
