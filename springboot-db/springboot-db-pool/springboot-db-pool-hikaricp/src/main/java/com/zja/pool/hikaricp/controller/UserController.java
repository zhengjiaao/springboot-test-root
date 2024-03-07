/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-01 14:43
 * @Since:
 */
package com.zja.pool.hikaricp.controller;

import com.zja.pool.hikaricp.model.dto.PageData;
import com.zja.pool.hikaricp.model.dto.UserDTO;
import com.zja.pool.hikaricp.model.request.UserPageSearchRequest;
import com.zja.pool.hikaricp.model.request.UserRequest;
import com.zja.pool.hikaricp.model.request.UserUpdateRequest;
import com.zja.pool.hikaricp.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 用户管理
 *
 * @author: zhengja
 * @since: 2023/08/01 14:43
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

    @PostMapping("/create")
    @ApiOperation("创建用户管理")
    public UserDTO create(@Valid @RequestBody UserRequest request) {
        return service.save(request);
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