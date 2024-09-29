package com.zja.hanbian.controller;

import com.zja.hanbian.service.用户管理服务;
import com.zja.hanbian.封装.数据结构.字符串;
import com.zja.hanbian.封装.注解.*;

/**
 * 用户管理页面
 * @Author: zhengja
 * @Date: 2024-09-19 17:30
 */
@控制器请求映射
public class 用户管理控制器 {

    @自动装配
    用户管理服务 用户管理服务;

    @存储映射(路径 = "/添加")
    public void 添加用户() {
        用户管理服务.添加用户(字符串.值("李四"), 字符串.值("123456"));
    }

    @获取映射(路径 = "/验证")
    public void 验证用户() {
        用户管理服务.验证用户(字符串.值("李四"), 字符串.值("123456"));
    }

    @删除映射(路径 = "/删除")
    public void 删除用户() {
        用户管理服务.删除用户(字符串.值("李四"));
    }
}
