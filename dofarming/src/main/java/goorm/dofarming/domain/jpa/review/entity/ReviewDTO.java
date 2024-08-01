package goorm.dofarming.domain.jpa.review.entity;

import java.util.List;

public record ReviewDTO(
        Double score,
        String content,
        List<String> imageUrls
) {
}
