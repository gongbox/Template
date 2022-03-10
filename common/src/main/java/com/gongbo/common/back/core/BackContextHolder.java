package com.gongbo.common.back.core;


import com.gongbo.common.back.exception.BackException;

public class BackContextHolder {

    private static final ThreadLocal<BackContext> BACK_CONTEXT = new InheritableThreadLocal<>();

    /**
     * 获取当前BackContext
     */
    public static BackContext currentBackContext() {
        return BACK_CONTEXT.get();
    }

    /**
     * 新开启一个BackContext
     */
    public static BackContext newBackContext(BackConsumer backSender, boolean transaction) {
        if (currentBackContext() == null) {
            BackContext backContext = BackContext.builder()
                    .consumer(backSender)
                    .transaction(transaction)
                    .build();
            BACK_CONTEXT.set(backContext);
            return backContext;
        } else {
            throw new BackException("has exists BackContext");
        }
    }

    /**
     * 清除当前BackContext
     */
    public static void clearBackContext() {
        BACK_CONTEXT.remove();
    }

}
