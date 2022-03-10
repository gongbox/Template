package com.gongbo.test.custom.annotations;

import com.gongbo.test.custom.provider.DescartesMethodArgumentsProvider;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.annotation.*;

/**
 * 自定义参数提供注解，用于支持多个参数的方法（多个参数将以笛卡尔积的方式合并）
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(
        status = Status.EXPERIMENTAL,
        since = "5.0"
)
@ArgumentsSource(DescartesMethodArgumentsProvider.class)
public @interface DescartesMethodSource {
    MethodSource[] value();
}
