package goorm.dofarming.domain.jpa.review.entity;

import java.util.List;

public record ReviewDTO(
        Long reviewId,
        Double score,
        String content,
        List<String> imageUrls
) {
}
