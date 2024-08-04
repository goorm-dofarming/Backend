package goorm.dofarming.domain.jpa.review.dto;

import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.location.entity.Location;

import java.util.List;

public record ReviewDTO(
        ReviewLocationResponse locationResponse,
        List<ReviewResponse> reviewResponses
) {
    public static ReviewDTO of(Location location, List<ReviewResponse> reviewResponses, boolean liked, Double averageScore) {
        return new ReviewDTO(
                ReviewLocationResponse.of(liked, location, averageScore),
                reviewResponses
        );
    }
}
