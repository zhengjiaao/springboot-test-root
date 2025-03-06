package com.zja.controller;

import com.zja.model.base.PageData;
import com.zja.model.dto.UserDTO;
import com.zja.model.request.UserPageRequest;
import com.zja.model.request.UserRequest;
import com.zja.model.request.UserUpdateRequest;
import com.zja.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 用户 接口层（一般与页面、功能对应）
 *
 * @author: zhengja
 * @since: 2025/03/05 14:08
 */
@CrossOrigin
@Validated
@RestController
@RequestMapping("/rest/user")
@Api(tags = {"用户管理页面"})
public class UserController {

    @Autowired
    UserService service;

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个用户详情")
    public UserDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.queryById(id);
    }

    @GetMapping("/list")
    @ApiOperation("查询用户列表")
    public List<UserDTO> list() {
        return service.list();
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询用户列表")
    public PageData<UserDTO> pageList(@Valid UserPageRequest pageRequest) {
        return service.pageList(pageRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加用户")
    public UserDTO add(@Valid @RequestBody UserRequest request) {
        return service.add(request);
    }

    @PostMapping("/add/batch")
    @ApiOperation("批量添加用户")
    public List<UserDTO> add(@Valid @RequestBody List<UserRequest> requests) {
        return service.addBatch(requests);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新用户")
    public UserDTO update(@NotBlank @PathVariable("id") String id,
                          @Valid @RequestBody UserUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除用户")
    public boolean deleteById(@NotBlank @PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除用户")
    public void deleteBatch(@RequestBody List<String> ids) {
        service.deleteBatch(ids);
    }
}