package com.gongbo.common.dynamictable.core;

public interface DynamicTableOf<T, S> {

    Class<T> getEntityClass();

    default S tableOf(Object... params) {
        DynamicTableHelper.tableOf(getEntityClass(), params);
        return (S) this;
    }
}
