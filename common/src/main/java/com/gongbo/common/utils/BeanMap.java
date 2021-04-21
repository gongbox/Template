package com.gongbo.common.utils;

import org.springframework.beans.BeanUtils;

import java.util.function.Supplier;

/**
 * bean转换工具类
 */
public interface BeanMap {

    /**
     * 转换为其他类型对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    default <T> T mapTo(Class<T> clazz) {
        return mapTo(() -> BeanUtils.instantiateClass(clazz));
    }

    /**
     * 转换为其他类型对象
     *
     * @param supplier
     * @param <T>
     * @return
     */
    default <T> T mapTo(Supplier<T> supplier) {
        T data = supplier.get();
        BeanUtils.copyProperties(this, data);
        return data;
    }


}
