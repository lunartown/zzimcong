package com.zzimcong.order.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // BadRequestException (400)
    INVALID_INPUT("잘못된 입력입니다."),
    INVALID_ORDER_STATUS("유효하지 않은 주문 상태입니다."),

    // UnauthorizedException (401)

    // ForbiddenException (403)
    ACCESS_DENIED("접근이 거부되었습니다."),

    // NotFoundException (404)
    RESOURCE_NOT_FOUND("요청한 리소스를 찾을 수 없습니다."),
    ORDER_NOT_FOUND("주문을 찾을 수 없습니다."),

    // MethodNotAllowedException (405)
    METHOD_NOT_ALLOWED("허용되지 않은 HTTP 메서드입니다."),

    // ConflictException (409)
    INSUFFICIENT_STOCK("상품의 재고가 부족합니다."),
    CANNOT_CANCEL_ORDER("주문을 취소할 수 없습니다."),

    // InternalServerErrorException (500)
    RESOURCE_LOAD_FAILED("내부 리소스 로딩에 실패했습니다."),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}