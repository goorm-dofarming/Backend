package goorm.dofarming.global.common.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        int statusCode,

        HttpStatus httpStatus,

        String message
) {
}
