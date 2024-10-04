package com.team5.pyeonjip.global.aop;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class UserLoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before("execution(* com.team5.pyeonjip.user..*(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        System.out.printf("[실행 메서드]: %s\n[매개변수]: %s\n\n", methodName, Arrays.toString(args));
    }

    @AfterReturning(value = "execution(* com.team5.pyeonjip.user..*(..))", returning = "result")
    public void logAfterMethod(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().toShortString();

        System.out.printf("[종료 메서드]: %s\n[반환값]: %s\n\n", methodName, result);
    }
}
