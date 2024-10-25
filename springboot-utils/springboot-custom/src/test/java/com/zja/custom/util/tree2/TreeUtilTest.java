package com.zja.custom.util.tree2;

import com.alibaba.fastjson.JSON;
import com.zja.custom.util.vo.Menu;
import com.zja.custom.util.vo.Organization;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-24 14:47
 */
public class TreeUtilTest {

    @Test
    public void test_1() {
        List<Organization> organizations = Arrays.asList(
                new Organization("1", null, "组织1", "总部", "公司总部"),
                // new Organization("1", "-1", "组织1", "总部", "公司总部"),
                new Organization("2", "1", "组织2", "部门", "研发部"),
                new Organization("3", "1", "组织3", "部门", "市场部"),
                new Organization("4", "2", "组织4", "小组", "前端组")
        );

        List<TreeNode<String, Organization>> orgTreeNodes = TreeUtil.convertToTreeNodes(organizations, "id", "parentId", "name");
        System.out.println(JSON.toJSONString(orgTreeNodes));
        TreeUtil.printTree(orgTreeNodes, "");

        System.out.println("=====================");

        // List<TreeNode<String, Organization>> orgTree = TreeUtil.buildTree(orgTreeNodes);
        List<TreeNode<String, Organization>> orgTree = TreeUtil.buildTreeLeaf(orgTreeNodes);
        // List<TreeNode<String, Organization>> orgTree = TreeUtil.buildTreeLeaf(orgTreeNodes, "-1");
        System.out.println(JSON.toJSONString(orgTree));
        TreeUtil.printTree(orgTree, "");
    }

    @Test
    public void test_2() {
        List<Menu> menus = Arrays.asList(
                new Menu(1, null, "菜单1", "菜单"),
                // new Menu(1, -1, "菜单1", "菜单"),
                new Menu(2, 1, "菜单2", "菜单"),
                new Menu(3, 2, "菜单3", "菜单"),
                new Menu(4, 3, "菜单4", "菜单"),
                new Menu(5, 1, "菜单5", "菜单")
        );

        List<TreeNode<Integer, Menu>> menuTreeNodes = TreeUtil.convertToTreeNodes(menus, "id", "parentId", "name");
        System.out.println(JSON.toJSONString(menuTreeNodes));
        TreeUtil.printTree(menuTreeNodes, "");

        System.out.println("=====================");

        List<TreeNode<Integer, Menu>> treeNodes = TreeUtil.buildTreeLeaf(menuTreeNodes);
        // List<TreeNode<Integer, Menu>> treeNodes = TreeUtil.buildTreeLeaf(menuTreeNodes,-1);
        System.out.println(JSON.toJSONString(treeNodes));
        TreeUtil.printTree(treeNodes, "");
    }
}
