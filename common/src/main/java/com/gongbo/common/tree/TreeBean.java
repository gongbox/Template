package com.gongbo.common.tree;


import java.io.Serializable;

/**
 * 树结构实体
 */
public interface TreeBean<PK extends Serializable> extends PrimaryKeyBean<PK> {
    PK getParentId();

    boolean isRoot();
}
