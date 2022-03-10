package com.gongbo.common.utils;

import cn.hutool.core.collection.CollUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionUtils extends CollUtil {

    /**
     * 合并集合
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param keyMap1     集合1数据转换为key
     * @param keyMap2     集合2数据转换为key
     * @param merge       合并
     * @param <T1>        集合类型1
     * @param <T2>        集合类型2
     * @param <K>         集合项映射类型
     */
    public static <T1, T2, K> void merge(Collection<T1> collection1, Collection<T2> collection2,
                                         Function<T1, K> keyMap1, Function<T2, K> keyMap2,
                                         BiConsumer<T1, T2> merge) {
        //不需要合并
        if (collection1 == null || collection2 == null) {
            return;
        }

        if (keyMap1 == null || keyMap2 == null || merge == null) {
            throw new NullPointerException();
        }

        //list2转换为 key -> item格式，方便索引数据
        Map<K, T2> keyCollection2 = collection2.stream()
                .collect(Collectors.toMap(keyMap2, Function.identity()));

        collection1.forEach(item1 -> {
            //获取T1类型对应的key，与T2相同
            K item1Key = keyMap1.apply(item1);

            //获取集合2对应位置的数据
            T2 item2 = keyCollection2.get(item1Key);

            //执行合并
            merge.accept(item1, item2);
        });
    }

    /**
     * 计算map所有value之和
     */
    public static BigDecimal sumMapValues(Map<?, BigDecimal> map) {
        if (isEmpty(map)) {
            return null;
        }

        return map.values()
                .stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 实现笛卡尔积
     */
    public static List<Object[]> descartes(List<?>... values) {
        List<Object[]> result = null;
        for (int i = 0; i < values.length; i++) {
            List<?> list = values[i];
            if (CollUtil.isEmpty(list)) {
                throw new IllegalArgumentException();
            }

            if (result == null) {
                result = list.stream()
                        .map(item -> new Object[]{item})
                        .collect(Collectors.toList());
            } else {
                int index = i;
                result = result.stream()
                        .flatMap(arr -> list.stream()
                                .map(item -> {
                                    Object[] newArr = new Object[arr.length + 1];
                                    System.arraycopy(arr, 0, newArr, 0, arr.length);
                                    newArr[index] = item;
                                    return newArr;
                                })).collect(Collectors.toList());
            }
        }
        return result;
    }

    /**
     * 判断集合是否有重复值
     */
    public static boolean isDistinct(Collection<?> collection) {
        if (collection instanceof Set) {
            return true;
        }
        return new HashSet<>(collection).size() == collection.size();
    }

    /**
     * 判断集合是否有重复值
     */
    public static boolean isNotDistinct(Collection<?> collection) {
        return !isDistinct(collection);
    }

}
