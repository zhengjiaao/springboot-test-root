package com.dist.utils.tree;

import com.dist.utils.tree.dto.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/18 15:16
 */
public class TreeUtils {
    /**
     * 生成树型数据
     * @param nodes     树的基础数据
     * @param rootId    根的唯一标识
     * @return
     */
    public static final List<Tree> getTree(List<Tree> nodes, Integer rootId) {
        List<Tree> treeList = new ArrayList<>();
        if (nodes == null || nodes.size() <= 0) {
            return treeList;
        }
        // 根点集合
        List<Tree> rootNodes = new ArrayList<>();
        // 子节点集合
        List<Tree> childNodes = new ArrayList<>();
        nodes.forEach(node-> {
            Integer parentId = node.getParentId();
            // 当父节点为0时  当前节点为根节点
            if (rootId.equals(parentId)) {
                rootNodes.add(node);
            } else {
                childNodes.add(node);
            }
        });

        rootNodes.forEach(rootNode -> {
            addChilds(rootNode, childNodes);
        });
        return rootNodes;
    }

    /**
     * 为父节点添加所有的子节点
     * 一次性获取父节点的所有子节点（然后遍历子节点，把当前子节点当作父节点，为该节点再添加子节点）
     * @param parentNode    当前节点
     * @param childNodes    所有的节点
     */
    private static void addChilds(Tree parentNode, List<Tree> childNodes) {
        List<Tree> childs = getChilds(parentNode, childNodes);
        if (childs.size() > 0) {
            parentNode.setChildrens(childs);
            //为每一个子节点获取所有的自己的子节点
            childs.forEach(p_node -> {
                addChilds(p_node, childNodes);
            });
        }
    }

    /**
     * 获取父节点的所有直接子节点
     */
    private static List<Tree> getChilds(Tree rootNode, List<Tree> childNodes) {
        Integer id = rootNode.getId();
        List<Tree> nextNodes = new ArrayList<>();
        childNodes.forEach(childNode -> {
            Integer parentId = childNode.getParentId();
            if (id.equals(parentId)) {
                nextNodes.add(childNode);
            }
        });
        return nextNodes;
    }

}
