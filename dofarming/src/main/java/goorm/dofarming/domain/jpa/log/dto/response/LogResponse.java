package goorm.dofarming.domain.jpa.log.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
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
                changeTheme(log.getTheme()),
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
                mapY,
                mapX,
                null
        );
    }

    public static String changeTheme(String theme) {
        switch (theme) {
            case "0" : return "Random";
            case "1" : return "Ocean";
            case "2" : return "Mountain";
            case "3" : return "Activity";
            case "4" : return "Tour";
            case "5" : return "Restaurant";
            case "6" : return "Cafe";
            default: throw new CustomException(ErrorCode.BAD_REQUEST, "존재하지 않는 타입입니다.");
        }
    }
}
