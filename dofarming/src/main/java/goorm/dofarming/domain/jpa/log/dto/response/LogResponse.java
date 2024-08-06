package goorm.dofarming.domain.jpa.log.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import goorm.dofarming.domain.jpa.log.entity.Log;
import goorm.dofarming.global.common.entity.Status;
import goorm.dofarming.global.common.error.ErrorCode;
import goorm.dofarming.global.common.error.exception.CustomException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

@Schema(description = "로그 응답 정보를 담는 DTO")
public record LogResponse(
        @Schema(description = "로그 ID", example = "1", nullable = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long logId,

        @Schema(description = "테마", example = "ACTIVITY", nullable = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String theme,

        @Schema(description = "주소", example = "서울시 강남구")
        String address,

        @Schema(description = "위도", example = "37.123456")
        Double latitude,

        @Schema(description = "경도", example = "127.123456")
        Double longitude,

        @Schema(description = "생성 일자", example = "2024-08-06T12:55:56", nullable = true)
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
