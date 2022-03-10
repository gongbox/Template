package com.gongbo.common.batch.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

public interface BaseInsertOnDuplicateKeyUpdateService<T> extends IService<T> {

    /**
     * 插入一条记录
     */
    default int insertOnDuplicateKeyUpdate(Wrapper<T> insertWrapper) {
        return SqlHelper.retCount(((BaseInsertOnDuplicateKeyUpdateMapper<T>) getBaseMapper())
                .insertOnDuplicateKeyUpdate(insertWrapper.getEntity(), insertWrapper));
    }

}
