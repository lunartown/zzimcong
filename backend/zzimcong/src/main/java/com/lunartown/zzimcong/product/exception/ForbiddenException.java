package com.lunartown.zzimcong.product.exception;

public class ForbiddenException extends BaseException {
    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ForbiddenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}