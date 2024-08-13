package com.lunartown.zzimcong.order.exception;

public abstract class UnauthorizedException extends BaseException {
    protected UnauthorizedException(String message, String errorCode) {
        super(message, errorCode);
    }
}