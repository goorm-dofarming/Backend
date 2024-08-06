package goorm.dofarming.domain.jpa.like.dto.response;

import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "좋아요 응답 정보를 담는 DTO")
public record LikeResponse(
        @Schema(description = "좋아요 ID", example = "1")
        Long likeId,
        @Schema(description = "갱신 일자", example = "2024-08-06T11:40:33")
        LocalDateTime updatedAt,
        @Schema(description = "장소 응답 정보")
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
