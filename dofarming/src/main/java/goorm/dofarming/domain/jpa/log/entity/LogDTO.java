package goorm.dofarming.domain.jpa.log.entity;

import java.util.List;

public record LogDTO(
        String theme,
        String address,
        Double longitude,
        Double latitude,
        List<Object> logData
) {
}
