package com.zja.storage.controller;

import com.zja.storage.service.MinioClientService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Minio 接口层（一般与页面、功能对应）
 *
 * @author: zhengja
 * @since: 2024/12/24 11:08
 */
@Validated
@RestController
@RequestMapping("/rest/minio/file")
@Api(tags = {"Minio文件管理页面"})
public class MinioController {

    @Autowired
    MinioClientService service;

  /*  @GetMapping("/query/{id}")
    @ApiOperation("查询单个Minio文件详情")
    public MinioDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.queryById(id);
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询Minio文件列表")
    public PageData<MinioDTO> pageList(@Valid MinioPageRequest pageRequest) {
        return service.pageList(pageRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加Minio文件")
    public MinioDTO add(@Valid @RequestBody MinioRequest request) {
        return service.add(request);
    }

    @PostMapping("/add/batch")
    @ApiOperation("批量添加Minio文件")
    public List<MinioDTO> add(@Valid @RequestBody List<MinioRequest> orgRequests) {
        return service.addBatch(orgRequests);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新Minio文件")
    public MinioDTO update(@NotBlank @PathVariable("id") String id,
                           @Valid @RequestBody MinioUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除Minio文件")
    public boolean deleteById(@NotBlank @PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除Minio文件")
    public void deleteBatch(@RequestBody List<String> ids) {
        service.deleteBatch(ids);
    }

    @GetMapping("/compose")
    public void merge() {
        service.merge();
    }*/
}