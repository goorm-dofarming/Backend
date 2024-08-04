package goorm.dofarming.domain.jpa.review.dto;

import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.location.entity.Location;

import java.util.List;

public record ReviewDTO(
        LocationResponse locationResponse,
        List<ReviewResponse> reviewResponses
) {
    public static ReviewDTO of(Location location, List<ReviewResponse> reviewResponses, boolean liked) {
        return new ReviewDTO(
                LocationResponse.of(liked, location),
                reviewResponses
        );
    }
}
