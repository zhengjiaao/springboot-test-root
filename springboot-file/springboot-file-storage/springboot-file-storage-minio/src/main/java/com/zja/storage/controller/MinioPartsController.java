package com.zja.storage.controller;

import com.zja.storage.service.MinioAsyncClientService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Minio分片 接口层（一般与页面、功能对应）
 *
 * @author: zhengja
 * @since: 2024/12/24 11:12
 */
@Validated
@RestController
@RequestMapping("/rest/minio/parts")
@Api(tags = {"Minio分片管理页面"})
public class MinioPartsController {

    @Autowired
    MinioAsyncClientService service;

  /*  @GetMapping("/query/{id}")
    @ApiOperation("查询单个Minio分片详情")
    public MinioPartsDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.queryById(id);
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询Minio分片列表")
    public PageData<MinioPartsDTO> pageList(@Valid MinioPartsPageRequest pageRequest) {
        return service.pageList(pageRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加Minio分片")
    public MinioPartsDTO add(@Valid @RequestBody MinioPartsRequest request) {
        return service.add(request);
    }

    @PostMapping("/add/batch")
    @ApiOperation("批量添加Minio分片")
    public List<MinioPartsDTO> add(@Valid @RequestBody List<MinioPartsRequest> orgRequests) {
        return service.addBatch(orgRequests);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新Minio分片")
    public MinioPartsDTO update(@NotBlank @PathVariable("id") String id,
                                @Valid @RequestBody MinioPartsUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除Minio分片")
    public boolean deleteById(@NotBlank @PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除Minio分片")
    public void deleteBatch(@RequestBody List<String> ids) {
        service.deleteBatch(ids);
    }*/
}