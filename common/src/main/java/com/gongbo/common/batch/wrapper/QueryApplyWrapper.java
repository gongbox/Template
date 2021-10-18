package com.gongbo.common.batch.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.function.UnaryOperator;

/**
 * wrapper为QueryWrapper的ApplyWrapper
 */
public class QueryApplyWrapper<T> extends AbstractApplyWrapper<T, QueryWrapper<T>> {

    /**
     * 默认构造函数
     */
    protected QueryApplyWrapper(IService<T> service, QueryWrapper<T> wrapper) {
        super(service, wrapper);
    }

    /**
     * 静态构造方法
     */
    public static <T> QueryApplyWrapper<T> of(IService<T> service, QueryWrapper<T> wrapper) {
        return new QueryApplyWrapper<>(service, wrapper);
    }

    /**
     * wrapper修改
     */
    public QueryApplyWrapper<T> apply(UnaryOperator<QueryWrapper<T>> mapper) {
        return new QueryApplyWrapper<>(service, mapper.apply(wrapper));
    }
}
