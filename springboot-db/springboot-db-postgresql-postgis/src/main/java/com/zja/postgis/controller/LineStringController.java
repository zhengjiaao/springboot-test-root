package com.zja.postgis.controller;

import com.zja.postgis.model.dto.LineStringDTO;
import com.zja.postgis.model.dto.PageData;
import com.zja.postgis.model.request.LineStringPageRequest;
import com.zja.postgis.model.request.LineStringRequest;
import com.zja.postgis.model.request.LineStringUpdateRequest;
import com.zja.postgis.service.LineStringService;
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
 * 线 接口层（一般与页面、功能对应）
 * @author: zhengja
 * @since: 2024/07/15 14:02
 */
@Validated
@RestController
@RequestMapping("/rest/lineString")
@Api(tags = {"线管理页面"})
public class LineStringController {

    @Autowired
    LineStringService service;

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个线详情")
    public LineStringDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.findById(id);
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询线列表")
    public PageData<LineStringDTO> pageList(@Valid LineStringPageRequest pageRequest) {
        return service.pageList(pageRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加线")
    public LineStringDTO add(@Valid @RequestBody LineStringRequest request) {
        return service.add(request);
    }

    @PostMapping("/add/batch")
    @ApiOperation("批量添加线")
    public List<LineStringDTO> add(@Valid @RequestBody List<LineStringRequest> orgRequests) {
        return service.addBatch(orgRequests);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新线")
    public LineStringDTO update(@NotBlank @PathVariable("id") String id,
                                @Valid @RequestBody LineStringUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除线")
    public boolean deleteById(@NotBlank @PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除线")
    public void deleteBatch(@RequestBody List<String> ids) {
        service.deleteBatch(ids);
    }
}