package com.gongbo.common.tree.common;

/**
 * 带主键的实体类型
 *
 * @param <K> 主键类型
 */
public interface PrimaryKeyBean<K> {
    K getId();
}
