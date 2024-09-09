package com.zzimcong.user.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // BadRequestException (400)
    INVALID_INPUT("잘못된 입력입니다."),
    INVALID_EMAIL("잘못된 이메일 형식입니다."),
    INVALID_PASSWORD("잘못된 비밀번호 형식입니다."),
    INVALID_VERIFICATION_CODE("잘못된 인증 코드입니다."),
    EMAIL_VERIFICATION_FAILED("이메일 인증에 실패했습니다."),
    MISSING_REQUEST_BODY("요청 바디가 없습니다."),
    ADDRESS_NOT_FOUND("주소를 찾을 수 없습니다."),

    // UnauthorizedException (401)
    INVALID_CREDENTIALS("잘못된 인증 정보입니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("만료된 토큰입니다."),
    TOKEN_NOT_FOUND("토큰을 찾을 수 없습니다."),
    USER_NOT_AUTHENTICATED("사용자가 인증되지 않았습니다."),

    // ForbiddenException (403)
    ACCESS_DENIED("접근 권한이 없습니다."),

    // NotFoundException (404)
    RESOURCE_NOT_FOUND("요청한 리소스를 찾을 수 없습니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),

    // MethodNotAllowedException (405)
    METHOD_NOT_ALLOWED("허용되지 않은 HTTP 메서드입니다."),

    // ConflictException (409)
    DUPLICATE_EMAIL("이미 존재하는 이메일입니다."),

    // InternalServerErrorException (500)
    ENCRYPTION_FAILED("사용자 정보 암호화에 실패했습니다."),
    DECRYPTION_FAILED("사용자 정보 복호화에 실패했습니다."),
    RESOURCE_LOAD_FAILED("내부 리소스 로딩에 실패했습니다."),
    EMAIL_SEND_FAILED("이메일 전송에 실패했습니다."),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),
    UNKNOWN_ERROR("알 수 없는 오류가 발생했습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}