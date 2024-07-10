package goorm.dofarming.global.common.error.exception;

public class CustomMalformedJwtException extends RuntimeException {
    public CustomMalformedJwtException(String message) {
        super(message);
    }
}
