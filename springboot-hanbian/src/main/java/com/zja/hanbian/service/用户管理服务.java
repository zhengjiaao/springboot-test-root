package com.zja.hanbian.service;

import com.zja.hanbian.封装.工具.控制台;
import com.zja.hanbian.封装.工具.比较;
import com.zja.hanbian.封装.数据结构.哈希字典;
import com.zja.hanbian.封装.数据结构.字典;
import com.zja.hanbian.封装.数据结构.字符串;
import com.zja.hanbian.封装.数据结构.布尔值;
import com.zja.hanbian.封装.注解.服务;

import java.lang.String;

/**
 * 汉编
 *
 * @Author: zhengja
 * @Date: 2024-09-19 17:27
 */
@服务
public class 用户管理服务 {
    private 字典<字符串, Object> 用户名密码映射;

    public 用户管理服务() {
        用户名密码映射 = new 哈希字典<>();
    }

    public void 添加用户(字符串 用户名, 字符串 密码) {
        用户名密码映射.添加(用户名, 密码);
        控制台.输出("用户添加成功：" + 用户名);
    }

    public void 删除用户(字符串 用户名) {
        用户名密码映射.删除(用户名);
        控制台.输出("用户删除成功：" + 用户名);
    }

    public 布尔值 验证用户(字符串 用户名, 字符串 密码) {
        if (用户名密码映射.包含密钥(用户名) && 比较.相等(用户名密码映射.获取(用户名), 密码)) {
            控制台.输出("用户验证通过：" + 用户名);
            return 布尔值.属于("真");
        } else {
            控制台.输出("用户验证失败：" + 用户名);
            return 布尔值.属于("假");
        }
    }

    public static void main(String[] args) {
        用户管理服务 服务 = new 用户管理服务();
        服务.添加用户(字符串.值("张三"), 字符串.值("123456"));
        服务.添加用户(字符串.值("李四"), 字符串.值("abcdef"));

        服务.验证用户(字符串.值("张三"), 字符串.值("123456"));
        服务.验证用户(字符串.值("李四"), 字符串.值("wrongpassword"));

        服务.删除用户(字符串.值("张三"));

        服务.验证用户(字符串.值("张三"), 字符串.值("123456"));
    }
}

