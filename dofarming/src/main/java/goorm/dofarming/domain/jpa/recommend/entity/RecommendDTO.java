package goorm.dofarming.domain.jpa.recommend.entity;

import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;
import goorm.dofarming.domain.jpa.log.dto.response.LogResponse;
import goorm.dofarming.domain.jpa.log.entity.Log;

import java.util.List;

public record RecommendDTO(
        LogResponse logResponse,
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
