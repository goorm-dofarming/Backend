package goorm.dofarming.domain.jpa.log.dto.response;

import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public record LogResponse(
        Long logId,
        String theme,
        String address,
        Double latitude,
        Double longitude,
        LocalDateTime createdAt
) {
    public static LogResponse of(Log log) {
        return new LogResponse(
                log.getLogId(),
                log.getTheme(),
                log.getAddress(),
                log.getLatitude(),
                log.getLongitude(),
                log.getCreatedAt()
        );
    }
}
