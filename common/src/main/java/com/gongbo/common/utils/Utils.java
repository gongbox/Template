package com.gongbo.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gongbo.common.utils.Range;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.util.StringUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
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

    /**
     * 判断是否位于区间内
     */
    public static <T extends Comparable<? super T>> boolean isBetween(T value, Range<T> range) {
        return range.contains(value);
    }

    /**
     * 判断是否位于区间内
     */
    public static <T extends Comparable<? super T>> boolean isBetween(T value, T start, T end) {
        return isBetween(value, Range.of(start, end));
    }

    /**
     * 判断是否包含
     */
    public static <T> boolean in(T target, T... values) {
        for (T value : values) {
            if (Objects.equals(target, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否不包含
     */
    public static <T> boolean notIn(T target, T... values) {
        for (T value : values) {
            if (Objects.equals(target, value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取最大值
     */
    public static <T extends Comparable<? super T>> T min(T... values) {
        T min = null;
        for (T value : values) {
            if (value == null) {
                continue;
            }
            if (min == null) {
                min = value;
            }
            if (value.compareTo(min) < 0) {
                min = value;
            }
        }
        return min;
    }

    /**
     * 获取最大值
     */
    public static <T extends Comparable<? super T>> T max(T... values) {
        T max = null;
        for (T value : values) {
            if (value == null) {
                continue;
            }
            if (max == null) {
                max = value;
            }
            if (value.compareTo(max) > 0) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 限制值范围
     */
    public static <T extends Comparable<? super T>> T limit(T value, T start, T end) {
        return limit(value, Range.of(start, end));
    }

    /**
     * 限制值范围
     */
    public static <T extends Comparable<? super T>> T limit(T value, Range<T> range) {
        return range.limit(value);
    }
}
