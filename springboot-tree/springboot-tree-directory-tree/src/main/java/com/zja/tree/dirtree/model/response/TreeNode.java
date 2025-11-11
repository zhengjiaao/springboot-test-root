package com.zja.tree.dirtree.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 11:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode<E> implements Serializable {
    private E node;
    private List<TreeNode<E>> children;

    public TreeNode(E node) {
        this.node = node;
    }
}
