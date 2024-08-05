package goorm.dofarming.domain.jpa.review.dto;

import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.location.entity.Location;

public record ReviewLocationResponse(
        Long locationId,
        Double averageScore,
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
    public static ReviewLocationResponse of(boolean liked, Location location, Double averageScore) {
        return new ReviewLocationResponse(
                location.getLocationId(),
                averageScore,
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
