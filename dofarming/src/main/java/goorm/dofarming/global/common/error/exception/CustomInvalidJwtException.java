package goorm.dofarming.global.common.error.exception;

public class CustomInvalidJwtException extends RuntimeException {
    public CustomInvalidJwtException(String message) {
        super(message);
    }
}
