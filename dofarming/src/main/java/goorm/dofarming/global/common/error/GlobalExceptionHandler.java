package goorm.dofarming.global.common.error;

import goorm.dofarming.global.common.error.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ErrorResponse.of(e.getErrorCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(ErrorCode.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(CustomInvalidJwtException.class)
    public ResponseEntity<ErrorResponse> invalidJwtExceptionHandler(CustomInvalidJwtException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(ErrorCode.FORBIDDEN, e.getMessage()));
    }

    @ExceptionHandler(CustomMalformedJwtException.class)
    public ResponseEntity<ErrorResponse> malformedJwtExceptionHandler(CustomMalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ErrorCode.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(CustomExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> expiredJwtExceptionHandler(CustomExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(ErrorCode.UNAUTHORIZED, e.getMessage()));
    }

    @ExceptionHandler(CustomUnsupportedJwtException.class)
    public ResponseEntity<ErrorResponse> unsupportedJwtExceptionHandler(CustomUnsupportedJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ErrorCode.BAD_REQUEST, e.getMessage()));
    }
}
