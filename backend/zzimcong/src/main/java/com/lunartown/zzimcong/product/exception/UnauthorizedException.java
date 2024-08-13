package com.lunartown.zzimcong.product.exception;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}