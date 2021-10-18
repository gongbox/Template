package com.gongbo.common.dynamictable.base;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gongbo.common.dynamictable.core.DynamicTableHelper;
import com.gongbo.common.dynamictable.core.DynamicTableOf;

/**
 * 动态表Mapper通用父类
 *
 * @param <T> 实体类型
 * @param <S> Mapper类型
 */
public interface BaseDynamicMapper<T, S> extends BaseMapper<T>, DynamicTableOf<T, S> {

    @Override
    default Class<T> getEntityClass() {
        return (Class<T>) DynamicTableHelper.getEntityClassWithCache(this.getClass());
    }
}
