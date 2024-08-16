package com.zzimcong.product.common.exception;

public class InternalServerError extends BaseException {
    public InternalServerError(ErrorCode errorCode) {
        super(errorCode);
    }

    public InternalServerError(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
