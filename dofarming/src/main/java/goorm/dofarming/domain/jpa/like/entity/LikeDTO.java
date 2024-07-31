package goorm.dofarming.domain.jpa.like.entity;

import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.infra.tourapi.domain.*;
import jakarta.persistence.DiscriminatorValue;

public record LikeDTO(
        Long id,
        String type,
        String title,
        String addr,
        String tel,
        String image,
        Double mapX,
        Double mapY,
        int likeCount
) {
    public static LikeDTO of(Location location) {
        return new LikeDTO(
                location.getLocationId(),
                instanceofType(location),
                location.getTitle(),
                location.getAddr(),
                location.getTel(),
                location.getImage(),
                location.getMapX(),
                location.getMapY(),
                getLikeCount(location)
        );
    }

    public static String instanceofType(Location location) {
        switch (location.getTheme()) {
            case "1":
                return "Ocean";
            case "2":
                return "Mountain";
            case "3":
                return "Activity";
            case "4":
                return "Tour";
            case "5":
                return "Restaurant";
            case "6":
                return "Cafe";
            default:
                throw new IllegalArgumentException("Unknown type: " + location.getTheme());
        }
    }

    public static int getLikeCount(Location location) {
        return (int) location.getLikes().stream()
                .filter(like -> like.getStatus().equals(Status.ACTIVE))
                .count();
    }
}
