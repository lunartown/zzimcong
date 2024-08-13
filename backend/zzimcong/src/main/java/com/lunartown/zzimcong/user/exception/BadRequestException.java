package com.lunartown.zzimcong.user.exception;

public class BadRequestException extends BaseException {
    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}