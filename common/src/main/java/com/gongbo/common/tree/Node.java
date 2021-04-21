package com.gongbo.common.tree;

import com.gongbo.common.tree.common.TreeBean;
import lombok.Data;

import java.io.Serializable;

@Data
public class Node<K extends Serializable, T extends TreeBean<K>> {

    private Node<K, T> next;

    private Node<K, T> pre;

    private T value;
}
