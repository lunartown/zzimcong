package com.lunartown.zzimcong.common.exception;

import com.lunartown.zzimcong.user.exception.CryptoException;
import com.lunartown.zzimcong.user.exception.UserNotFoundException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request) {
        String requestDescription = request.getDescription(false);

        log.error("EntityNotFoundException occurred: {} for request: {}", ex.getMessage(), requestDescription);

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                requestDescription);

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        String requestDescription = request.getDescription(false);

        log.error("EntityNotFoundException occurred: {} for request: {}", ex.getMessage(), requestDescription);

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                ex.getMessage(),
                requestDescription);

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        String requestDescription = request.getDescription(false);
        log.error("Validation error occurred for request: {}. Errors: {}", requestDescription, errors);

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "양식이 올바르지 않습니다.",
                requestDescription,
                errors
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CryptoException.class)
    public ResponseEntity<?> handleCryptoException(CryptoException ex, WebRequest request) {
        String RequestDescription = request.getDescription(false);
        log.error("CryptoException occurred: {} for request: {}", ex.getMessage(), RequestDescription);

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "암호화/복호화에 실패하였습니다.",
                RequestDescription
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex, WebRequest request) {
        String requestDescription = request.getDescription(false);
        log.error("예외 발생: type={}, message={}, request={}",
                ex.getClass().getSimpleName(), ex.getMessage(), requestDescription, ex);

        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                "에러가 발생했습니다.",
                requestDescription
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@Getter
@Setter
class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;
    private Map<String, String> validationErrors;

    //일반 Exception 처리
    public ErrorDetails(LocalDateTime timestamp, String message, String details) {
        this(timestamp, message, details, null);
    }

    //Validation Exception 처리
    public ErrorDetails(LocalDateTime timestamp, String message, String details, Map<String, String> validationErrors) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.validationErrors = validationErrors;
    }
}
