package com.zja.postgis.controller;

import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.dto.PolygonDTO;
import com.zja.postgis.model.request.PolygonPageRequest;
import com.zja.postgis.model.request.PolygonRequest;
import com.zja.postgis.model.request.PolygonUpdateRequest;
import com.zja.postgis.service.PolygonService;
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
 * 面 接口层（一般与页面、功能对应）
 * @author: zhengja
 * @since: 2024/07/15 14:03
 */
@Validated
@RestController
@RequestMapping("/rest/polygon")
@Api(tags = {"面(多边形)管理页面"})
public class PolygonController {

    @Autowired
    PolygonService service;

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个面详情")
    public PolygonDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.findById(id);
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询面列表")
    public PageData<PolygonDTO> pageList(@Valid PolygonPageRequest pageRequest) {
        return service.pageList(pageRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加面")
    public PolygonDTO add(@Valid @RequestBody PolygonRequest request) {
        return service.add(request);
    }

    @PostMapping("/add/batch")
    @ApiOperation("批量添加面")
    public List<PolygonDTO> add(@Valid @RequestBody List<PolygonRequest> orgRequests) {
        return service.addBatch(orgRequests);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新面")
    public PolygonDTO update(@NotBlank @PathVariable("id") String id,
                             @Valid @RequestBody PolygonUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除面")
    public boolean deleteById(@NotBlank @PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除面")
    public void deleteBatch(@RequestBody List<String> ids) {
        service.deleteBatch(ids);
    }
}