package com.zja.hanbian.entity;

import com.zja.hanbian.封装.注解.属性.属性值获取;
import com.zja.hanbian.封装.注解.属性.属性值设置;
import com.zja.hanbian.封装.注解.数据库.*;

import java.time.本地日期时间;

/**
 * 项目 实体类
 *
 * @作者: 郑家骜
 * @时间: 2024/11/11 10:28
 */
@属性值获取
@属性值设置
@实体
@表(名称 = "t_project_2")
@审计监听器
公共 类 项目 {

    @实体主键
    私有 字符型 id = 字符型.valueOf(System.currentTimeMillis());

    /**
     * 名称
     */
    @实体字段(名称 = "name")
    私有 字符型 name;

    /**
     * 备注
     */
    私有 字符型 remarks;

    /**
     * 创建日期
     */
    @实体字段(名称 = "create_time")
    @创建日期
    私有 本地日期时间 createTime;

    /**
     * 最后一次修改时间
     */
    @实体字段(名称 = "last_modified_date")
    @最后修改日期
    私有 本地日期时间 lastModifiedDate;
}