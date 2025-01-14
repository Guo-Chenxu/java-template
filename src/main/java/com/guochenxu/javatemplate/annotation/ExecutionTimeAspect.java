package com.guochenxu.javatemplate.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ExecutionTimeAspect {

    @Pointcut("@within(com.guochenxu.javatemplate.annotation.ExecutionTime) || @annotation(com.guochenxu.javatemplate.annotation.ExecutionTime)")
    public void executionTimeMethods() {
    }

    @Around("executionTimeMethods()")
    public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long executionTimeInMilliseconds = System.currentTimeMillis() - start;
            log.info("Method: {}, cost: {} ms.",
                    joinPoint.getSignature().getName(), executionTimeInMilliseconds);
        }
    }
}
