package com.gongbo.common.back.aspect;

import com.gongbo.common.back.BackHandler;
import com.gongbo.common.back.annotations.WithBack;
import com.gongbo.common.back.core.BackService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Slf4j
@ConditionalOnBean(BackService.class)
@Component
public class BackAdvise {
    @Pointcut("@annotation(com.gongbo.common.back.annotations.WithBack)")
    public void withBack() {
    }

    @Around("withBack()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!BackHandler.isEnableBack()) {
            return joinPoint.proceed();
        }

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        WithBack withBack = method.getAnnotation(WithBack.class);

        BackHandler.start(withBack.sync() ? BackHandler.getDefaultDelayMs() : 0);
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            BackHandler.rollbackCurrent();
            throw e;
        } finally {
            BackHandler.stop();
        }
    }
}