package com.zzimcong.order.common.exception;

public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public BaseException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }

    public BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}