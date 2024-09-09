package com.zzimcong.order.common.exception;

public class InventoryException extends BaseException {
    public InventoryException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
