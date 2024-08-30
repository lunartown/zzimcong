package com.zzimcong.order.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "API-LOG")
@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.zzimcong.order.api.controller.*.*(..))")
    public Object logAroundMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String userId = request.getHeader("X-Auth-User-ID");

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // 메서드 실행 전 로깅
        log.info("------------------------------------------------------");
        log.info("Method execution start: {}.{}, User ID: {}",
                className, methodName, userId != null ? userId : "Unknown");

        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        // 메서드 실행 후 로깅
        log.info("Method execution end: {}.{}, User ID: {}, Execution time: {}ms",
                className, methodName, userId != null ? userId : "Unknown", executionTime);
        log.info("------------------------------------------------------");

        return result;
    }
}