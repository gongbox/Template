package com.gongbo.common.dynamictable.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface DynamicTable {

    /**
     * 对应的表格式
     */
    String format();

}
