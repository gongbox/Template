package com.gongbo.common.dynamictable.annotations;

import com.gongbo.common.dynamictable.config.DynamicTableConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 配置动态表
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DynamicTableConfig.class)
@Documented
public @interface EnableDynamicTable {

}
