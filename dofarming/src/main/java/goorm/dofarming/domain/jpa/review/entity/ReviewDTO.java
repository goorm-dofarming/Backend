package goorm.dofarming.domain.jpa.review.entity;

import java.util.List;

public record ReviewDTO(
        Long reviewId,
        Double score,
        Double averageScore,
        String content,
        String userImageUrl,
        String userNickname,
        List<String> imageUrls
) {
}
