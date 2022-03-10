package com.gongbo.common.dynamictable.core;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gongbo.common.dynamictable.annotations.DynamicTable;
import com.gongbo.common.dynamictable.base.BaseDynamicMapper;
import com.gongbo.common.dynamictable.exception.NotFoundEntityClassException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态表辅助类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DynamicTableHelper {
    private static final ThreadLocal<Map<Class<?>, Object[]>> TABLE_THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);
    private static final Map<Class<?>, Class<?>> ENTITY_CLASS_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取Mapper对应实体类型
     */
    public static Class<?> getEntityClassWithCache(Class<?> clazz) {
        return ENTITY_CLASS_CACHE.computeIfAbsent(clazz, ignored -> getMapperDelegateEntityClass(clazz));
    }

    /**
     * 获取Mapper动态代理类对应实体类型
     */
    public static Class<?> getMapperDelegateEntityClass(Class<?> clazz) {
        //获取Mapper代理类实现的第一个接口类型（对应Mapper接口）
        Type mapperGenericInterface = clazz.getGenericInterfaces()[0];
        //获取Mapper接口的父接口数组
        Type[] genericInterfaces = ((Class<?>) mapperGenericInterface).getGenericInterfaces();

        for (Type type : genericInterfaces) {
            //找泛型父类型
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                if (parameterizedType.getRawType() == BaseMapper.class || parameterizedType.getRawType() == BaseDynamicMapper.class) {
                    return (Class<?>) parameterizedType.getActualTypeArguments()[0];
                }
            }
        }

        throw new NotFoundEntityClassException(MessageFormat.format("无法在类[{0}]上找到Entity类型", clazz.getCanonicalName()));
    }

    /**
     * 设置表参数
     */
    public static void tableOf(Class<?> clazz, Object[] params) {
        TABLE_THREAD_LOCAL.get().put(clazz, params);
    }

    /**
     * 设置表参数并返回Service/Mapper对象
     */
    public static <T> T tableOf(T service, Object... params) {
        Class<?> entityClass;
        if (service instanceof IService) {
            entityClass = ((IService<?>) service).getEntityClass();
        } else if (service instanceof BaseMapper) {
            entityClass = getEntityClassWithCache(service.getClass());
        } else {
            throw new IllegalArgumentException();
        }
        tableOf(entityClass, params);
        return service;
    }

    /**
     * 获取表参数
     */
    public static Object[] getParams(Class<?> entityType) {
        return TABLE_THREAD_LOCAL.get().get(entityType);
    }

    /**
     * 清除
     */
    public static void clear(Class<?> entityType) {
        TABLE_THREAD_LOCAL.get().remove(entityType);
    }

    /**
     * 构建表名
     */
    public static String buildTableName(Class<?> entityType, String format) {
        Object[] params = getParams(entityType);
        if (params == null) {
            throw new RuntimeException("动态表调用前请先设置表参数");
        }
        return String.format(format, params);
    }

    /**
     * 获取动态表映射集合
     */
    public static Map<String, TableNameHandler> getTableNameHandlers() {
        Map<String, TableNameHandler> tableNameHandlerMap = new HashMap<>();

        for (TableInfo tableInfo : TableInfoHelper.getTableInfos()) {
            Class<?> entityType = tableInfo.getEntityType();

            DynamicTable dynamicTable = entityType.getAnnotation(DynamicTable.class);
            if (dynamicTable != null) {
                tableNameHandlerMap.put(tableInfo.getTableName(), (sql, tableName) -> {
                    try {
                        return DynamicTableHelper.buildTableName(entityType, dynamicTable.format());
                    } finally {
                        DynamicTableHelper.clear(entityType);
                    }
                });
            }
        }
        return tableNameHandlerMap;
    }
}
