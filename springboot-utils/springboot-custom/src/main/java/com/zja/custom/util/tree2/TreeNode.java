package com.zja.custom.util.tree2;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-24 13:29
 */
@Setter
@Getter
public class TreeNode<T, V> {
    private T id;
    private T parentId;
    private String name;
    private V value;
    private List<TreeNode<T, V>> children;
    private int level; // 表示当前节点的层级，根节点的层级为0，子节点的层级比父节点的层级大1。
    private boolean isRoot; // 表示当前节点是否是树的根节点，根节点通常是树的最顶层节点，没有父节点 parentId。
    private boolean isLeaf; // 表示当前节点是否是叶子节点，叶子节点是树的最底层节点，没有子节点 children。
    private boolean isLast; // 表示当前节点是否是其父节点的最后一个子节点，在某些场景下，需要区分某个节点是否是其父节点的最后一个子节点，例如在渲染树形结构时，最后一个子节点可能有不同的样式或行为。

    public TreeNode(T id, T parentId, V value, String name) {
        this.id = id;
        this.parentId = parentId;
        this.value = value;
        this.name = name;
        this.children = new ArrayList<>();
        this.level = 0;
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public int getChildrenCount() {
        return children.size();
    }
}
