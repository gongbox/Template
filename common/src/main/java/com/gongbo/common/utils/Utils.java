package com.gongbo.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    /**
     * 获取第一个非空的对象，如果所有对象都为空，则返回空
     */
    public static <T> T firstNotNull(T... values) {
        for (T value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取第一个非空的对象，如果所有对象都为空，则返回空
     */
    @SafeVarargs
    public static <T> T firstNotNull(Supplier<T>... values) {
        for (Supplier<T> supplier : values) {
            T value = supplier.get();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取第一个非空的字符串，如果所有对象都为空，则返回null
     */
    public static String firstNotEmpty(String... values) {
        for (String value : values) {
            if (StrUtil.isNotEmpty(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取第一个非空的字符串，如果所有对象都为空，则返回null
     */
    @SafeVarargs
    public static String firstNotEmpty(Supplier<String>... values) {
        for (Supplier<String> supplier : values) {
            String value = supplier.get();
            if (StrUtil.isNotEmpty(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * 获取第一个非空的字符串，如果所有对象都为空，则返回空集合
     */
    public static <T> Collection<T> firstNotEmpty(Collection<T>... values) {
        for (Collection<T> value : values) {
            if (CollUtil.isNotEmpty(value)) {
                return value;
            }
        }
        return Collections.emptyList();
    }
}
