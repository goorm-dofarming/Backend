package goorm.dofarming.domain.jpa.review.dto;

import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.location.entity.Location;

import java.util.List;

public record ReviewDTO(
        Long locationId,
        Double averageScore,
        int totalReview,
        String title,
        String addr,
        String tel,
        String image,
        Double mapX,
        Double mapY,
        int dataType,
        boolean liked,
        int countLikes
) {
    public static ReviewDTO of(Location location, boolean liked) {
        return new ReviewDTO(
                location.getLocationId(),
                location.getTotalScore(),
                location.getReviewCount(),
                location.getTitle(),
                location.getAddr(),
                location.getTel(),
                location.getImage(),
                location.getMapX(),
                location.getMapY(),
                Integer.parseInt(location.getTheme()),
                liked,
                location.getLikeCount()
        );
    }
}
