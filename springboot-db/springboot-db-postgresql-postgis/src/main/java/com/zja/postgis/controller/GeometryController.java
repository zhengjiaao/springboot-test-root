package com.zja.postgis.controller;

import com.zja.postgis.model.dto.GeometryDTO;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.request.GeometryPageRequest;
import com.zja.postgis.model.request.GeometryRequest;
import com.zja.postgis.model.request.GeometryUpdateRequest;
import com.zja.postgis.service.GeometryService;
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
 * 几何对象 接口层（一般与页面、功能对应）
 * @author: zhengja
 * @since: 2024/07/15 14:31
 */
@Validated
@RestController
@RequestMapping("/rest/geometry")
@Api(tags = {"几何对象管理页面"})
public class GeometryController {

    @Autowired
    GeometryService service;

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个几何对象详情")
    public GeometryDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.findById(id);
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询几何对象列表")
    public PageData<GeometryDTO> pageList(@Valid GeometryPageRequest pageRequest) {
        return service.pageList(pageRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加几何对象")
    public GeometryDTO add(@Valid @RequestBody GeometryRequest request) {
        return service.add(request);
    }

    @PostMapping("/add/batch")
    @ApiOperation("批量添加几何对象")
    public List<GeometryDTO> add(@Valid @RequestBody List<GeometryRequest> orgRequests) {
        return service.addBatch(orgRequests);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新几何对象")
    public GeometryDTO update(@NotBlank @PathVariable("id") String id,
                              @Valid @RequestBody GeometryUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除几何对象")
    public boolean deleteById(@NotBlank @PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除几何对象")
    public void deleteBatch(@RequestBody List<String> ids) {
        service.deleteBatch(ids);
    }
}