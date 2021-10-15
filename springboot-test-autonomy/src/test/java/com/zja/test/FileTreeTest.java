package com.zja.test;


import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/5/28 13:32
 */
public class FileTreeTest {

    @Test
    public void testtrees(){
        //目标位置  将该位置下的文件生成树的形状
        String path = "c://基线移动//资源管理文件";
        // 不需要的
        List<String> ignore = new ArrayList<>(Arrays.asList());
        Node tree = createTree(path, ignore);
        System.out.println(tree);
    }

    private static Node createTree(String path, List<String> ignores) {
        File file = new File(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("路径错误 == " + path);
        }

        String rootNodeID = getNodeID();
        String rootNodeName = file.getName();

        Node rootNode = new Node(rootNodeID, rootNodeName);
        doProcess(rootNode, file, ignores);
        return rootNode;
    }

    private static void doProcess(final Node rootNode, File file, List<String> ignores) {

        String rootNodeName = rootNode.getName();
        // 派出的文件
        if (ignores.contains(rootNodeName)) {
            return;
        }
        //单文件
        if (file.isFile()) {
            return;
        }
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("路径错误 == " + file.getAbsolutePath());
        }

        File[] files = file.listFiles((f, n) -> !ignores.contains(n));

        Arrays.stream(files).forEach(f -> {
            Node currentNode = new Node(getNodeID(), f.getName());
            Optional.ofNullable(rootNode.getChildrens())
                    .map(list -> list.add(currentNode))
                    .orElseGet(() -> {
                        List < Node > childrens = new ArrayList<>();
                        childrens.add(currentNode);
                        rootNode.setChildrens(childrens);
                        return null;
                    });
            doProcess(currentNode, f, ignores);
        });
    }

    /**
     * 获取节点的名字
     * @return
     */
    private static String getNodeID() {
        return System.currentTimeMillis() + "";
    }
}

class Node {
    private String id;
    private String name;
    private String icon;
    private List<Node> childrens;

    public Node() {}

    public Node(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Node> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<Node> childrens) {
        this.childrens = childrens;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", childrens=" + childrens +
                '}';
    }

}
