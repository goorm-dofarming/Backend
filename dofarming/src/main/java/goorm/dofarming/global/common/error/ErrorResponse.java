package goorm.dofarming.global.common.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Schema(description = "에러 응답 정보를 담는 DTO")
public record ErrorResponse(

        @Schema(description = "상태 코드", example = "404")
        int statusCode,

        @Schema(description = "HTTP 상태", example = "NOT_FOUND")
        HttpStatus httpStatus,

        @Schema(description = "에러 메시지", example = "User not found")
        String message
) {

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getHttpStatus(),
                message
        );
    }
}
