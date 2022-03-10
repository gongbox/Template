package com.gongbo.common.batch.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

public interface BaseInsertOnDuplicateKeyUpdateMapper<T> extends BaseMapper<T> {

    /**
     * 插入一条记录
     *
     * @param entity 实体对象
     */
    int insertOnDuplicateKeyUpdate(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> insertWrapper);

}
