package com.gongbo.common.validate.custom.validator;


import com.gongbo.common.utils.Range;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RangeCheckContext {

    private static final ThreadLocal<Map<String, Range<Comparable<?>>>> rangeValueThreadLocal = ThreadLocal.withInitial(HashMap::new);

    /**
     * 获取当前线程比较值
     */
    public static Range<Comparable<?>> getCurrentRange(String group) {
        return rangeValueThreadLocal.get().computeIfAbsent(group, k -> new Range<>());
    }

    /**
     * 清除ThreadLocal
     */
    public static void clear() {
        rangeValueThreadLocal.remove();
    }

}
