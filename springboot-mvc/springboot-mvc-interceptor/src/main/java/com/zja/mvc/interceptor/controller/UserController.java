package com.zja.mvc.interceptor.controller;

import com.zja.mvc.interceptor.model.*;
import com.zja.mvc.interceptor.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author: zhengja
 * @since: 2024/03/11 15:19
 */
@Validated
@RestController
@RequestMapping("/rest/user")
@Api(tags = {"用户管理页面"})
public class UserController {

    @Autowired
    UserService service;

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个用户管理详情")
    public UserDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.findById(id);
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询用户管理列表")
    public PageData<UserDTO> pageList(@Valid UserPageSearchRequest pageSearchRequest) {
        return service.pageList(pageSearchRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加用户管理")
    public UserDTO add(@Valid @RequestBody UserRequest request) {
        return service.add(request);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新用户管理")
    public UserDTO update(@NotBlank @PathVariable("id") String id,
                          @Valid @RequestBody UserUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除用户管理")
    public void deleteById(@NotBlank @PathVariable("id") String id) {
        service.deleteById(id);
    }

}