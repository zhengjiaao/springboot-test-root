package com.zja.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-23 11:29
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：实体类基本操作
 */
@Validated
@RestController
@RequestMapping("/exception")
@Api(tags = {"Test-UserController"}, description = "用户管理模块")
public class ExceptionController {

    @GetMapping("get/v1")
    @ApiOperation(value = "正常请求接口")
    public String v1(@ApiParam(value = "用户名称", defaultValue = "李四", required = true) @RequestParam String name) {
        System.out.println("name" + name);
        return name;
    }

    @GetMapping("get/v2")
    @ApiOperation(value = "异常-自定义异常")
    public String v2(@ApiParam(value = "用户名称", defaultValue = "李四", required = true) @RequestParam String name) {
        System.out.println("name" + name);
        return name;
    }

}
