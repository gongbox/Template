package com.gongbo.common.utils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Range<T extends Comparable<? super T>> {
    private final T start;
    private final T end;

    /**
     * 创建
     */
    public static <T extends Comparable<? super T>> Range<T> of(T a, T b) {
        return new Range<>(a, b);
    }

    /**
     * 检查值是否合理，即end是否大于start
     */
    public boolean isValid() {
        return start != null && end != null && end.compareTo(start) >= 0;
    }

    /**
     * 转换为List
     */
    public RangeList<T> toList(BiFunction<T, Integer, T> stepTo) {
        return RangeList.of(this, stepTo);
    }

    /**
     * 判断是否在范围内
     */
    public boolean contains(T value) {
        if (value == null) {
            return false;
        }
        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

    /**
     * 限制值范围
     */
    public T limit(T value) {
        if (value == null) {
            return null;
        }
        if (value.compareTo(start) < 0) {
            return start;
        }
        if (value.compareTo(end) > 0) {
            return end;
        }
        return value;
    }

    /**
     * 转换为其他Range
     *
     * @param mapper    转换方法
     * @param <R>
     * @return
     */
    public <R extends Comparable<? super R>> Range<R> convert(Function<T, R> mapper) {
        return of(mapper.apply(start), mapper.apply(end));
    }
}
