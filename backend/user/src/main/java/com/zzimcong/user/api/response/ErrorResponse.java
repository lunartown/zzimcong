package com.zzimcong.user.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zzimcong.user.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j(topic = "error")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int status,
        String message,
        String errorCode,
        LocalDateTime timestamp,
        String path
) {
    public ErrorResponse {
        Objects.requireNonNull(message, "메시지는 null일 수 없습니다.");
        Objects.requireNonNull(errorCode, "에러 코드는 null일 수 없습니다.");
        Objects.requireNonNull(path, "경로는 null일 수 없습니다.");
        timestamp = LocalDateTime.now();
    }

    public static ErrorResponse of(BaseException ex, HttpStatus status, String path) {
        Objects.requireNonNull(ex, "예외는 null일 수 없습니다.");
        Objects.requireNonNull(status, "HTTP 상태는 null일 수 없습니다.");

        log.error("Error occurred: status={}, message={}, errorCode={}, path={}",
                status.value(), ex.getMessage(), ex.getErrorCode().name(), path, ex);

        return new ErrorResponse(
                status.value(),
                ex.getMessage(),
                ex.getErrorCode().name(),
                LocalDateTime.now(),
                path
        );
    }
}