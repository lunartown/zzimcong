package com.lunartown.zzimcong.user.exception;

public class UnauthorizedException extends AuthException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
