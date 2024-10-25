package com.zja.custom.util.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: zhengja
 * @Date: 2024-10-24 13:29
 */
public class TreeUtil {

    /**
     * 构建树形结构（根节点的标识，默认为 null）
     *
     * @param nodes 节点列表
     * @param <T>   节点数据类型
     * @return 树形结构的根节点列表
     */
    public static <T> List<TreeNode<T>> buildTree(List<TreeNode<T>> nodes) {
        return buildTree(nodes, null);
    }

    /**
     * 构建树形结构
     *
     * @param nodes  节点列表
     * @param rootId 根节点的标识，可以为 null 或特定值
     * @param <T>    节点数据类型
     * @return 树形结构的根节点列表
     */
    public static <T> List<TreeNode<T>> buildTree(List<TreeNode<T>> nodes, T rootId) {
        if (nodes == null || nodes.isEmpty()) {
            return Collections.emptyList();
        }

        // 使用Map存储所有节点，以便快速查找
        Map<T, TreeNode<T>> nodeMap = nodes.stream()
                .collect(Collectors.toMap(TreeNode::getId, node -> node));

        // 构建树形结构
        List<TreeNode<T>> rootNodes = new ArrayList<>();
        for (TreeNode<T> node : nodes) {
            T parentId = node.getParentId();
            if (parentId == null || parentId.equals(rootId)) {
                rootNodes.add(node);
            } else {
                TreeNode<T> parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                    node.setLevel(parentNode.getLevel() + 1); // 设置层级
                }
            }
        }

        return rootNodes;
    }

    /**
     * 构建树形结构（根节点的标识，默认为 null）
     *
     * @param nodes 节点列表
     * @param <T>   节点数据类型
     * @return 树形结构的根节点列表
     */
    public static <T> List<TreeNode<T>> buildTreeLeaf(List<TreeNode<T>> nodes) {
        return buildTreeLeaf(nodes, null);
    }

    /**
     * 构建树形结构
     *
     * @param nodes  节点列表
     * @param rootId 根节点的标识，可以为 null 或特定值
     * @param <T>    节点数据类型
     * @return 树形结构的根节点列表
     */
    public static <T> List<TreeNode<T>> buildTreeLeaf(List<TreeNode<T>> nodes, T rootId) {
        if (nodes == null || nodes.isEmpty()) {
            return Collections.emptyList();
        }

        // 使用Map存储所有节点，以便快速查找
        Map<T, TreeNode<T>> nodeMap = nodes.stream()
                .collect(Collectors.toMap(TreeNode::getId, node -> node));

        // 构建树形结构
        List<TreeNode<T>> rootNodes = new ArrayList<>();
        for (TreeNode<T> node : nodes) {
            T parentId = node.getParentId();
            if (parentId == null || parentId.equals(rootId)) {
                rootNodes.add(node);
                node.setRoot(true);
            } else {
                TreeNode<T> parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                    node.setLevel(parentNode.getLevel() + 1); // 设置层级
                    node.setRoot(false);
                    node.setLeaf(true); // 初始设置为叶子节点
                }
            }
        }

        // 设置叶子节点和最后一个节点
        for (TreeNode<T> node : nodes) {
            if (node.hasChildren()) {
                node.setLeaf(false);
                int childCount = node.getChildrenCount();
                for (int i = 0; i < childCount; i++) {
                    TreeNode<T> child = node.getChildren().get(i);
                    if (i == childCount - 1) {
                        child.setLast(true);
                    } else {
                        child.setLast(false);
                    }
                }
            }
        }

        return rootNodes;
    }

    /**
     * 打印树形结构
     *
     * @param nodes  节点列表
     * @param prefix 前缀，用于缩进
     * @param <T>    节点数据类型
     */
    public static <T> void printTree(List<TreeNode<T>> nodes, String prefix) {
        for (TreeNode<T> node : nodes) {
            System.out.println(prefix + node.getName() + " (ID: " + node.getId() + ", Parent ID: " + node.getParentId() + ", Level: " + node.getLevel() + ", Is Root: " + node.isRoot() + ", Is Leaf: " + node.isLeaf() + ", Is Last: " + node.isLast());
            if (node.hasChildren()) {
                printTree(node.getChildren(), prefix + "  ");
            }
        }
    }
}
