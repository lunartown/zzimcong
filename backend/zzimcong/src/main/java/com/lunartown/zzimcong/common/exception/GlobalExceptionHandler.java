package com.lunartown.zzimcong.common.exception;

import com.lunartown.zzimcong.api.response.ErrorResponse;
import com.lunartown.zzimcong.user.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ExceptionHandler(MethodNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(MethodNotAllowedException ex, HttpServletRequest request) {
        return createErrorResponse(ex, HttpStatus.METHOD_NOT_ALLOWED, request.getRequestURI());
    }

    // ConflictException (409)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex, HttpServletRequest request) {
        return createErrorResponse(ex, HttpStatus.CONFLICT, request.getRequestURI());
    }

    // InternalServerErrorException (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllException(ConflictException ex, HttpServletRequest request) {
        return createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI());
    }

    // Create ErrorResponse
    private ResponseEntity<ErrorResponse> createErrorResponse(BaseException ex, HttpStatus status, String path) {
        logException(ex, status, path);
        ErrorResponse errorResponse = ErrorResponse.of(status, ex, path);
        return new ResponseEntity<>(errorResponse, status);
    }

    // Log Exception
    private void logException(Exception ex, HttpStatus status, String path) {
        log.error(
                "Exception occurred:: Error Code: {}, HTTP Status: {}, Message: {}, Path: {}",
                ex instanceof BaseException ? ((BaseException) ex).getErrorCode() : "UNKNOWN",
                status,
                ex.getMessage(),
                path,
                ex
        );
    }
}
