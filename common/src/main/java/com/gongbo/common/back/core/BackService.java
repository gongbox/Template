package com.gongbo.common.back.core;


import com.gongbo.common.back.entity.SqlItem;

import java.util.Collection;

public interface BackService extends BackConsumer {

    /**
     * 是否允许回传
     */
    boolean isEnable();

    /**
     * 是否在事务中
     */
    boolean isTransaction();

    /**
     * 记录sql消费
     */
    @Override
    void consume(Integer delayMs, Collection<SqlItem> sqls);

}
