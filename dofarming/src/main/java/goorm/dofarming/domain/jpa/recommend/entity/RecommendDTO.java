package goorm.dofarming.domain.jpa.recommend.entity;

import goorm.dofarming.domain.jpa.location.dto.response.LocationResponse;

import java.util.List;

public record RecommendDTO(
        Long logId,
        String address,
        List<LocationResponse> recommendations
) {
    public static RecommendDTO of(Long logId, String address, List<LocationResponse> recommendations) {
        return new RecommendDTO(
                logId,
                address,
                recommendations
        );
    }
}
