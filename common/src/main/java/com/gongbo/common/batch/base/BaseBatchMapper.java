package com.gongbo.common.batch.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

public interface BaseBatchMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入 仅适用于mysql
     *
     * @param entityList 实体列表
     * @return 影响行数（注意，如果defaultExecutorType为BATCH时，返回值将固定为-2147482646）
     */
    int insertOrUpdateBatch(Collection<T> entityList);

}
