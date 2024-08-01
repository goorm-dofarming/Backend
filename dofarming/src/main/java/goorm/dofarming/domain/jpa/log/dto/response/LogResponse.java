package goorm.dofarming.domain.jpa.log.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

public record LogResponse(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long logId,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String theme,
        String address,
        Double latitude,
        Double longitude,

        @JsonInclude(JsonInclude.Include.NON_NULL)
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

    public static LogResponse guest(String address, Double mapX, Double mapY) {
        return new LogResponse(
                null,
                null,
                address,
                mapX,
                mapY,
                null
        );
    }
}
