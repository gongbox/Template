package com.gongbo.common.back.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WithBack {

    /**
     * 是否同步执行，即该方法的消息要消费后才能发送下一条消息
     */
    boolean sync() default false;
}
