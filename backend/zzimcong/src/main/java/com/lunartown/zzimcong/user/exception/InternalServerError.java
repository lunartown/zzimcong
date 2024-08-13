package com.lunartown.zzimcong.user.exception;

public class InternalServerError extends BaseException {
    public InternalServerError(ErrorCode errorCode) {
        super(errorCode);
    }

    public InternalServerError(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
