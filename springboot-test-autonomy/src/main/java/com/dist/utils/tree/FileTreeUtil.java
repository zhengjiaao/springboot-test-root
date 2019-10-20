package com.dist.utils.tree;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dist.utils.tree.dto.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 生成树形结构方式一
 */
public class FileTreeUtil {

    public static void main(String[] args) {
        String[] trees = {"a/b/c/cc.xml", "a/b/bb.xml", "a/d/dd.xml", "e/e.xml"};
        List<TreeNode> treeNodes = getTree(trees);
        JSONArray jsonarr = JSONArray.parseArray(JSON.toJSONString(treeNodes));
        System.out.println(jsonarr.toJSONString());
    }


    /**
     * 多个文件及文件夹：路径生成树形结构
     * 如：String[] trees = {"a/b/c/cc.xml", "a/b/bb.xml", "a/d/dd.xml", "e/e.xml"};
     * @param trees
     * @return
     */
    public static List<TreeNode> getTree (String[] trees) {
        Map<String, TreeNode> nodeMap = new HashMap<>(16);
        String pt = null;
        for (String tree : trees) {
            String[] split = tree.split("/");

            for (String s : split) {
                if (pt != null) {
                    pt += "/" + s;
                } else {
                    pt = s;
                }
                if (nodeMap.containsKey(pt)) {
                    continue;
                }
                loadTree(pt, nodeMap);
            }
            pt = null;
        }


        return nodeMap.entrySet().stream()
                .filter(me -> !me.getKey().contains("/"))
                .map(me -> me.getValue())
                .collect(Collectors.toList());

    }

    private static void loadTree(String path, Map<String, TreeNode> nodeMap) {

        String nodeName = path.substring(path.lastIndexOf("/") + 1);
        if (path.contains(".")) {
            addChildrens(path, new TreeNode(path, nodeName), nodeMap);
        } else {
            //目录节点
            TreeNode node = new TreeNode(path, nodeName);
            if (path.contains("/")) {
                //父节点路径
                addChildrens(path, node, nodeMap);
            }
            nodeMap.putIfAbsent(path, node);
        }
    }

    /**
     * 在父节点上挂载当前子节点
     * @param path 当前节点的路径
     * @param currentNode 当前节点
     * @param nodeMap 节点集合
     */
    private static void addChildrens(String path, TreeNode currentNode, Map<String, TreeNode> nodeMap) {
        // 父节点路径
        String parentNodePath = path.substring(0, path.lastIndexOf("/"));
        // 文件节点
        TreeNode treeNode = nodeMap.get(parentNodePath);
        List<TreeNode> childrends = treeNode.getChildrends();
        if (childrends == null) {
            childrends = new ArrayList<>();
            treeNode.setChildrends(childrends);
        }
        childrends.add(currentNode);
    }
}
