包 com.zja.hanbian.controller;

导入 com.zja.hanbian.service.用户管理服务;
导入 com.zja.hanbian.封装.数据结构.字符串;
导入 com.zja.hanbian.封装.数据结构.布尔值;
导入 com.zja.hanbian.封装.注解.*;
导入 com.zja.hanbian.封装.结果.统一数据结构;

/**
 * 用户管理页面
 *
 * @作者: 郑家骜
 * @时间: 2024-09-19 17:30
 */
@请求跨域
@请求控制器
@请求映射(路径 = "/用户")
公共 类 用户管理控制器 {

    @自动装配
    用户管理服务 用户管理服务;

    @添加映射(路径 = "/添加")
    公共 统一数据结构<?> 添加用户() {
        字符串 结果 = 用户管理服务.添加用户(字符串.属于("李四"), 字符串.属于("123456"));

        返回 统一数据结构.数据(结果.值());
    }

    @修改映射(路径 = "/修改")
    公共 统一数据结构<?> 修改用户() {
        字符串 结果 = 用户管理服务.更新用户(字符串.属于("李四"), 字符串.属于("654321"));

        返回 统一数据结构.数据(结果.值());
    }

    @获取映射(路径 = "/验证")
    公共 统一数据结构<?> 验证用户() {
        布尔值 结果 = 用户管理服务.验证用户(字符串.属于("李四"), 字符串.属于("123456"));

        返回 统一数据结构.数据(结果.值());
    }

    @删除映射(路径 = "/删除")
    公共 统一数据结构<?> 删除用户() {
        字符串 结果 = 用户管理服务.删除用户(字符串.属于("李四"));

        返回 统一数据结构.数据(结果.值());
    }
}
