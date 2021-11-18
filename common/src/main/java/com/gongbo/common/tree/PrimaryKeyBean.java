package com.gongbo.common.tree;

import java.io.Serializable;

/**
 * 带主键的实体类型
 *
 * @param <K> 主键类型
 */
public interface PrimaryKeyBean<K extends Serializable> {
    /**
     * 节点ID
     */
    K getId();
}
