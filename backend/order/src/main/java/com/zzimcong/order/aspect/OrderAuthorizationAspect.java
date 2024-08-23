//package com.zzimcong.order.aspect;
//
//import com.zzimcong.order.application.service.OrderService;
//import com.zzimcong.order.common.exception.ErrorCode;
//import com.zzimcong.order.common.exception.ForbiddenException;
//import jakarta.servlet.http.HttpServletRequest;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@Aspect
//@Component
//public class OrderAuthorizationAspect {
//
//    private final OrderService orderService;
//
//    public OrderAuthorizationAspect(OrderService orderService) {
//        this.orderService = orderService;
//    }
//
//    @Before("execution(* com.zzimcong.order.api.controller.OrderFulfillmentController.*(.., Long, ..))")
//    public void validateUserAuthorization(JoinPoint joinPoint) {
//        // HTTP 요청에서 사용자 ID 추출
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        String userId = request.getHeader("X-Auth-User-ID");
//
//        // orderId 파라미터 찾기
//        Long orderId = null;
//        Object[] args = joinPoint.getArgs();
//        for (Object arg : args) {
//            if (arg instanceof Long) {
//                orderId = (Long) arg;
//                break;
//            }
//        }
//
//        if (orderId == null) {
//            throw new IllegalArgumentException("주문 ID를 찾을 수 없습니다.");
//        }
//
//        if (userId == null) {
//            throw new ForbiddenException(ErrorCode.ACCESS_DENIED, "사용자 인증 정보가 없습니다.");
//        }
//
//        if (!orderService.isOrderOwnedByUser(orderId, Long.parseLong(userId))) {
//            throw new ForbiddenException(ErrorCode.ACCESS_DENIED, "주문 접근 권한이 없습니다.");
//        }
//    }
//}