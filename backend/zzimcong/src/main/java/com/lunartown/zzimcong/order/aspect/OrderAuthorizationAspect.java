package com.lunartown.zzimcong.order.aspect;

import com.lunartown.zzimcong.common.UserInfo;
import com.lunartown.zzimcong.order.exception.UnauthorizedAccessException;
import com.lunartown.zzimcong.order.service.OrderService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OrderAuthorizationAspect {

    private final OrderService orderService;

    public OrderAuthorizationAspect(OrderService orderService) {
        this.orderService = orderService;
    }

    @Before("execution(* com.lunartown.zzimcong.order.controller.OrderController.*(.., com.lunartown.zzimcong.common.UserInfo, Long, ..)) && args(userInfo, orderId, ..)")
    public void validateUserAuthorization(JoinPoint joinPoint, UserInfo userInfo, Long orderId) {
        if (!orderService.isOrderOwnedByUser(orderId, userInfo.getId())) {
            throw new UnauthorizedAccessException("해당 주문에 대한 접근 권한이 없습니다.");
        }
    }
}