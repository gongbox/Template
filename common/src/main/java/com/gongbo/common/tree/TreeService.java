package com.gongbo.common.tree;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface TreeService<K extends Serializable, T extends TreeBean<K>> extends IService<T> {

    /**
     * 获取所有数据的树结构
     */
    default List<TreeNode<K, T>> tree() {
        List<T> list = list();
        return TreeUtils.listToTree(list);
    }

    /**
     * 获取指定节点的树结构
     */
    default List<TreeNode<K, T>> tree(K parentId) {
        List<T> list = listAllChildren(parentId, true);
        return TreeUtils.listToTree(list);
    }

    /**
     * 获取节点的下级列表
     */
    default List<T> listChildren(K parentId) {
        return list(new QueryWrapper<T>().in("parent_id", parentId));//用Lambda不行
    }

    /**
     * 获取节点的下级列表
     */
    default List<K> listChildrenIds(K parentId) {
        return listChildren(parentId).stream().map(T::getId).collect(Collectors.toList());
    }

    /**
     * 获取节点的所有下级列表
     *
     * @param withParent 是否包含父级
     */
    default List<T> listAllChildren(K parentId, boolean withParent) {
        List<T> list = list();
        return TreeUtils.listAllChildren(list, parentId, withParent);
    }

    /**
     * 获取节点的所有下级ID列表
     *
     * @param withParent 是否包含父级
     */
    default Set<K> listAllChildrenIds(K parentId, boolean withParent) {
        return listAllChildren(parentId, withParent).stream().map(T::getId).collect(Collectors.toSet());
    }

}
