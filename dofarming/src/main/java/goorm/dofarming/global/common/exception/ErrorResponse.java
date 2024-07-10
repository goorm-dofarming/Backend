package goorm.dofarming.global.common.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        int statusCode,

        HttpStatus httpStatus,

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
