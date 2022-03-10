package com.gongbo.common.executetimer.aspect;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import com.gongbo.common.executetimer.annotations.ExecuteTimer;
import com.gongbo.common.executetimer.logger.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
public class ExecuteTimerAdvise {

    @Autowired
    private Logger logger;

    @Pointcut("@annotation(com.gongbo.common.executetimer.annotations.ExecuteTimer) || @within(com.gongbo.common.executetimer.annotations.ExecuteTimer)")
    public void executeTimer() {
    }

    @Around("executeTimer()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Class<?> declaringClass = method.getDeclaringClass();
        ExecuteTimer methodExecuteTimer = method.getAnnotation(ExecuteTimer.class);
        ExecuteTimer classExecuteTimer = declaringClass.getAnnotation(ExecuteTimer.class);

        int timeout = 0;
        if (methodExecuteTimer != null) {
            timeout = methodExecuteTimer.timeout();
        }
        if (timeout <= 0 && classExecuteTimer != null) {
            timeout = classExecuteTimer.timeout();
        }

        String className = Optional.ofNullable(classExecuteTimer)
                .map(ExecuteTimer::name)
                .filter(StrUtil::isNotEmpty)
                .orElseGet(declaringClass::getSimpleName);
        String methodName = Optional.ofNullable(methodExecuteTimer)
                .map(ExecuteTimer::name)
                .filter(StrUtil::isNotEmpty)
                .orElseGet(method::getName);

        String name = className + " -> " + methodName;

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();

        //执行方法
        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();

            //获取方法执行时间
            long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();

            if (timeout > 0 && timeout < lastTaskTimeMillis) {
                logger.warn(name, lastTaskTimeMillis);
            } else {
                logger.info(name, lastTaskTimeMillis);
            }
        }
    }

}
