package com.gongbo.common.batch.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface BaseBatchService<T> extends IService<T> {

    /**
     * 批量插入 仅适用于mysql
     *
     * @param entityList 实体列表
     * @return 影响行数（注意，如果defaultExecutorType为BATCH时，返回值将固定为-2147482646）
     */
    @Transactional(rollbackFor = Exception.class)
    default int insertOrUpdateBatch(Collection<T> entityList) {
        return SqlHelper.retCount(((BaseBatchMapper<T>) getBaseMapper()).insertOrUpdateBatch(entityList));
    }


}
