package com.zja.custom.util.tree3;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: zhengja
 * @Date: 2024-10-24 16:34
 */
public class TreeUtil {

    public static <T extends BaseTreeNode<T>> List<T> buildTree(List<T> nodeList) {
        if (CollectionUtils.isEmpty(nodeList)) {
            return nodeList;
        }
        return buildTree(nodeList, 0L);
    }

    public static <T extends BaseTreeNode<T>> List<T> buildTree(List<T> nodeList, Long parentId) {
        if (CollectionUtils.isEmpty(nodeList)) {
            return nodeList;
        }

        // 以父id进行分组
        Map<Long, List<T>> map = nodeList.stream()
                .collect(Collectors.groupingBy(BaseTreeNode::getParentId));

        return buildTree(map, parentId);
    }

    private static <T extends BaseTreeNode<T>> List<T> buildTree(Map<Long, List<T>> map, Long parentId) {
        List<T> nodes = map.get(parentId);
        if (nodes == null) {
            return new ArrayList<>();
        }
        return nodes.stream()
                .map(node -> {
                    T newNode = deepCopy(node);
                    newNode.setChildren(buildTree(map, node.getId()));
                    return newNode;
                })
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <T extends BaseTreeNode<T>> T deepCopy(T original) {
        try {
            T copy = (T) original.getClass().newInstance();
            Field[] fields = original.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(copy, field.get(original));
            }
            return copy;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to deep copy object", e);
        }
    }

}
