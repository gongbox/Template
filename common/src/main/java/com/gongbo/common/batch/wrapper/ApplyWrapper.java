package com.gongbo.common.batch.wrapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.function.UnaryOperator;

/**
 * wrapper为Wrapper的通用ApplyWrapper
 */
public class ApplyWrapper<T> extends AbstractApplyWrapper<T, Wrapper<T>> {

    /**
     * 默认构造函数
     */
    protected ApplyWrapper(IService<T> service, Wrapper<T> wrapper) {
        super(service, wrapper);
    }

    /**
     * 静态构造方法
     */
    public static <T> ApplyWrapper<T> of(IService<T> service, Wrapper<T> wrapper) {
        return new ApplyWrapper<>(service, wrapper);
    }

    /**
     * wrapper修改
     */
    public ApplyWrapper<T> apply(UnaryOperator<Wrapper<T>> mapper) {
        return new ApplyWrapper<>(service, mapper.apply(wrapper));
    }

}
