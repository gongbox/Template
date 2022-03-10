package com.gongbo.common.common.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.gongbo.common.batch.base.BaseInsertOnDuplicateKeyUpdateMapper;
import com.gongbo.common.common.entity.CommonVal1440View;
import com.gongbo.common.dynamictable.base.BaseDynamicMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 通用动态表
 */
@Mapper
public interface CommonValDynamicMapper extends BaseDynamicMapper<CommonVal1440View, CommonValDynamicMapper>, BaseInsertOnDuplicateKeyUpdateMapper<CommonVal1440View> {


    /**
     * 根据 entity 条件，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    CommonVal1440View selectOne(@Param(Constants.WRAPPER) Wrapper<CommonVal1440View> queryWrapper);

    /**
     * 根据 entity 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    List<CommonVal1440View> selectList(@Param(Constants.WRAPPER) Wrapper<CommonVal1440View> queryWrapper);

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    List<CommonVal1440View> selectByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    List<Map<String, Object>> selectMaps(@Param(Constants.WRAPPER) Wrapper<CommonVal1440View> queryWrapper);

    /**
     * 根据 Wrapper 条件，查询全部记录
     * <p>注意： 只返回第一个字段的值</p>
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    List<Object> selectObjs(@Param(Constants.WRAPPER) Wrapper<CommonVal1440View> queryWrapper);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULCommonVal1440View）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    <E extends IPage<CommonVal1440View>> E selectPage(E page, @Param(Constants.WRAPPER) Wrapper<CommonVal1440View> queryWrapper);

    /**
     * 根据 Wrapper 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件
     * @param queryWrapper 实体对象封装操作类
     */
    <E extends IPage<Map<String, Object>>> E selectMapsPage(E page, @Param(Constants.WRAPPER) Wrapper<CommonVal1440View> queryWrapper);

    /**
     * replace into 单条记录
     *
     * @param entity 实体对象
     * @return 影响行数（注意，如果defaultExecutorType为BATCH时，返回值将固定为-2147482646）
     */
    int replace(CommonVal1440View entity);
}
