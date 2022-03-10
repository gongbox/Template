package com.gongbo.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ValUtils {

    public static final Pattern VAL_REGEX_PATTERN = Pattern.compile("^val(\\d+)$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VAL_15_PATTERN = Pattern.compile("^val(\\d){2}(00|15|30|45){1}$", Pattern.CASE_INSENSITIVE);


    public static final String VAL_XX_PATTERN = "val%02d";
    public static final String VAL_XXXX_PATTERN = "val%04d";
    public static final String VAL_XX00_PATTERN = "val%02d00";

    /**
     * 构造val字段名称
     *
     * @param valFieldPattern 如：val%02d
     * @param value           如：1
     * @return 如：val01
     */
    public static String buildValFieldName(String valFieldPattern, int value) {
        return String.format(valFieldPattern, value);
    }

    /**
     * 构造val字段名称(格式如：valXX)
     *
     * @param value 如：1
     * @return 如：val01
     */
    public static String buildValXXFieldName(int value) {
        return buildValFieldName(VAL_XX_PATTERN, value);
    }

    /**
     * 构造val字段名称(格式：valXXXX)
     *
     * @param value1 如：1
     * @param value2 如：5
     * @return 如：val0105
     */
    public static String buildValXXXXFieldName(int value1, int value2) {
        return buildValFieldName(VAL_XXXX_PATTERN, value1 * 100 + value2);
    }

    /**
     * 获取val字段值
     *
     * @param obj             数据
     * @param valFieldPattern val字段名称格式,如：val%02d
     * @param value           如：1
     * @return 如：val01
     */
    public static Object getValFieldValue(Object obj, String valFieldPattern, int value) {
        return getValFieldValue(obj, buildValFieldName(valFieldPattern, value));
    }

    /**
     * 获取val字段值
     *
     * @param obj       数据
     * @param fieldName 字段名
     */
    public static Object getValFieldValue(Object obj, String fieldName) {
        return ReflectUtil.getFieldValue(obj, fieldName);
    }

    /**
     * val字段求和 (求指定范围内的字段 如 start=val0001   end=val0016)
     */
    public static BigDecimal sumValFieldValue(Object obj) {
        return sumValFieldValue(obj, propertyDescriptor -> VAL_REGEX_PATTERN.matcher(propertyDescriptor.getName()).matches());
    }

    /**
     * val字段求和 (求指定范围内的字段 如 start=val0001   end=val0016)
     */
    public static BigDecimal sumValFieldValue(Object obj, String start, String end) {
        return sumValFieldValue(obj,
                propertyDescriptor -> (VAL_REGEX_PATTERN.matcher(propertyDescriptor.getDisplayName()).matches()
                        && (propertyDescriptor.getDisplayName().compareToIgnoreCase(start) >= 0)
                        && (propertyDescriptor.getDisplayName().compareToIgnoreCase(end) <= 0)));
    }

    /**
     * val字段求和
     */
    public static BigDecimal sumValFieldValue(Object obj, Predicate<PropertyDescriptor> predicate) {
        return values(obj, predicate)
                .stream()
                .filter(Objects::nonNull)
                .collect(com.goldwind.pf.common.util.Streams.summingBigDecimal());
    }

    /**
     * val字段名称提取数字部分（字符串格式）
     */
    public static final Function<PropertyDescriptor, String> VAL_FIELD_NUMBER_APPLY_TO_STRING = propertyDescriptor -> {
        Matcher matcher = VAL_REGEX_PATTERN.matcher(propertyDescriptor.getName());
        if (!matcher.matches()) {
            return null;
        }
        return matcher.group(1);
    };

    /**
     * val字段名称提取数字部分（整数格式）
     */
    public static final Function<PropertyDescriptor, Integer> VAL_FIELD_NUMBER_APPLY_TO_INT = propertyDescriptor -> Optional.ofNullable(VAL_FIELD_NUMBER_APPLY_TO_STRING.apply(propertyDescriptor))
            .map(Integer::valueOf)
            .orElse(null);

    /**
     * val字段名称提取月份部分(要求格式为：valXX，val忽略大小写)
     */
    public static final Function<PropertyDescriptor, Month> VAL_FIELD_NUMBER_APPLY_TO_MONTH = propertyDescriptor -> Optional.ofNullable(VAL_FIELD_NUMBER_APPLY_TO_INT.apply(propertyDescriptor))
            .map(Month::of)
            .orElse(null);

    /**
     * val字段名称提取时间部分(要求格式为：valXXXX，val忽略大小写)
     */
    public static final Function<PropertyDescriptor, LocalTime> VAL_FIELD_NUMBER_APPLY_TO_LOCAL_TIME = propertyDescriptor -> Optional.ofNullable(VAL_FIELD_NUMBER_APPLY_TO_STRING.apply(propertyDescriptor))
            .map(hhmm -> LocalTime.parse(hhmm, DateTimeFormatter.ofPattern("HHmm")))
            .orElse(null);

    /**
     * 范围筛选
     *
     * @param range
     * @return
     */
    public static Predicate<PropertyDescriptor> rangePredicate(Range<String> range) {
        return propertyDescriptor -> range.contains(propertyDescriptor.getName());
    }

    /**
     * 大于等于某一范围
     *
     * @param start
     * @return
     */
    public static Predicate<PropertyDescriptor> greaterOrEqualPredicate(String start) {
        return propertyDescriptor -> propertyDescriptor.getName().compareTo(start) >= 0;
    }

    /**
     * 小于等于某一范围
     *
     * @param end
     * @return
     */
    public static Predicate<PropertyDescriptor> lessOrEqualPredicate(String end) {
        return propertyDescriptor -> propertyDescriptor.getName().compareTo(end) <= 0;
    }


    /**
     * 集合相同字段值合并求和
     *
     * @param clazz 集合数据的class类型
     * @param list  计算的数据集合
     * @return map的key为字段映射的key, map的value为对应字段求和结果
     */
    public static <T> Map<String, BigDecimal> sumValFieldValueGroupBy(Class<T> clazz, List<T> list) {
        return sumValFieldValueGroupBy(clazz, list, VAL_FIELD_NUMBER_APPLY_TO_STRING);
    }

    /**
     * 集合相同字段值合并求和
     *
     * @param clazz    集合数据的class类型
     * @param list     计算的数据集合
     * @param grouping 字段映射为Key值，映射为空时则忽略该字段
     * @return map的key为字段映射的key, map的value为对应字段求和结果
     */
    public static <K, T> Map<K, BigDecimal> sumValFieldValueGroupBy(Class<T> clazz, List<T> list, Function<PropertyDescriptor, K> grouping) {
        Map<K, BigDecimal> result = new HashMap<>();

        if (CollUtil.isEmpty(list)) {
            return result;
        }

        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(clazz);

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            K key = grouping.apply(propertyDescriptor);
            if (key == null) {
                continue;
            }

            Method readMethod = propertyDescriptor.getReadMethod();
            BigDecimal sum = list.stream()
                    .map(t -> {
                        try {
                            Object value = readMethod.invoke(t);
                            return Optional.ofNullable(value)
                                    .map(String::valueOf)
                                    .map(BigDecimal::new)
                                    .orElse(BigDecimal.ZERO);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            log.error(e.getMessage(), e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .reduce(result.getOrDefault(key, BigDecimal.ZERO),
                            BigDecimal::add);

            result.put(key, sum);
        }

        return result;
    }

    /**
     * 获取所有val字段值
     */
    public static List<BigDecimal> values(Object obj) {
        return values(obj, propertyDescriptor -> VAL_REGEX_PATTERN.matcher(propertyDescriptor.getName()).matches());
    }

    /**
     * 获取所有val字段值
     */
    public static List<BigDecimal> values(Object obj, Predicate<PropertyDescriptor> predicate) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(obj.getClass());
        return Arrays.stream(propertyDescriptors)
                .filter(predicate)
                .map(propertyDescriptor -> {
                    Method readMethod = propertyDescriptor.getReadMethod();
                    Object value = null;
                    try {
                        value = readMethod.invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                    }
                    return NumberUtils.parseBigDecimal(value);
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取所有val字段名称 -> 值集合
     */
    public static Map<String, BigDecimal> toValMap(Object obj) {
        return toValMap(obj, propertyDescriptor -> VAL_REGEX_PATTERN.matcher(propertyDescriptor.getName()).matches());
    }

    /**
     * 获取所有val字段名称 -> 值集合
     */
    public static Map<String, BigDecimal> toValMap(Object obj, Predicate<PropertyDescriptor> predicate) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(obj.getClass());
        Map<String, BigDecimal> result = new HashMap<>();
        Arrays.stream(propertyDescriptors)
                .filter(predicate)
                .forEach(propertyDescriptor -> {
                    String name = propertyDescriptor.getName();
                    Method readMethod = propertyDescriptor.getReadMethod();
                    Object value = null;
                    try {
                        value = readMethod.invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                    }
                    result.put(name, NumberUtils.parseBigDecimal(value));
                });
        return result;
    }

    /**
     * bean转化为val值集合
     */
    public static BigDecimal[] toValArray(Object obj) {
        return toValArray(obj, null);
    }

    /**
     * bean转化为val值集合
     * mapper: 值进行转换
     */
    public static BigDecimal[] toValArray(Object obj, UnaryOperator<BigDecimal> mapper) {
        return toValArray(obj, propertyDescriptor -> VAL_REGEX_PATTERN.matcher(propertyDescriptor.getName()).matches(), mapper);
    }

    /**
     * bean转化为val值集合
     * mapper: 值进行转换
     */
    public static BigDecimal[] toValArray(Object obj, Predicate<PropertyDescriptor> predicate, UnaryOperator<BigDecimal> mapper) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(obj.getClass());

        return Arrays.stream(propertyDescriptors)
                .filter(predicate)
                .sorted(Comparator.comparing(PropertyDescriptor::getName))
                .map(propertyDescriptor -> {
                    BigDecimal value = null;
                    try {
                        value = (BigDecimal) propertyDescriptor.getReadMethod().invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                    }
                    if (mapper != null) {
                        return mapper.apply(value);
                    } else {
                        return value;
                    }
                }).toArray(BigDecimal[]::new);
    }

    /**
     * 获取第一个非空的val值
     *
     * @param obj
     * @return 如果所有值都为空，则返回空
     */
    public static BigDecimal firstNonNullValue(Object obj) {
        return Optional.ofNullable(nonNullProperty(obj, Comparator.comparing(PropertyDescriptor::getName)))
                .map(propertyDescriptor -> {
                    try {
                        return (BigDecimal) propertyDescriptor.getReadMethod().invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                    }
                    return null;
                }).orElse(null);
    }

    /**
     * 获取最后一个非空的val值
     *
     * @param obj
     * @return 如果所有值都为空，则返回空
     */
    public static BigDecimal lastNonNullValue(Object obj) {
        return Optional.ofNullable(nonNullProperty(obj, Comparator.comparing(PropertyDescriptor::getName).reversed()))
                .map(propertyDescriptor -> {
                    try {
                        return (BigDecimal) propertyDescriptor.getReadMethod().invoke(obj);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                    }
                    return null;
                }).orElse(null);
    }

    /**
     * 获取第一个非空的val字段名
     *
     * @param obj
     * @return 如果所有值都为空，则返回空
     */
    public static String firstNonNullFieldName(Object obj) {
        return Optional.ofNullable(nonNullProperty(obj, Comparator.comparing(PropertyDescriptor::getName)))
                .map(PropertyDescriptor::getName).orElse(null);
    }

    /**
     * 获取最后一个非空的字段名
     *
     * @param obj
     * @return 如果所有值都为空，则返回空
     */
    public static String lastNonNullFieldName(Object obj) {
        return Optional.ofNullable(nonNullProperty(obj, Comparator.comparing(PropertyDescriptor::getName).reversed()))
                .map(PropertyDescriptor::getName).orElse(null);
    }

    /**
     * 获取第一个非空的val对应的时间
     *
     * @param obj
     * @return 如果所有值都为空，则返回空
     */
    public static LocalTime firstNonNullTime(Object obj) {
        return Optional.ofNullable(nonNullProperty(obj, Comparator.comparing(PropertyDescriptor::getName)))
                .map(propertyDescriptor -> LocalTime.parse(propertyDescriptor.getName().toLowerCase().replace("val", ""), DateTimeFormatter.ofPattern("HHmm")))
                .orElse(null);
    }

    /**
     * 获取最后一个非空的val对应的时间
     *
     * @param obj
     * @return 如果所有值都为空，则返回空
     */
    public static LocalTime lastNonNullTime(Object obj) {
        return Optional.ofNullable(nonNullProperty(obj, Comparator.comparing(PropertyDescriptor::getName).reversed()))
                .map(propertyDescriptor -> LocalTime.parse(propertyDescriptor.getName().toLowerCase().replace("val", ""), DateTimeFormatter.ofPattern("HHmm")))
                .orElse(null);
    }

    /**
     * 获取第一个不为空的属性
     *
     * @param obj
     * @param comparator 排序规则
     * @return 如果所有值都为空，则返回空
     */
    public static PropertyDescriptor nonNullProperty(Object obj, Comparator<PropertyDescriptor> comparator) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(obj.getClass());
        return Arrays.stream(propertyDescriptors)
                .filter(propertyDescriptor -> VAL_REGEX_PATTERN.matcher(propertyDescriptor.getName()).matches())
                .sorted(comparator)
                .map(propertyDescriptor -> {
                    try {
                        if (propertyDescriptor.getReadMethod().invoke(obj) != null) {
                            return propertyDescriptor;
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * 替换null
     *
     * @param obj
     * @param defaultValue
     * @return
     */
    public static boolean checkAndReplaceNull(Object obj, Object defaultValue) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(obj.getClass());

        ValueWrapper<Boolean> flag = ValueWrapper.of(false);
        Arrays.stream(propertyDescriptors)
                .filter(propertyDescriptor -> VAL_REGEX_PATTERN.matcher(propertyDescriptor.getName()).matches())
                .forEach(propertyDescriptor -> {
                    try {
                        Object value = propertyDescriptor.getReadMethod().invoke(obj);
                        if (value == null) {
                            propertyDescriptor.getWriteMethod().invoke(obj, defaultValue);
                            flag.setValue(true);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        log.error(e.getMessage(), e);
                    }
                });
        return flag.getValue();
    }


    /**
     * 返回日期范围内的所有val数据，并根据日期排序
     *
     * @param data       数据
     * @param dateRange  日期范围
     * @param dateApply  提取日期
     * @param emptyApply 缺失该数据时
     * @param <T>
     * @return
     */
    public static <T> List<BigDecimal> listValuesByDate(List<T> data, Range<LocalDate> dateRange,
                                                        Function<T, String> dateApply,
                                                        Function<LocalDate, List<BigDecimal>> emptyApply) {
        return listValuesByDate(data, propertyDescriptor -> VAL_REGEX_PATTERN.matcher(propertyDescriptor.getName()).matches(),
                dateRange, dateApply, emptyApply);
    }

    /**
     * 返回日期范围内的所有val数据，并根据日期排序
     *
     * @param data       数据
     * @param dateRange  日期范围
     * @param dateApply  提取日期
     * @param predicate  字段过滤
     * @param emptyApply 缺失该数据时
     * @param <T>
     * @return
     */
    public static <T> List<BigDecimal> listValuesByDate(List<T> data,
                                                        Predicate<PropertyDescriptor> predicate,
                                                        Range<LocalDate> dateRange,
                                                        Function<T, String> dateApply,
                                                        Function<LocalDate, List<BigDecimal>> emptyApply) {
        List<LocalDate> dates = dateRange.toList(LocalDate::plusDays).stream()
                .sorted()
                .collect(Collectors.toList());

        Map<String, T> map = data.stream().collect(Collectors.toMap(dateApply, Function.identity()));

        List<BigDecimal> result = new ArrayList<>();
        for (LocalDate date : dates) {
            String dateText = date.format(DateTimeFormatter.BASIC_ISO_DATE);
            if (map.containsKey(dateText)) {
                result.addAll(ValUtils.values(map.get(dateText), predicate));
            } else {
                result.addAll(emptyApply.apply(date));
            }
        }

        return result;
    }
}
