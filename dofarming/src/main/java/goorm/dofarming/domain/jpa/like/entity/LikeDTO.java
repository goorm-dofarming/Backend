package goorm.dofarming.domain.jpa.like.entity;

import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.global.common.entity.Status;
public record LikeDTO(
        Long locationId,
        int dataType,
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
                Integer.parseInt(location.getTheme()),
                location.getTitle(),
                location.getAddr(),
                location.getTel(),
                location.getImage(),
                location.getMapX(),
                location.getMapY(),
                getLikeCount(location)
        );
    }
    public static int getLikeCount(Location location) {
        return (int) location.getLikes().stream()
                .filter(like -> like.getStatus().equals(Status.ACTIVE))
                .count();
    }
}
