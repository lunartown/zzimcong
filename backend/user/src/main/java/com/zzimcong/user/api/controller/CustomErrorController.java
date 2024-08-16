package com.zzimcong.user.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzimcong.user.api.response.ErrorResponse;
import com.zzimcong.user.common.exception.BaseException;
import com.zzimcong.user.common.exception.ErrorCode;
import com.zzimcong.user.common.exception.InternalServerErrorException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j(topic = "CUSTOM_ERROR_CONTROLLER")
@RestController
public class CustomErrorController implements ErrorController {
    private final ObjectMapper objectMapper;

    public CustomErrorController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @RequestMapping("/error")
    public void handleError(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer status = (Integer) request.getAttribute("status");
        Exception ex = (Exception) request.getAttribute("exception");
        String path = (String) request.getAttribute("path");

        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        log.info("Error occurred: status={}, path={}", status, path);
        BaseException baseException = ex instanceof BaseException ?
                (BaseException) ex : new InternalServerErrorException(ErrorCode.UNKNOWN_ERROR);

        ErrorResponse errorResponse = ErrorResponse.of(baseException, HttpStatus.valueOf(status), path);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}