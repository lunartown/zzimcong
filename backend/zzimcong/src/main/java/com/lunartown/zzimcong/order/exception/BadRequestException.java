package com.lunartown.zzimcong.order.exception;

public abstract class BadRequestException extends BaseException {
    protected BadRequestException(String message, String errorCode) {
        super(message, errorCode);
    }
}