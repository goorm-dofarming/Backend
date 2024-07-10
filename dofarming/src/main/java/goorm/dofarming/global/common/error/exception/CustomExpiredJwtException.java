package goorm.dofarming.global.common.error.exception;

import io.jsonwebtoken.ClaimJwtException;

public class CustomExpiredJwtException extends RuntimeException {
    public CustomExpiredJwtException(String message) {
        super(message);
    }
}
