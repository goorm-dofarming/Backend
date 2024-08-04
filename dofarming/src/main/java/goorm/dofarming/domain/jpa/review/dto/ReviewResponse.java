package goorm.dofarming.domain.jpa.review.dto;

import java.util.List;

public record ReviewResponse(
        Long reviewId,
        Double score,
        Double averageScore,
        String content,
        String userImageUrl,
        String userNickname,
        List<String> imageUrls
) {
}
