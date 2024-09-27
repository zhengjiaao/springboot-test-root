package com.zja.hanbian.service;

import com.zja.hanbian.注解.服务;

import java.util.HashMap;
import java.util.Map;

/**
 * 汉编
 *
 * @Author: zhengja
 * @Date: 2024-09-19 17:27
 */
@服务
public class 用户管理服务 {
    private Map<String, String> 用户名密码映射;

    public 用户管理服务() {
        用户名密码映射 = 创建地图();
    }

    public void 添加用户(String 用户名, String 密码) {
        用户名密码映射.put(用户名, 密码);
        输出("用户添加成功：" + 用户名);
    }

    public void 删除用户(String 用户名) {
        用户名密码映射.remove(用户名);
        输出("用户删除成功：" + 用户名);
    }

    public boolean 验证用户(String 用户名, String 密码) {
        if (用户名密码映射.containsKey(用户名) && 用户名密码映射.get(用户名).equals(密码)) {
            输出("用户验证通过：" + 用户名);
            return true;
        } else {
            输出("用户验证失败：" + 用户名);
            return false;
        }
    }

    public static void main(String[] args) {
        用户管理服务 服务 = new 用户管理服务();
        服务.添加用户("张三", "123456");
        服务.添加用户("李四", "abcdef");

        服务.验证用户("张三", "123456");
        服务.验证用户("李四", "wrongpassword");

        服务.删除用户("张三");
    }

    private Map<String, String> 创建地图() {
        return 用户名密码映射 = new HashMap<>();
    }

    public void 输出(String s) {
        System.out.println(s);
    }
}

