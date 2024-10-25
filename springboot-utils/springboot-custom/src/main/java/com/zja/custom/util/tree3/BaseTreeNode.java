package com.zja.custom.util.tree3;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-24 16:31
 */
public interface BaseTreeNode<T> {

    /**
     * 获取id
     *
     * @return 当前记录id
     */
    Long getId();

    /**
     * 获取父id
     *
     * @return 当前记录父id
     */
    Long getParentId();

    /**
     * 设置子节点
     *
     * @param children 子节点集合
     */
    void setChildren(List<T> children);

    /**
     * 获取子节点
     */
    List<T> getChildren();

    default boolean hasChildren() {
        return getChildren() != null && !getChildren().isEmpty();
    }

    default boolean isRoot() {
        return getParentId() == null || getParentId() == 0;
    }

    default boolean isLeaf() {
        return !hasChildren();
    }

}
