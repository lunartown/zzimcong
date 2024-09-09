package com.zzimcong.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Response<T>(
        Integer code,
        String message,
        T data
) {
    public Response {
        if (code == null) {
            code = HttpStatus.OK.value();
        }
        if (message == null) {
            message = HttpStatus.OK.getReasonPhrase();
        }
    }
}