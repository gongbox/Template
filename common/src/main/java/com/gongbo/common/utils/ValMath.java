package com.gongbo.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.util.comparator.Comparators;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 包含val字段的对象val值计算
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValMath {

    /**
     * val字段求平均值（忽略null,如果所有值都为null，则结果也为null）
     *
     * @param obj 包含val字段的对象
     */
    public static @Nullable BigDecimal averageIgnoreNull(Object obj,
                                 int scale,
                                 RoundingMode roundingMode) {
        List<BigDecimal> values = ValUtils.values(Objects.requireNonNull(obj));
        return values.stream()
                .filter(Objects::nonNull)
                .collect(com.goldwind.pf.common.util.Streams.averagingBigDecimal(scale, roundingMode));
    }

    /**
     * val字段求平均值（null作为0处理）
     *
     * @param obj 包含val字段的对象
     */
    public static BigDecimal averageNullAsZero(Object obj,
                                               int scale,
                                               RoundingMode roundingMode) {
        List<BigDecimal> values = ValUtils.values(Objects.requireNonNull(obj));
        return values.stream()
                .map(com.goldwind.pf.common.util.Streams.nullAs(BigDecimal.ZERO))
                .collect(com.goldwind.pf.common.util.Streams.averagingBigDecimal(scale, roundingMode));
    }


    /**
     * 求和(如果所有值为null，则结果也为null)
     *
     * @param obj 包含val字段的对象
     */
    public static @Nullable BigDecimal sum(Object obj) {
        List<BigDecimal> values = ValUtils.values(Objects.requireNonNull(obj));
        return values.stream()
                .filter(Objects::nonNull)
                .collect(com.goldwind.pf.common.util.Streams.summingBigDecimal());
    }

    /**
     * 求val字段中的最大值(为空代表没有最大值，即所有值val为null)
     *
     * @param obj 包含val字段的对象
     */
    public static @Nullable BigDecimal maxValueIgnoreNull(Object obj) {
        List<BigDecimal> values = ValUtils.values(Objects.requireNonNull(obj));
        return values.stream()
                .filter(Objects::nonNull)
                .max(Comparators.comparable())
                .orElse(null);
    }

    /**
     * 求val字段中的最小值(为空代表没有最小值，即所有值val为null)
     *
     * @param obj 包含val字段的对象
     */
    public static @Nullable BigDecimal minValueIgnoreNull(Object obj) {
        List<BigDecimal> values = ValUtils.values(Objects.requireNonNull(obj));
        return values.stream()
                .filter(Objects::nonNull)
                .min(Comparators.comparable())
                .orElse(null);
    }

    /**
     * 求val字段中的最大值(为空代表没有最大值，即所有值val为null)
     *
     * @param obj 包含val字段的对象
     */
    public static @Nullable Map.Entry<String, BigDecimal> maxNameValueIgnoreNull(Object obj) {
        Map<String, BigDecimal> values = ValUtils.toValMap(Objects.requireNonNull(obj));

        return values.entrySet()
                .stream()
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .max(Map.Entry.comparingByValue())
                .orElse(null);
    }

    /**
     * 求val字段中的最小值(为空代表没有最小值，即所有值val为null)
     *
     * @param obj 包含val字段的对象
     */
    public static @Nullable Map.Entry<String, BigDecimal> minNameValueIgnoreNull(Object obj) {
        Map<String, BigDecimal> values = ValUtils.toValMap(Objects.requireNonNull(obj));

        return values.entrySet()
                .stream()
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .min(Map.Entry.comparingByValue())
                .orElse(null);
    }

    /**
     * 求val字段中的最大值(会列出所有并列的结果,集合为空代表没有最大值，即所有值val为null)
     *
     * @param obj 包含val字段的对象
     */
    public static List<Map.Entry<String, BigDecimal>> listMaxNameValueIgnoreNull(Object obj) {
        Map<String, BigDecimal> values = ValUtils.toValMap(Objects.requireNonNull(obj));

        return values.entrySet()
                .stream()
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .collect(com.goldwind.pf.common.util.Streams.listMax(Map.Entry.comparingByValue()));
    }

    /**
     * 求val字段中的最小值(会列出所有并列的结果,集合为空代表没有最小值，即所有值val为null)
     *
     * @param obj 包含val字段的对象
     */
    public static List<Map.Entry<String, BigDecimal>> listMinNameValueIgnoreNull(Object obj) {
        Map<String, BigDecimal> values = ValUtils.toValMap(Objects.requireNonNull(obj));
        return values.entrySet()
                .stream()
                .filter(entry -> Objects.nonNull(entry.getValue()))
                .collect(com.goldwind.pf.common.util.Streams.listMin(Map.Entry.comparingByValue()));
    }
}
