package com.zja.postgis.controller;

import com.zja.postgis.model.dto.MultiPolygonDTO;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.request.MultiPolygonPageRequest;
import com.zja.postgis.model.request.MultiPolygonRequest;
import com.zja.postgis.model.request.MultiPolygonUpdateRequest;
import com.zja.postgis.service.MultiPolygonService;
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
 * 多边形集合 接口层（一般与页面、功能对应）
 * @author: zhengja
 * @since: 2024/07/15 15:20
 */
@Validated
@RestController
@RequestMapping("/rest/multiPolygon")
@Api(tags = {"面集合(多边形集合)管理页面"})
public class MultiPolygonController {

    @Autowired
    MultiPolygonService service;

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个多边形集合详情")
    public MultiPolygonDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.findById(id);
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询多边形集合列表")
    public PageData<MultiPolygonDTO> pageList(@Valid MultiPolygonPageRequest pageRequest) {
        return service.pageList(pageRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加多边形集合")
    public MultiPolygonDTO add(@Valid @RequestBody MultiPolygonRequest request) {
        return service.add(request);
    }

    @PostMapping("/add/batch")
    @ApiOperation("批量添加多边形集合")
    public List<MultiPolygonDTO> add(@Valid @RequestBody List<MultiPolygonRequest> orgRequests) {
        return service.addBatch(orgRequests);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新多边形集合")
    public MultiPolygonDTO update(@NotBlank @PathVariable("id") String id,
                                  @Valid @RequestBody MultiPolygonUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除多边形集合")
    public boolean deleteById(@NotBlank @PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除多边形集合")
    public void deleteBatch(@RequestBody List<String> ids) {
        service.deleteBatch(ids);
    }
}