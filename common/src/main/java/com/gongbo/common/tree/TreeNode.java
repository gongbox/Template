package com.gongbo.common.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gongbo.common.tree.TreeBean;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
public class TreeNode<PK extends Serializable, T extends TreeBean<PK>> {

    private T value;

    private TreeNode<PK, T> parent;

    private List<TreeNode<PK, T>> children;

    @JsonIgnore
    public boolean isRoot() {
        return value.isRoot();
    }

    @JsonIgnore
    public PK geId() {
        return value.getId();
    }

    @JsonIgnore
    public PK getParentId() {
        return value.getParentId();
    }

    public List<TreeNode<PK, T>> getChildren() {
        return Optional.ofNullable(children)
                .orElse(Collections.emptyList());
    }
}
