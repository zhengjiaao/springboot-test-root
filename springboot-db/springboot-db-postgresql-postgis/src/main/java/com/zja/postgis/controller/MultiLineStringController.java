package com.zja.postgis.controller;

import com.zja.postgis.model.dto.MultiLineStringDTO;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.request.MultiLineStringPageRequest;
import com.zja.postgis.model.request.MultiLineStringRequest;
import com.zja.postgis.model.request.MultiLineStringUpdateRequest;
import com.zja.postgis.service.MultiLineStringService;
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
 * 线集合 接口层（一般与页面、功能对应）
 * @author: zhengja
 * @since: 2024/07/15 15:18
 */
@Validated
@RestController
@RequestMapping("/rest/multiLineString")
@Api(tags = {"线集合管理页面"})
public class MultiLineStringController {

    @Autowired
    MultiLineStringService service;

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个线集合详情")
    public MultiLineStringDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.findById(id);
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询线集合列表")
    public PageData<MultiLineStringDTO> pageList(@Valid MultiLineStringPageRequest pageRequest) {
        return service.pageList(pageRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加线集合")
    public MultiLineStringDTO add(@Valid @RequestBody MultiLineStringRequest request) {
        return service.add(request);
    }

    @PostMapping("/add/batch")
    @ApiOperation("批量添加线集合")
    public List<MultiLineStringDTO> add(@Valid @RequestBody List<MultiLineStringRequest> orgRequests) {
        return service.addBatch(orgRequests);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新线集合")
    public MultiLineStringDTO update(@NotBlank @PathVariable("id") String id,
                                     @Valid @RequestBody MultiLineStringUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除线集合")
    public boolean deleteById(@NotBlank @PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除线集合")
    public void deleteBatch(@RequestBody List<String> ids) {
        service.deleteBatch(ids);
    }
}