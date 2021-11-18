package com.gongbo.common.tree;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树操作工具类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TreeUtils {

    /**
     * 树结构转List
     */
    public static <K extends Serializable, T extends TreeBean<K>> List<T> treeToList(List<TreeNode<K, T>> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return Collections.emptyList();
        }

        //当前层级
        List<T> current = nodes.stream()
                .map(TreeNode::getValue)
                .collect(Collectors.toList());

        //找当前层级的下一层级
        List<TreeNode<K, T>> children = nodes.stream()
                .filter(n -> CollectionUtils.isNotEmpty(n.getChildren()))
                .flatMap(n -> n.getChildren().stream())
                .collect(Collectors.toList());

        if (!children.isEmpty()) {
            current.addAll(treeToList(children));
        }
        return current;
    }

    /**
     * 集合转树结构
     */
    public static <K extends Serializable, T extends TreeBean<K>> List<TreeNode<K, T>> listToTree(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        //转化为Node集合
        List<TreeNode<K, T>> nodes = list.stream()
                .map(TreeNode::new)
                .collect(Collectors.toList());

        //构建 id -> T 的map
        Map<K, TreeNode<K, T>> nodeMap = nodes.stream()
                .collect(Collectors.toMap(TreeNode::getId, Function.identity()));

        //构建 parentId -> T 的map
        Map<K, List<TreeNode<K, T>>> nodeChildrenMap = nodes.stream()
                .filter(n -> !n.isRoot())
                .collect(Collectors.groupingBy(TreeNode::getParentId));

        //填充node集合
        for (TreeNode<K, T> node : nodes) {
            if (!node.isRoot()) {
                node.setParent(nodeMap.get(node.getValue().getParentId()));
            }
            node.setChildren(nodeChildrenMap.get(node.getValue().getId()));
        }

        return nodes.stream()
                .filter(TreeNode::isRoot)
                .collect(Collectors.toList());
    }


    /**
     * 获取下级节点
     *
     * @param list       对象集合
     * @param parentId   要获取的父节点
     * @param withParent 是否包含父节点
     * @param <T>        泛型参数
     * @return
     */
    public static <K extends Serializable, T extends TreeBean<K>> List<T> listChildren(List<T> list, K parentId, boolean withParent) {
        return list.stream()
                .filter(t -> (withParent && t.getId().equals(parentId)) || parentId.equals(t.getParentId()))
                .collect(Collectors.toList());
    }


    /**
     * 获取节点的所有下级列表
     *
     * @param list       对象集合
     * @param parentId   要获取的父节点
     * @param withParent 是否包含父节点
     * @param <T>        类型
     * @return
     */
    public static <K extends Serializable, T extends TreeBean<K>> List<T> listAllChildren(List<T> list, K parentId, boolean withParent) {
        //先找到最顶层
        List<T> parentList = list.stream()
                .filter(t -> parentId.equals(t.getId()))
                .collect(Collectors.toList());

        List<T> result = new ArrayList<>();

        if (withParent) {
            result.addAll(parentList);
        }

        //循环填充下级数据
        while (CollectionUtils.isNotEmpty(parentList)) {
            Set<K> parentIds = parentList.stream().map(T::getId)
                    .collect(Collectors.toSet());

            List<T> subList = list.stream()
                    .filter(p -> parentIds.contains(p.getParentId()))
                    .collect(Collectors.toList());
            result.addAll(parentList = subList);
        }

        return result;
    }
}
