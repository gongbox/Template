package com.gongbo.common.validate.custom.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RangeCheckContext {

    private static final ThreadLocal<Map<String, Comparable<?>[]>> rangeValueThreadLocal = ThreadLocal.withInitial(HashMap::new);

    /**
     * 获取当前线程比较值
     */
    public static Comparable<?>[] currentOf(String group) {
        return rangeValueThreadLocal.get().computeIfAbsent(group, (_ignored) -> new Comparable<?>[2]);
    }

    /**
     * 清除ThreadLocal
     */
    public static void clear() {
        rangeValueThreadLocal.remove();
    }

}
