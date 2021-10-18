package com.gongbo.common.batch.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

import java.util.Collection;

public interface BaseReplaceService<T> extends IService<T> {

    /**
     * replace into 单条记录
     *
     * @param entity 实体对象
     * @return 影响行数（注意，如果defaultExecutorType为BATCH时，返回值将固定为-2147482646）
     */
    default int replace(T entity) {
        return SqlHelper.retCount(((BaseReplaceMapper<T>) getBaseMapper()).replace(entity));
    }

    /**
     * replace into 多条记录
     *
     * @param entityList 实体列表
     * @return 影响行数（注意，如果defaultExecutorType为BATCH时，返回值将固定为-2147482646）
     */
    default int replaceBatch(Collection<T> entityList) {
        return SqlHelper.retCount(((BaseReplaceMapper<T>) getBaseMapper()).replaceBatch(entityList));
    }
}
