package com.gongbo.common.back.core;

import com.gongbo.common.back.entity.SqlItem;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Collection;


public class BaseBackServiceImpl implements BackService {

    @Override
    public boolean isEnable() {
        //FIXME
        return true;
    }

    /**
     * 判断是否位于事务中
     */
    @Override
    public boolean isTransaction() {
        try {
            TransactionAspectSupport.currentTransactionStatus();
            return true;
        } catch (NoTransactionException e) {
            return false;
        }
    }

    /**
     * 记录sql消费
     */
    @Override
    public void consume(Integer delayMs, Collection<SqlItem> sqls) {
        //
    }

}
