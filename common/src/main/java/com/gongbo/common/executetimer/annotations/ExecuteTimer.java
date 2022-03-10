package com.gongbo.common.executetimer.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecuteTimer {

    /**
     * 名称，默认为方法名称
     */
    String name() default "";

    /**
     * 超时时间
     */
    int timeout() default 0;
}
