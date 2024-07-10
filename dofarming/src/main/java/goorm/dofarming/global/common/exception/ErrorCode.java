package goorm.dofarming.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 404),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401),
    CONFLICT(HttpStatus.CONFLICT, 409),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500),

    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, 401),
    DUPLICATE_OBJECT(HttpStatus.CONFLICT, 409);

    private final HttpStatus httpStatus;
    private final int code;
}
