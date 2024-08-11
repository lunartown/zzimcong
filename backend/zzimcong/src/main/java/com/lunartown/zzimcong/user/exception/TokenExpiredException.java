package com.lunartown.zzimcong.user.exception;

public class TokenExpiredException extends AuthException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
