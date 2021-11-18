package com.gongbo.common.tree;

import java.io.Serializable;

/**
 * 带主键的实体类型
 *
 * @param <PK> 主键类型
 */
public interface PrimaryKeyBean<PK extends Serializable> {
    PK getId();
}
