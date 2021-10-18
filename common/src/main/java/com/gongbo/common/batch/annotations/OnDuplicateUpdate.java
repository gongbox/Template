package com.gongbo.common.batch.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 批量插入数据冲突时需要更新的字段,可以声明在类上，字段优先级大于类优先级
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnDuplicateUpdate {
    /**
     * 是否更新该字段
     */
    boolean value() default true;
}
