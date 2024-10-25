package com.zja.custom.util.tree;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-24 13:30
 */
public class TreeUtilTest {

    @Test
    public void testBuildTree() {
        // 构建树节点列表
        List<TreeNode<Integer>> nodes = new ArrayList<>();
        nodes.add(new TreeNode<>(1, null, "根节点"));
        nodes.add(new TreeNode<>(2, 1, "子节点1"));
        nodes.add(new TreeNode<>(3, 1, "子节点2"));
        nodes.add(new TreeNode<>(4, 1, "子节点3"));
        nodes.add(new TreeNode<>(5, 1, "子节点4"));
        nodes.add(new TreeNode<>(6, 2, "孙节点1"));
        nodes.add(new TreeNode<>(7, 2, "孙节点2"));
        nodes.add(new TreeNode<>(8, 3, "孙节点3"));
        nodes.add(new TreeNode<>(9, 4, "曾孙节点1"));
        nodes.add(new TreeNode<>(10, 5, "曾孙节点2"));

        // List<TreeNode<Integer>> treeNodes = TreeUtil.buildTree(nodes);
        List<TreeNode<Integer>> treeNodes = TreeUtil.buildTreeLeaf(nodes);
        System.out.println(JSON.toJSONString(treeNodes));

        // 打印树形结构
        TreeUtil.printTree(treeNodes, "");
    }


    @Test
    public void testBuildTree2() {
        List<TreeNode<Integer>> menuTree = buildMenuTree();
        // 打印或返回给前端
        System.out.println(JSON.toJSONString(menuTree));

        // 打印树形结构
        TreeUtil.printTree(menuTree, "");
    }

    public List<TreeNode<Integer>> getMenuList() {
        // 模拟从数据库查询菜单列表
        return Arrays.asList(
                new TreeNode<>(1, null, "菜单1"),
                new TreeNode<>(2, 1, "菜单1-1"),
                new TreeNode<>(3, 1, "菜单1-2"),
                new TreeNode<>(4, null, "菜单2"),
                new TreeNode<>(5, 4, "菜单2-1")
        );
    }

    public List<TreeNode<Integer>> buildMenuTree() {
        List<TreeNode<Integer>> menuList = getMenuList();
        // return TreeUtil.buildTree(menuList); // 默认，指定根节点为 null
        return TreeUtil.buildTreeLeaf(menuList); // 默认，指定根节点为 null
    }

    @Test
    public void testBuildTree3() {
        List<TreeNode<String>> departmentTree = buildDepartmentTree();
        // 打印或返回给前端
        System.out.println(JSON.toJSONString(departmentTree));

        // 打印树形结构
        TreeUtil.printTree(departmentTree, "");
    }

    public List<TreeNode<String>> getDepartmentList() {
        // 模拟从数据库查询部门列表
        return Arrays.asList(
                new TreeNode<>("D001", "-1", "部门1"),
                new TreeNode<>("D002", "D001", "部门1-1"),
                new TreeNode<>("D003", "D001", "部门1-2"),
                new TreeNode<>("D004", "-1", "部门2"),
                new TreeNode<>("D005", "D004", "部门2-1"),
                new TreeNode<>("D008", "", "部门8") // "" 无法找到父节点，不会存在构建树形结构中
        );
    }

    public List<TreeNode<String>> buildDepartmentTree() {
        List<TreeNode<String>> departmentList = getDepartmentList();
        // return TreeUtil.buildTree(departmentList, "-1"); // 指定根节点为 "-1"
        return TreeUtil.buildTreeLeaf(departmentList, "-1"); // 指定根节点为 "-1"
    }

}
