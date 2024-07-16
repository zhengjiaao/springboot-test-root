package com.zja.postgis.controller;

import com.zja.postgis.model.dto.MultiPointDTO;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.request.MultiPointPageRequest;
import com.zja.postgis.model.request.MultiPointRequest;
import com.zja.postgis.model.request.MultiPointUpdateRequest;
import com.zja.postgis.service.MultiPointService;
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
 * 点集合 接口层（一般与页面、功能对应）
 * @author: zhengja
 * @since: 2024/07/15 15:10
 */
@Validated
@RestController
@RequestMapping("/rest/multiPoint")
@Api(tags = {"点集合管理页面"})
public class MultiPointController {

    @Autowired
    MultiPointService service;

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个点集合详情")
    public MultiPointDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.findById(id);
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询点集合列表")
    public PageData<MultiPointDTO> pageList(@Valid MultiPointPageRequest pageRequest) {
        return service.pageList(pageRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加点集合")
    public MultiPointDTO add(@Valid @RequestBody MultiPointRequest request) {
        return service.add(request);
    }

    @PostMapping("/add/batch")
    @ApiOperation("批量添加点集合")
    public List<MultiPointDTO> add(@Valid @RequestBody List<MultiPointRequest> orgRequests) {
        return service.addBatch(orgRequests);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新点集合")
    public MultiPointDTO update(@NotBlank @PathVariable("id") String id,
                                @Valid @RequestBody MultiPointUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除点集合")
    public boolean deleteById(@NotBlank @PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除点集合")
    public void deleteBatch(@RequestBody List<String> ids) {
        service.deleteBatch(ids);
    }
}