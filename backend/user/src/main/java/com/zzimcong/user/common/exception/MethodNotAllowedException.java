package com.zzimcong.user.common.exception;

public class MethodNotAllowedException extends BaseException {
    public MethodNotAllowedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MethodNotAllowedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}