package goorm.dofarming.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "Bad request"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "Resource not found"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "Unauthorized"),

    PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, 401, "Password not match"),
    DUPLICATE_OBJECT(HttpStatus.CONFLICT, 409, "Duplicate Object");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
