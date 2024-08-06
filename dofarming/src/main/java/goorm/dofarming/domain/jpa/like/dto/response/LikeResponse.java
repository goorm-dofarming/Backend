package goorm.dofarming.domain.jpa.like.dto.response;

import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;

import java.time.LocalDateTime;

public record LikeResponse(
        Long likeId,
        LocalDateTime updatedAt,
        LocationResponse locationResponse
) {
    public static LikeResponse of(String reviewImageUrl, Like like) {
        return new LikeResponse(
                like.getLikeId(),
                like.getUpdatedAt(),
                LocationResponse.user(true, reviewImageUrl, like.getLocation())
        );
    }

}
