package com.gongbo.common.tree.common;


/**
 * 树结构实体
 */
public interface TreeBean<K> extends PrimaryKeyBean<K> {
    K getParentId();

    boolean isRoot();
}
