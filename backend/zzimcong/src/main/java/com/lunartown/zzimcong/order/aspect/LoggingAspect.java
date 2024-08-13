package com.lunartown.zzimcong.order.aspect;

import com.lunartown.zzimcong.common.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.lunartown.zzimcong.order.controller.*.*(.., com.lunartown.zzimcong.common.UserInfo, ..))")
    public Object logAroundMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        UserInfo userInfo = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof UserInfo) {
                userInfo = (UserInfo) arg;
                break;
            }
        }

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("사용자 ID: {}, 메서드 실행: {}.{}",
                userInfo != null ? userInfo.getId() : "Unknown",
                className, methodName);

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        log.info("사용자 ID: {}, 메서드 종료: {}.{}, 실행 시간: {}ms",
                userInfo != null ? userInfo.getId() : "Unknown",
                className, methodName, executionTime);

        return result;
    }
}