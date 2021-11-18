package com.gongbo.common.tree;


import java.io.Serializable;

/**
 * 树结构实体
 */
public interface TreeBean<K extends Serializable> extends PrimaryKeyBean<K> {
    /**
     * 父节点ID
     */
    K getParentId();

    /**
     * 是否是根节点
     */
    boolean isRoot();
}
