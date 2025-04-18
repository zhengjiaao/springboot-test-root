package com.zja.hanbian.service;

import com.zja.hanbian.封装.工具.异常;
import com.zja.hanbian.封装.工具.控制台;
import com.zja.hanbian.封装.工具.比较;
import com.zja.hanbian.封装.数据结构.*;
import com.zja.hanbian.封装.注解.服务;
import com.zja.hanbian.封装.工具.条件真假;

import java.lang.String;

/**
 * 汉编
 *
 * @作者: 郑家骜
 * @时间: 2024-09-19 17:27
 */
@服务
public class 用户管理服务 {
    private 字典<字符串, 任何对象> 用户名密码映射;

    public 用户管理服务() {
        // 用户名密码映射 = new 哈希字典<>();
        用户名密码映射 = 哈希字典.新建();
    }

    public 字符串 添加用户(字符串 用户名, 字符串 密码) {
        用户名密码映射.添加(用户名, 任何对象.属于(密码));
        控制台.输出("用户添加成功：" + 用户名);

        return 字符串.属于("成功");
    }

    public 字符串 更新用户(字符串 用户名, 字符串 新密码) {
        布尔值 条件 = 布尔值.属于(用户名密码映射.含密钥(用户名));
        条件真假.操作 假操作 = () -> {
            异常.抛出运行时异常("用户不存在：" + 用户名);
        };

        条件真假.执行条件假操作(条件, 假操作);

        用户名密码映射.添加(用户名, 任何对象.属于(新密码));
        控制台.输出("用户更新成功：" + 用户名);

        return 字符串.属于("成功");
    }

    public 字符串 删除用户(字符串 用户名) {
        用户名密码映射.删除(用户名);
        控制台.输出("用户删除成功：" + 用户名);

        return 字符串.属于("成功");
    }

    public 布尔值 验证用户(字符串 用户名, 字符串 密码) {

        布尔值 条件 = 布尔值.属于(用户名密码映射.含密钥(用户名) && 比较.相等(用户名密码映射.获取(用户名), 密码));

        条件真假.操作 真操作 = () -> 控制台.输出("用户验证通过：" + 用户名);
        条件真假.操作 假操作 = () -> 控制台.输出("用户验证失败：" + 用户名);

        return 条件真假.执行条件操作(条件, 真操作, 假操作);
    }

    public static void main(String[] args) {
        用户管理服务 服务 = new 用户管理服务();
        服务.添加用户(字符串.属于("张三"), 字符串.属于("123456"));
        服务.添加用户(字符串.属于("李四"), 字符串.属于("abcdef"));

        服务.验证用户(字符串.属于("张三"), 字符串.属于("123456"));
        服务.验证用户(字符串.属于("李四"), 字符串.属于("wrongpassword"));

        服务.删除用户(字符串.属于("张三"));

        服务.验证用户(字符串.属于("张三"), 字符串.属于("123456"));
    }
}

