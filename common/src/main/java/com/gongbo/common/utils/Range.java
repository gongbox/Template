package com.gongbo.common.utils;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Range<T extends Comparable<? super T>> {
    private final T start;
    private final T end;
    //左区间包含
    private final boolean leftClose;
    //右区间包含
    private final boolean rightClose;

    /**
     * 创建
     */
    public static <T extends Comparable<? super T>> Range<T> of(T a, T b) {
        return of(a, b, true, true);
    }

    /**
     * 创建闭区间
     */
    public static <T extends Comparable<? super T>> Range<T> close(T a, T b) {
        return of(a, b, true, true);
    }

    /**
     * 创建开区间
     */
    public static <T extends Comparable<? super T>> Range<T> open(T a, T b) {
        return of(a, b, false, false);
    }

    /**
     * 创建左开区间
     */
    public static <T extends Comparable<? super T>> Range<T> leftOpen(T a, T b) {
        return of(a, b, false, true);
    }

    /**
     * 创建右开区间
     */
    public static <T extends Comparable<? super T>> Range<T> rightOpen(T a, T b) {
        return of(a, b, true, false);
    }

    /**
     * 创建
     */
    public static <T extends Comparable<? super T>> Range<T> of(T a, T b, boolean leftClose, boolean rightClose) {
        return new Range<>(a, b, leftClose, rightClose);
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
     * 计算长度
     * @param stepTo
     * @return
     */
    public int size(BiFunction<T, Integer, T> stepTo) {
        return toList(stepTo).size();
    }

    /**
     * 判断是否在范围内
     */
    public boolean contains(T value) {
        if (value == null) {
            return false;
        }

        if (leftClose && rightClose) {
            return start.compareTo(value) <= 0 && end.compareTo(value) >= 0;
        } else if (leftClose) {
            return start.compareTo(value) <= 0 && end.compareTo(value) > 0;
        } else if (rightClose) {
            return start.compareTo(value) < 0 && end.compareTo(value) >= 0;
        } else {
            return start.compareTo(value) < 0 && end.compareTo(value) > 0;
        }
    }

    /**
     * 限制值范围
     */
    public T limit(T value) {
        if (value == null) {
            return null;
        }
        if ((!leftClose && start.compareTo(value) == 0) || (!rightClose && end.compareTo(value) == 0)) {
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
     * @param mapper 转换方法
     * @param <R>
     * @return
     */
    public <R extends Comparable<? super R>> Range<R> map(Function<T, R> mapper) {
        return of(mapper.apply(start), mapper.apply(end), this.leftClose, this.rightClose);
    }

    /**
     * 排除某一区间
     *
     * @param exclude 要排除的区间
     * @return
     */
    public List<Range<T>> exclude(Range<T> exclude) {
        //若区间没有交集，则直接返回当前对象
        if (start.compareTo(exclude.end) >= 0 || end.compareTo(exclude.start) <= 0) {
            return Collections.singletonList(this);
        }

        //如果区间相等，则返回空
        if (this.equals(exclude)) {
            return Collections.emptyList();
        }

        //如果要排除的区间包含当前区间，则返回空
        if (exclude.start.compareTo(start) <= 0 && exclude.end.compareTo(end) >= 0) {
            return Collections.emptyList();
        }

        //如果要排除的区间有一部分在当前区间内
        if (exclude.getStart().compareTo(start) <= 0 && exclude.end.compareTo(end) <= 0) {
            return Collections.singletonList(Range.of(exclude.end, end));
        } else if (exclude.start.compareTo(start) >= 0 && exclude.end.compareTo(end) >= 0) {
            return Collections.singletonList(Range.of(start, exclude.start));
        } else if (exclude.start.compareTo(start) >= 0 && exclude.end.compareTo(end) <= 0) {
            return Arrays.asList(Range.of(start, exclude.start), Range.of(exclude.end, end));
        }
        throw new IllegalArgumentException();
    }

    /**
     * 交集
     *
     * @param other
     * @return
     */
    public Range<T> intersection(Range<T> other) {
        if (start.compareTo(other.end) >= 0 || end.compareTo(other.start) <= 0) {
            return null;
        }
        return of(Utils.max(start, other.start), Utils.min(end, other.end));
    }

    /**
     * 并集
     */
    public List<Range<T>> union(Range<T> other) {
        if (start.compareTo(other.end) >= 0 || end.compareTo(other.start) <= 0) {
            return Arrays.asList(this, other);
        }
        return Collections.singletonList(of(Utils.min(start, other.start), Utils.max(end, other.end)));
    }

}
