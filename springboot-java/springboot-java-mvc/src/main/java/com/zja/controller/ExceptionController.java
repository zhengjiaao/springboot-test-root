package com.zja.controller;

import com.zja.vo.user.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
@RequestMapping("rest/user")
@Api(tags = {"Test-UserController"}, description = "用户管理模块")
public class ExceptionController {

    @GetMapping("get/v1")
    @ApiOperation(value = "获取用户信息")
    public UserVO getTest(@ApiParam(value = "用户名称", defaultValue = "李四", required = true) @RequestParam String name,
                          @ApiParam(value = "用户密码", defaultValue = "a12345") @RequestParam @NotBlank String password,
                          @ApiParam(value = "用户年龄", defaultValue = "22") @RequestParam Integer age) {
        System.out.println("age" + age);
        return new UserVO(name, password, age);
    }

    @PostMapping("post/v1")
    @ApiOperation(value = "新增用户信息")
    public Object postTest(@ApiParam(value = "参数说明-查看Model", required = true) @RequestBody @Valid UserVO userVO) {
        return userVO;
    }

    @PutMapping("put/v1")
    @ApiOperation(value = "更新用户信息")
    public Object putTest(@ApiParam(value = "参数说明-查看Model", required = true) @RequestBody @Valid UserVO userVO) {
        return userVO;
    }

    @DeleteMapping("delete/v1")
    @ApiOperation(value = "删除用户信息")
    public Object deleteTest(@ApiParam(value = "用户名称", defaultValue = "删除用户成功", required = true) @RequestParam String name) {
        return name;
    }

}
