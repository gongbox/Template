package com.gongbo.common.common.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.gongbo.common.batch.base.BaseInsertOnDuplicateKeyUpdateService;
import com.gongbo.common.common.entity.CommonVal1440View;
import com.gongbo.common.common.mapper.CommonValDynamicMapper;
import com.gongbo.common.dynamicresult.DynamicResult;
import com.gongbo.common.dynamicresult.DynamicResultHolder;
import com.gongbo.common.dynamictable.base.BaseDynamicService;
import com.gongbo.common.dynamictable.core.DynamicTableHelper;
import com.gongbo.common.utils.BeanMap;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用val动态表
 */
public interface CommonValDynamicService extends BaseDynamicService<CommonVal1440View, CommonValDynamicService>,
        BaseInsertOnDuplicateKeyUpdateService<CommonVal1440View> {

    /**
     * 根据 Wrapper，查询一条记录,将查询结果转换为clazz类型对象
     * <p>结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")</p>
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param clazz        要转换的类型
     */
    default <T extends DynamicResult> T getOne(Class<T> clazz, Wrapper<T> queryWrapper) {
        try {
            DynamicResultHolder.setResultType(clazz);
            setTableName(clazz);
            return (T) BaseDynamicService.super.getOne((Wrapper<CommonVal1440View>) queryWrapper);
        } finally {
            DynamicResultHolder.clear();
        }
    }

    @Override
    default CommonVal1440View getOne(Wrapper<CommonVal1440View> queryWrapper) {
        return getOne(CommonVal1440View.class, queryWrapper);
    }

    /**
     * 查询列表，将查询结果转换为clazz类型对象
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param clazz        要转换的类型
     */
    default <T extends DynamicResult> List<T> list(Class<T> clazz, Wrapper<T> queryWrapper) {
        try {
            DynamicResultHolder.setResultType(clazz);
            setTableName(clazz);
            return (List<T>) BaseDynamicService.super.list((Wrapper<CommonVal1440View>) queryWrapper);
        } finally {
            DynamicResultHolder.clear();
        }
    }

    /**
     * 查询所有，将查询结果转换为clazz类型对象
     *
     * @param clazz 要转换的类型
     * @see Wrappers#emptyWrapper()
     */
    default <T extends DynamicResult> List<T> list(Class<T> clazz) {
        try {
            DynamicResultHolder.setResultType(clazz);
            setTableName(clazz);
            return (List<T>) BaseDynamicService.super.list();
        } finally {
            DynamicResultHolder.clear();
        }
    }

    @Override
    default List<CommonVal1440View> list() {
        return list(CommonVal1440View.class);
    }

    @Override
    default List<CommonVal1440View> list(Wrapper<CommonVal1440View> queryWrapper) {
        return list(CommonVal1440View.class, queryWrapper);
    }

    /**
     * 查询（根据 columnMap 条件）,查询结果转换为clazz类型对象
     *
     * @param columnMap 表字段 map 对象
     * @param clazz     要转换的类型
     */
    default <T extends DynamicResult> List<T> listByMap(Class<T> clazz, Map<String, Object> columnMap) {
        try {
            DynamicResultHolder.setResultType(clazz);
            setTableName(clazz);
            return (List<T>) BaseDynamicService.super.listByMap(columnMap);
        } finally {
            DynamicResultHolder.clear();
        }
    }

    @Override
    default List<CommonVal1440View> listByMap(Map<String, Object> columnMap) {
        return listByMap(CommonVal1440View.class, columnMap);
    }

    /**
     * 翻页查询,将分页结果由IPage<CommonVal1440View>转换为 IPage<T>
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param clazz        要转换的类型
     */
    default <T extends DynamicResult, E extends IPage<T>> E page(Class<T> clazz, E page, Wrapper<T> queryWrapper) {
        try {
            DynamicResultHolder.setResultType(clazz);
            setTableName(clazz);
            return (E) BaseDynamicService.super.page((IPage<CommonVal1440View>) page, (Wrapper<CommonVal1440View>) queryWrapper);
        } finally {
            DynamicResultHolder.clear();
        }
    }

    @Override
    default <E extends IPage<CommonVal1440View>> E page(E page, Wrapper<CommonVal1440View> queryWrapper) {
        return page(CommonVal1440View.class, page, queryWrapper);
    }

    /**
     * 无条件翻页查询,将分页结果由IPage<CommonVal1440View>转换为IPage<T>
     *
     * @param page  翻页对象
     * @param clazz 要转换的类型
     * @see Wrappers#emptyWrapper()
     */
    default <T extends DynamicResult, E extends IPage<T>> E page(Class<T> clazz, E page) {
        try {
            DynamicResultHolder.setResultType(clazz);
            setTableName(clazz);
            return (E) BaseDynamicService.super.page((IPage<CommonVal1440View>) page);
        } finally {
            DynamicResultHolder.clear();
        }
    }

    @Override
    default <E extends IPage<CommonVal1440View>> E page(E page) {
        return page(CommonVal1440View.class, page);
    }

    /**
     * 查询总记录数
     *
     * @see Wrappers#emptyWrapper()
     */
    default int count(Class<?> clazz) {
        setTableName(clazz);
        return count();
    }

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    default <T> int count(Class<T> clazz, Wrapper<T> queryWrapper) {
        setTableName(clazz);
        return count((Wrapper<CommonVal1440View>) queryWrapper);
    }


    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    default boolean insert(Object entity) {
        setTableName(entity.getClass());
        return save(BeanMap.mapTo(entity, CommonVal1440View::new));
    }

    /**
     * 插入（批量）
     *
     * @param entityList 实体对象集合
     */
    @Transactional(rollbackFor = Exception.class)
    default <T> boolean insertBatch(Collection<T> entityList) {
        if (CollUtil.isEmpty(entityList)) return false;
        Class<?> clazz = entityList.stream().findAny().orElseThrow(NullPointerException::new).getClass();
        setTableName(clazz);
        return saveBatch(entityList.stream()
                .map(obj -> BeanMap.mapTo(obj, CommonVal1440View::new))
                .collect(Collectors.toList()));
    }

    /**
     * 根据 whereEntity 条件，更新记录
     *
     * @param entity        实体对象
     * @param updateWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
     */
    default boolean updateByEntity(Object entity, Wrapper<CommonVal1440View> updateWrapper) {
        setTableName(entity.getClass());
        return update(BeanMap.mapTo(entity, CommonVal1440View::new), updateWrapper);
    }

    /**
     * replace into 单条记录
     *
     * @param entity 实体对象
     * @return 影响行数（注意，如果defaultExecutorType为BATCH时，返回值将固定为-2147482646）
     */
    default <T> int replace(T entity) {
        CommonVal1440View replaceEntity = BeanMap.mapTo(entity, CommonVal1440View::new);
        return SqlHelper.retCount(((CommonValDynamicMapper) getBaseMapper()).replace(replaceEntity));
    }

    /**
     * 根据 ID 删除
     *
     * @param id 主键ID
     */
    @Override
    default boolean removeById(Serializable id) {
        throw new UnsupportedOperationException();
    }

    /**
     * 删除（根据ID 批量删除）
     *
     * @param idList 主键ID列表
     */
    @Override
    default boolean removeByIds(Collection<? extends Serializable> idList) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据 ID 选择修改
     *
     * @param entity 实体对象
     */
    @Override
    default boolean updateById(CommonVal1440View entity) {
        throw new UnsupportedOperationException();
    }


    /**
     * 根据ID 批量更新
     *
     * @param entityList 实体对象集合
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    default boolean updateBatchById(Collection<CommonVal1440View> entityList) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据ID 批量更新
     *
     * @param entityList 实体对象集合
     * @param batchSize  更新批次数量
     */
    @Override
    default boolean updateBatchById(Collection<CommonVal1440View> entityList, int batchSize) {
        throw new UnsupportedOperationException();
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     */
    @Override
    default boolean saveOrUpdate(CommonVal1440View entity) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
     * 此次修改主要是减少了此项业务代码的代码量（存在性验证之后的saveOrUpdate操作）
     *
     * @param entity 实体对象
     */
    @Override
    default boolean saveOrUpdate(CommonVal1440View entity, Wrapper<CommonVal1440View> updateWrapper) {
        throw new UnsupportedOperationException();
    }

    /**
     * 批量修改插入
     *
     * @param entityList 实体对象集合
     */
    @Transactional(rollbackFor = Exception.class)
    default <T> boolean insertOrUpdateBatch(Collection<T> entityList) {
        throw new UnsupportedOperationException();
    }

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    @Override
    default CommonVal1440View getById(Serializable id) {
        throw new UnsupportedOperationException();
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表
     */
    @Override
    default List<CommonVal1440View> listByIds(Collection<? extends Serializable> idList) {
        throw new UnsupportedOperationException();
    }

    static void setTableName(Class<?> clazz) {
        //已经设置动态表参数的忽略
        Object[] params = DynamicTableHelper.getParams(CommonVal1440View.class);
        if (params == null || params.length == 0) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
            //
            if (tableInfo != null) {
                DynamicTableHelper.tableOf(CommonVal1440View.class, new Object[]{tableInfo.getTableName()});
            }
        }
    }

}
