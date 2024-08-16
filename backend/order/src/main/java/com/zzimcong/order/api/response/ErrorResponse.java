package com.zzimcong.order.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zzimcong.order.common.exception.BaseException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private int status;
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;
    private String path;

    public static ErrorResponse of(HttpStatus status, BaseException ex, String path) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(status.value());
        response.setMessage(ex.getMessage());
        response.setErrorCode(ex.getErrorCode().name());
        response.setTimestamp(LocalDateTime.now());
        response.setPath(path);
        return response;
    }
}