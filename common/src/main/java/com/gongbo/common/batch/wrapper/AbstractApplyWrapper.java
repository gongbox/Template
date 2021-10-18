package com.gongbo.common.batch.wrapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 抽象ApplyWrapper
 *
 * @param <T> 实体类型泛型
 * @param <W> Wrapper类型泛型
 */
public class AbstractApplyWrapper<T, W extends Wrapper<T>> extends Wrapper<T> {
    protected final IService<T> service;
    protected final W wrapper;

    /**
     * 默认构造函数
     */
    protected AbstractApplyWrapper(IService<T> service, W wrapper) {
        this.service = service;
        this.wrapper = wrapper;
    }

    /**
     * 根据 Wrapper 条件，删除记录
     */
    public boolean remove() {
        return service.remove(wrapper);
    }

    /**
     * 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
     */
    public boolean update() {
        return service.update(wrapper);
    }

    /**
     * 根据 whereEntity 条件，更新记录
     *
     * @param entity 实体对象
     */
    public boolean update(T entity) {
        return service.update(entity, wrapper);
    }

    /**
     * 根据 Wrapper，查询一条记录 <br/>
     */
    public T getOne() {
        return getOne(true);
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param throwEx 有多个 result 是否抛出异常
     */
    public T getOne(boolean throwEx) {
        return service.getOne(wrapper, throwEx);
    }

    /**
     * 根据 Wrapper，查询一条记录
     */
    public Map<String, Object> getMap() {
        return service.getMap(wrapper);
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param mapper 转换函数
     */
    public <V> V getObj(Function<? super Object, V> mapper) {
        return service.getObj(wrapper, mapper);
    }

    /**
     * 根据 Wrapper 条件，查询记录数
     */
    public int count() {
        return service.count(wrapper);
    }

    /**
     * 根据 Wrapper 条件，查询是否存在记录
     */
    public boolean exists() {
        return count() > 0;
    }

    /**
     * 根据 Wrapper 条件，查询列表
     */
    public List<T> list() {
        return service.list(wrapper);
    }


    /**
     * 根据 Wrapper 条件，翻页查询
     *
     * @param page 翻页对象
     */
    public <E extends IPage<T>> E page(E page) {
        return service.page(page, wrapper);
    }

    /**
     * 根据 Wrapper 条件，查询列表
     */
    public List<Map<String, Object>> listMaps() {
        return service.listMaps(wrapper);
    }

    /**
     * 根据 Wrapper 条件，查询全部记录
     */
    public List<Object> listObjs() {
        return service.listObjs(wrapper);
    }

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param mapper 转换函数
     */
    public <V> List<V> listObjs(Function<? super Object, V> mapper) {
        return service.listObjs(wrapper, mapper);
    }

    /**
     * 翻页查询
     *
     * @param page 翻页对象
     */
    public <E extends IPage<Map<String, Object>>> E pageMaps(E page) {
        return service.pageMaps(page, wrapper);
    }

    @Override
    public T getEntity() {
        return wrapper.getEntity();
    }

    @Override
    public MergeSegments getExpression() {
        return wrapper.getExpression();
    }

    @Override
    public void clear() {
        wrapper.clear();
    }

    @Override
    public String getSqlSegment() {
        return wrapper.getSqlSegment();
    }
}
