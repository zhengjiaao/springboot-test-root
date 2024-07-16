package com.zja.postgis.controller;

import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.dto.PointDTO;
import com.zja.postgis.model.request.PointPageRequest;
import com.zja.postgis.model.request.PointRequest;
import com.zja.postgis.model.request.PointUpdateRequest;
import com.zja.postgis.service.PointService;
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
 * 点 接口层（一般与页面、功能对应）
 * @author: zhengja
 * @since: 2024/07/15 14:02
 */
@Validated
@RestController
@RequestMapping("/rest/point")
@Api(tags = {"点管理页面"})
public class PointController {

    @Autowired
    PointService service;

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个点详情")
    public PointDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.findById(id);
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询点列表")
    public PageData<PointDTO> pageList(@Valid PointPageRequest pageRequest) {
        return service.pageList(pageRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加点")
    public PointDTO add(@Valid @RequestBody PointRequest request) {
        return service.add(request);
    }

    @PostMapping("/add/batch")
    @ApiOperation("批量添加点")
    public List<PointDTO> add(@Valid @RequestBody List<PointRequest> orgRequests) {
        return service.addBatch(orgRequests);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新点")
    public PointDTO update(@NotBlank @PathVariable("id") String id,
                           @Valid @RequestBody PointUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除点")
    public boolean deleteById(@NotBlank @PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除点")
    public void deleteBatch(@RequestBody List<String> ids) {
        service.deleteBatch(ids);
    }
}