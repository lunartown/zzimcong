package com.lunartown.zzimcong.user.exception;

public class DuplicateEmailException extends AuthException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}