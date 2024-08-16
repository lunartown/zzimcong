package com.zzimcong.user.common.exception;

import com.zzimcong.user.api.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@ControllerAdvice
public class GlobalExceptionHandler {
    // BadRequestException (400)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(HttpServletRequest request) {
        BadRequestException ex = new BadRequestException(ErrorCode.MISSING_REQUEST_BODY);
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    // UnauthorizedException (401)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        return createErrorResponse(ex, HttpStatus.UNAUTHORIZED, request.getRequestURI());
    }

    // ForbiddenException (403)
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex, HttpServletRequest request) {
        return createErrorResponse(ex, HttpStatus.FORBIDDEN, request.getRequestURI());
    }

    // NotFoundException (404)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return createErrorResponse(ex, HttpStatus.NOT_FOUND, request.getRequestURI());
    }

    // MethodNotAllowedException (405)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(HttpServletRequest request) {
        MethodNotAllowedException ex = new MethodNotAllowedException(ErrorCode.METHOD_NOT_ALLOWED);
        return createErrorResponse(ex, HttpStatus.METHOD_NOT_ALLOWED, request.getRequestURI());
    }

    // ConflictException (409)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex, HttpServletRequest request) {
        return createErrorResponse(ex, HttpStatus.CONFLICT, request.getRequestURI());
    }

    // InternalServerErrorException (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(Exception ex, HttpServletRequest request) {
        return createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }

    // Create ErrorResponse
    private ResponseEntity<ErrorResponse> createErrorResponse(Exception ex, HttpStatus status, String path) {
        logException(ex, status, path);
        BaseException baseException = ex instanceof BaseException ?
                (BaseException) ex : new InternalServerErrorException(ErrorCode.INTERNAL_SERVER_ERROR);
        ErrorResponse errorResponse = ErrorResponse.of(baseException, status, path);
        return new ResponseEntity<>(errorResponse, status);
    }

    // Log Exception
    private void logException(Exception ex, HttpStatus status, String path) {
        // 스택 트레이스를 분석하여 예외 발생 위치 추출
        StackTraceElement[] stackTraceElements = ex.getStackTrace();
        String errorLocation = "Unknown";
        if (stackTraceElements.length > 0) {
            StackTraceElement element = stackTraceElements[0];
            errorLocation = String.format("%s.%s (Line: %d)",
                    element.getClassName(),
                    element.getMethodName(),
                    element.getLineNumber());
        }
        log.error(
                "Exception occurred:: {}, Path: {}",
                errorLocation,
                path
        );
        log.error("Error Code: {}, HTTP Status: {}, Message: {}",
                ex instanceof BaseException ? ((BaseException) ex).getErrorCode() : "UNKNOWN",
                status,
                ex.getMessage());
    }
}
