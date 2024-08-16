package com.zzimcong.product.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // BadRequestException (400)
    INVALID_INPUT("잘못된 입력입니다."),
    INVALID_QUANTITY("상품 수량은 1 이상이어야 합니다."),

    // UnauthorizedException (401)

    // NotFoundException (404)
    RESOURCE_NOT_FOUND("요청한 리소스를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다."),
    CART_PRODUCT_NOT_FOUND("장바구니 상품을 찾을 수 없습니다."),

    // MethodNotAllowedException (405)
    METHOD_NOT_ALLOWED("허용되지 않은 HTTP 메서드입니다."),

    // ConflictException (409)
    INSUFFICIENT_STOCK("상품의 재고가 부족합니다."),

    // InternalServerErrorException (500)
    RESOURCE_LOAD_FAILED("내부 리소스 로딩에 실패했습니다."),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}