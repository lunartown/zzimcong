//package com.zzimcong.order.aspect;
//
//import com.zzimcong.order.common.UserInfo;
//import com.zzimcong.order.common.exception.ErrorCode;
//import com.zzimcong.order.common.exception.ForbiddenException;
//import com.zzimcong.order.application.service.OrderService;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
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
//    @Before("execution(* com.zzimcong.order.api.controller.OrderController.*(.., com.zzimcong.order.common.UserInfo, Long, ..)) && args(userInfo, orderId, ..)")
//    public void validateUserAuthorization(JoinPoint joinPoint, UserInfo userInfo, Long orderId) {
//        if (!orderService.isOrderOwnedByUser(orderId, userInfo.getId())) {
//            throw new ForbiddenException(ErrorCode.ACCESS_DENIED, "주문 접근 권한이 없습니다.");
//        }
//    }
//}