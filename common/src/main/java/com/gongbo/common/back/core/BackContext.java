package com.gongbo.common.back.core;

import cn.hutool.core.util.StrUtil;
import com.gongbo.common.back.entity.SqlItem;
import com.gongbo.common.back.exception.BackException;
import lombok.*;

import java.util.Collection;
import java.util.LinkedList;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackContext {

    /**
     * 未开启
     */
    public static final int RESET = 0;
    /**
     * 已开启
     */
    public static final int START = 1;
    /**
     * 已停止
     */
    public static final int STOP = 4;

    @Builder.Default
    private int state = RESET;
    @Builder.Default
    private Collection<String> currentSqls = new LinkedList<>();
    @Builder.Default
    private Collection<SqlItem> sqls = new LinkedList<>();
    //发送回传消息
    private BackConsumer consumer;
    //判断是否在事务中
    private Boolean transaction;
    //额外信息
    private Object[] extra;
    //延时时间
    private Integer delayMs;

    /**
     * 是否处于回传
     */
    public boolean isBacking() {
        return state == START;
    }

    /**
     * 添加到回传sql记录中
     */
    public void addWhenBacking(String sql) {
        if (!isBacking()) {
            return;
        }
        if (StrUtil.isNotEmpty(sql)) {
            currentSqls.add(sql);
        }
    }

    /**
     * 开启回传记录
     */
    public void start(Integer delayMs, Object... extra) {
        state = START;
        //清空当前记录
        flushCurrent();
        this.delayMs = delayMs;
        this.extra = extra;
    }

    /**
     * 开启回传记录
     */
    public void start(Object... extra) {
        start(0, extra);
    }

    /**
     * 停止回传记录
     */
    public void stop() {
        if (state < START) {
            throw new BackException("not start");
        }

        if (!transaction) {
            //不处于事务，则直接回传并复位
            consumer.consume(delayMs, SqlItem.wrap(currentSqls, extra));
            //重置当前线程BackContext
            reset();
        } else {
            //将当前sql合并到历史sql中
            flushCurrent();
            state = STOP;
        }
    }

    /**
     * 处于回传时，将回传内容发送，并复位
     */
    public void commit() {
        if (!transaction) {
            return;
        }
        if (state > BackContext.RESET) {
            consumer.consume(delayMs, sqls);
            reset();
        }
    }

    /**
     * 处于回传时，将回传内容清空，并复位
     */
    public void rollback() {
        if (!transaction) {
            return;
        }
        if (state > BackContext.RESET) {
            reset();
        }
    }

    /**
     * 复位，状态位清零，回传内容清空，清除当前线程BackContext
     */
    private void reset() {
        state = RESET;
        sqls.clear();
        currentSqls.clear();
        extra = null;
        delayMs = 0;
        //当前线程清除BackContext
        BackContextHolder.clearBackContext();
    }

    /**
     * 清除当前记录（作废）
     */
    public void clearCurrent() {
        currentSqls.clear();
        extra = null;
    }

    /**
     * 当前sql记录存入记录，并清空
     */
    private void flushCurrent() {
        sqls.addAll(SqlItem.wrap(currentSqls, extra));
        clearCurrent();
    }
}