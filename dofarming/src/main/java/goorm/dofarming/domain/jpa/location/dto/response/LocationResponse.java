package goorm.dofarming.domain.jpa.location.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import goorm.dofarming.domain.jpa.location.entity.Location;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장소 응답 정보를 담는 DTO")
public record LocationResponse(
        @Schema(description = "장소 ID", example = "1")
        Long locationId,

        @Schema(description = "평균 점수", example = "4.5", nullable = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String averageScore,

        @Schema(description = "총 리뷰 수", example = "10", nullable = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer totalReview,

        @Schema(description = "장소 제목", example = "서울 타워")
        String title,

        @Schema(description = "주소", example = "서울시 용산구 남산공원길 105")
        String addr,

        @Schema(description = "전화번호", example = "02-3455-9277")
        String tel,

        @Schema(description = "이미지 URL", example = "http://example.com/image.jpg")
        String image,

        @Schema(description = "지도 X 좌표", example = "126.988000")
        Double mapX,

        @Schema(description = "지도 Y 좌표", example = "37.5514250")
        Double mapY,

        @Schema(description = "데이터 타입", example = "1")
        int dataType,

        @Schema(description = "좋아요 여부", example = "true", nullable = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean liked,

        @Schema(description = "리뷰 작성 여부", example = "true", nullable = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean isReviewed,

        @Schema(description = "좋아요 수", example = "100")
        int countLikes
) {
    public static LocationResponse user(boolean liked, Location location) {
        return new LocationResponse(
                location.getLocationId(),
                null,
                null,
                location.getTitle(),
                location.getAddr(),
                location.getTel(),
                location.getImage(),
                location.getMapX(),
                location.getMapY(),
                Integer.parseInt(location.getTheme()),
                liked,
                null,
                location.getLikeCount()
        );
    }

    public static LocationResponse guest(Location location) {
        return new LocationResponse(
                location.getLocationId(),
                null,
                null,
                location.getTitle(),
                location.getAddr(),
                location.getTel(),
                location.getImage(),
                location.getMapX(),
                location.getMapY(),
                Integer.parseInt(location.getTheme()),
                null,
                null,
                location.getLikeCount()
        );
    }

    public static LocationResponse review(boolean liked, boolean isReviewed, Location location) {
        return new LocationResponse(
                location.getLocationId(),
                location.avgScore(),
                location.getReviewCount(),
                location.getTitle(),
                location.getAddr(),
                location.getTel(),
                location.getImage(),
                location.getMapX(),
                location.getMapY(),
                Integer.parseInt(location.getTheme()),
                liked,
                isReviewed,
                location.getLikeCount()
        );
    }
}
