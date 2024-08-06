package goorm.dofarming.domain.jpa.recommend.entity;

import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.log.dto.response.LogResponse;
import goorm.dofarming.domain.jpa.log.entity.Log;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "추천 응답 정보를 담는 DTO")
public record RecommendDTO(
        @Schema(description = "로그 정보")
        LogResponse logResponse,

        @Schema(description = "추천 장소 목록")
        List<LocationResponse> recommendations
) {
    public static RecommendDTO of(Log log, List<LocationResponse> recommendations) {
        return new RecommendDTO(
                LogResponse.of(log),
                recommendations
        );
    }

    public static RecommendDTO guest(String address, Double mapX, Double mapY, List<LocationResponse> recommendations) {
        return new RecommendDTO(
                LogResponse.guest(address, mapX, mapY),
                recommendations
        );
    }
}
