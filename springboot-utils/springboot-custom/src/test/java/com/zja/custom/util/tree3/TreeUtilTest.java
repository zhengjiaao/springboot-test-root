package com.zja.custom.util.tree3;

import com.alibaba.fastjson.JSON;
import com.zja.custom.util.entity.MenuEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-24 16:42
 */
public class TreeUtilTest {

    @Test
    public void test_1() {

        List<MenuEntity> menuEntityList = Arrays.asList(
                new MenuEntity(1L, 0L, "菜单1", "1", null),
                new MenuEntity(2L, 0L, "菜单2", "2", null),
                new MenuEntity(3L, 1L, "菜单3", "3", null),
                new MenuEntity(4L, 1L, "菜单4", "4", null),
                new MenuEntity(5L, 2L, "菜单5", "5", null),
                new MenuEntity(6L, 3L, "菜单6", "6", null),
                new MenuEntity(7L, 4L, "菜单7", "7", null),
                new MenuEntity(8L, 5L, "菜单8", "8", null)
        );

        List<MenuEntity> menuEntities = TreeUtil.buildTree(menuEntityList);
        System.out.println(JSON.toJSONString(menuEntities));

        for (MenuEntity menu : menuEntities) {
            System.out.println("------------------------------------");
            System.out.println(menu.getName());
            System.out.println(menu.getParentId());
            System.out.println(menu.getId());
            System.out.println(menu.isRoot());
            System.out.println(menu.isLeaf());
            System.out.println(menu.hasChildren());
            System.out.println(JSON.toJSONString(menu.getChildren()));
        }

    }

}
