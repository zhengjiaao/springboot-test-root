package com.zja.custom.util.tree2;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: zhengja
 * @Date: 2024-10-24 14:46
 */
public class TreeUtil {

    public static <T, V> List<TreeNode<T, V>> convertToTreeNodes(List<V> entities, String idFieldName, String parentIdFieldName, String nameFieldName) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }

        List<TreeNode<T, V>> treeNodes = new ArrayList<>();

        for (V entity : entities) {
            T id = getFieldValue(entity, idFieldName);
            T parentId = getFieldValue(entity, parentIdFieldName);
            String name = getFieldValue(entity, nameFieldName);
            TreeNode<T, V> treeNode = new TreeNode<>(id, parentId, entity, name);
            treeNodes.add(treeNode);
        }

        return treeNodes;
    }

    @SuppressWarnings("unchecked")
    private static <T, V> T getFieldValue(V entity, String fieldName) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get field value: " + fieldName, e);
        }
    }

    /**
     * 构建树形结构（根节点的标识，默认为 null）
     *
     * @param nodes 节点列表
     * @param <T>   节点数据类型
     * @param <V>   数据类型
     * @return 树形结构的根节点列表
     */
    public static <T, V> List<TreeNode<T, V>> buildTree(List<TreeNode<T, V>> nodes) {
        return buildTree(nodes, null);
    }

    /**
     * 构建树形结构
     *
     * @param nodes  节点列表
     * @param rootId 根节点的标识，可以为 null 或特定值
     * @param <T>    节点数据类型
     * @param <V>    数据类型
     * @return 树形结构的根节点列表
     */
    public static <T, V> List<TreeNode<T, V>> buildTree(List<TreeNode<T, V>> nodes, T rootId) {
        if (nodes == null || nodes.isEmpty()) {
            return Collections.emptyList();
        }

        // 使用Map存储所有节点，以便快速查找
        Map<T, TreeNode<T, V>> nodeMap = nodes.stream()
                .collect(Collectors.toMap(TreeNode<T, V>::getId, node -> node));

        // 构建树形结构
        List<TreeNode<T, V>> rootNodes = new ArrayList<>();
        for (TreeNode<T, V> node : nodes) {
            T parentId = node.getParentId();
            if (parentId == null || parentId.equals(rootId)) {
                rootNodes.add(node);
            } else {
                TreeNode<T, V> parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                    node.setLevel(parentNode.getLevel() + 1); // 设置层级
                }
            }
        }

        return rootNodes;
    }

    /**
     * 构建树形结构并设置叶子节点和最后一个节点(根节点的标识，默认为 null)
     *
     * @param nodes 节点列表
     * @param <T>   节点数据类型
     * @param <V>   数据类型
     * @return 树形结构的根节点列表
     */
    public static <T, V> List<TreeNode<T, V>> buildTreeLeaf(List<TreeNode<T, V>> nodes) {
        return buildTree(nodes, null);
    }

    /**
     * 构建树形结构并设置叶子节点和最后一个节点
     *
     * @param nodes  节点列表
     * @param rootId 根节点的标识，可以为 null 或特定值
     * @param <T>    节点数据类型
     * @param <V>    数据类型
     * @return 树形结构的根节点列表
     */
    public static <T, V> List<TreeNode<T, V>> buildTreeLeaf(List<TreeNode<T, V>> nodes, T rootId) {
        List<TreeNode<T, V>> rootNodes = buildTree(nodes, rootId);

        // 设置叶子节点和最后一个节点
        for (TreeNode<T, V> node : nodes) {
            if (node.hasChildren()) {
                node.setLeaf(false);
                int childCount = node.getChildrenCount();
                for (int i = 0; i < childCount; i++) {
                    TreeNode<T, V> child = node.getChildren().get(i);
                    if (i == childCount - 1) {
                        child.setLast(true);
                    } else {
                        child.setLast(false);
                    }
                }
            } else {
                node.setLeaf(true);
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
    public static <T, V> void printTree(List<TreeNode<T, V>> nodes, String prefix) {
        for (TreeNode<T, V> node : nodes) {
            System.out.println(prefix + node.getName() + " (ID: " + node.getId() + ", Parent ID: " + node.getParentId() + ", Level: " + node.getLevel() + ", Is Root: " + node.isRoot() + ", Is Leaf: " + node.isLeaf() + ", Is Last: " + node.isLast() + ", Value: " + JSON.toJSONString(node.getValue()));
            if (node.hasChildren()) {
                printTree(node.getChildren(), prefix + "  ");
            }
        }
    }
}
