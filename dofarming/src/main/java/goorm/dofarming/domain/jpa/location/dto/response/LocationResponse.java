package goorm.dofarming.domain.jpa.location.dto.response;

import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.infra.tourapi.domain.*;
import jakarta.persistence.DiscriminatorValue;

import java.time.LocalDateTime;

public record LocationResponse(
        Long locationId,
        String title,
        String addr,
        String tel,
        String image,
        Double mapX,
        Double mapY,
        int dataType,
        boolean liked,
        LocalDateTime createdAt
) {
    public static LocationResponse of(boolean liked, Location location) {
        return new LocationResponse(
                location.getLocationId(),
                location.getTitle(),
                location.getAddr(),
                location.getTel(),
                location.getTel(),
                location.getMapX(),
                location.getMapY(),
                Integer.parseInt(location.getTheme()),
                liked,
                location.getCreatedAt()
        );
    }
}
