package com.gongbo.common.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class TreeNode<K extends Serializable, T extends TreeBean<K>> {

    @NonNull
    private T value;

    private TreeNode<K, T> parent;

    private List<TreeNode<K, T>> children;


    @JsonIgnore
    public boolean isRoot() {
        return value.isRoot();
    }

    @JsonIgnore
    public K getId() {
        return value.getId();
    }

    @JsonIgnore
    public K getParentId() {
        return value.getParentId();
    }

    public List<TreeNode<K, T>> getChildren() {
        return Optional.ofNullable(children)
                .orElse(Collections.emptyList());
    }
}
