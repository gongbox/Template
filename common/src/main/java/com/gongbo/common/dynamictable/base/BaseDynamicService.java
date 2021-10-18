package com.gongbo.common.dynamictable.base;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gongbo.common.dynamictable.core.DynamicTableOf;

/**
 * 动态表Service通用父类
 *
 * @param <T> 实体类型
 * @param <S> Service类型
 */
public interface BaseDynamicService<T, S> extends IService<T>, DynamicTableOf<T, S> {
}
