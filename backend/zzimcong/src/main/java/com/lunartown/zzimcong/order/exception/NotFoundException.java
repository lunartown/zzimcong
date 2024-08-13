package com.lunartown.zzimcong.order.exception;

public abstract class NotFoundException extends BaseException {
    protected NotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }
}