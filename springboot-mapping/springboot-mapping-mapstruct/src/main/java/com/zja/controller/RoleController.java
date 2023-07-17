/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-17 13:28
 * @Since:
 */
package com.zja.controller;

import com.zja.model.dto.RoleDTO;
import com.zja.model.request.RolePageSearchRequest;
import com.zja.model.request.RoleRequest;
import com.zja.model.request.RoleUpdateRequest;
import com.zja.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**角色
 * @author: zhengja
 * @since: 2023/07/17 13:28
 */
@Validated
@RestController
@RequestMapping("/rest/role")
@Api(tags = {"角色页面"})
public class RoleController {

    @Autowired
    RoleService service;

    /**
     * http://localhost:8080/swagger-ui/index.html#/
     */
    @GetMapping("/query/{id}")
    @ApiOperation("查询单个角色详情")
    public RoleDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.findById(id);
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询角色列表")
    public List<RoleDTO> pageList(@Valid RolePageSearchRequest pageSearchRequest) {
        return service.pageList(pageSearchRequest);
    }

    @PostMapping("/create")
    @ApiOperation("创建角色")
    public RoleDTO create(@Valid @RequestBody RoleRequest request) {
        return service.save(request);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新角色")
    public RoleDTO update(@NotBlank @PathVariable("id") String id,
                          @Valid @RequestBody RoleUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除角色")
    public void deleteById(@NotBlank @PathVariable("id") String id) {
        service.deleteById(id);
    }

}