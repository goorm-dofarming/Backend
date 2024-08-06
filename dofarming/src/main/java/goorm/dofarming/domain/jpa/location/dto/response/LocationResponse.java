package goorm.dofarming.domain.jpa.location.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import goorm.dofarming.domain.jpa.image.repository.ImageRepository;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.infra.tourapi.domain.*;
import jakarta.persistence.DiscriminatorValue;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

public record LocationResponse(
        Long locationId,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String averageScore,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer totalReview,
        String title,
        String addr,
        String tel,
        String image,
        Double mapX,
        Double mapY,
        int dataType,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean liked,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean isReviewed,
        int countLikes
) {
    public static LocationResponse user(boolean liked, String reviewImageUrl, Location location) {
        return new LocationResponse(
                location.getLocationId(),
                null,
                null,
                location.getTitle(),
                location.getAddr(),
                location.getTel(),
                getImage(reviewImageUrl, location),
                location.getMapX(),
                location.getMapY(),
                Integer.parseInt(location.getTheme()),
                liked,
                null,
                location.getLikeCount()
        );
    }


    public static LocationResponse guest(String reviewImageUrl, Location location) {
        return new LocationResponse(
                location.getLocationId(),
                null,
                null,
                location.getTitle(),
                location.getAddr(),
                location.getTel(),
                getImage(reviewImageUrl, location),
                location.getMapX(),
                location.getMapY(),
                Integer.parseInt(location.getTheme()),
                null,
                null,
                location.getLikeCount()
        );
    }

    public static LocationResponse review(boolean liked, boolean isReviewed, String reviewImageUrl, Location location) {
        return new LocationResponse(
                location.getLocationId(),
                location.avgScore(),
                location.getReviewCount(),
                location.getTitle(),
                location.getAddr(),
                location.getTel(),
                getImage(reviewImageUrl, location),
                location.getMapX(),
                location.getMapY(),
                Integer.parseInt(location.getTheme()),
                liked,
                isReviewed,
                location.getLikeCount()
        );
    }
    private static String getImage(String reviewImageUrl, Location location) {
        return (location.getImage() != null && !location.getImage().isEmpty()) ?
                location.getImage() : reviewImageUrl;
    }
}
