package com.gongbo.common.batch.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

public interface BaseReplaceMapper<T> extends BaseMapper<T> {

    /**
     * replace into 单条记录
     *
     * @param entity 实体对象
     * @return 影响行数（注意，如果defaultExecutorType为BATCH时，返回值将固定为-2147482646）
     */
    int replace(T entity);

    /**
     * replace into 多条记录
     *
     * @param entityList 实体列表
     * @return 影响行数（注意，如果defaultExecutorType为BATCH时，返回值将固定为-2147482646）
     */
    int replaceBatch(Collection<T> entityList);
}
