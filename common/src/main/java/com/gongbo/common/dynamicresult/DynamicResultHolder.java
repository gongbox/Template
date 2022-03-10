package com.gongbo.common.dynamicresult;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DynamicResultHolder {

    private static final ThreadLocal<Class<? extends DynamicResult>> RESULT_CLASS_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 获取返回结果类型
     */
    public static Class<? extends DynamicResult> getResultType() {
        return RESULT_CLASS_THREAD_LOCAL.get();
    }

    /**
     * 设置返回结果类型
     */
    public static void setResultType(Class<? extends DynamicResult> type) {
        RESULT_CLASS_THREAD_LOCAL.set(type);
    }

    /**
     * 清除设置
     */
    public static void clear() {
        RESULT_CLASS_THREAD_LOCAL.remove();
    }
}
