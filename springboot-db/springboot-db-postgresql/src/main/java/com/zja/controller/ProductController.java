package com.zja.controller;

import com.zja.model.base.PageData;
import com.zja.model.dto.ProductDTO;
import com.zja.model.request.ProductPageRequest;
import com.zja.model.request.ProductRequest;
import com.zja.model.request.ProductUpdateRequest;
import com.zja.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 产品 接口层（一般与页面、功能对应）
 *
 * @author: zhengja
 * @since: 2026/02/06 10:30
 */
@CrossOrigin
@Validated
@RestController
@RequestMapping("/rest/product")
@Api(tags = {"产品管理页面"})
public class ProductController {

    @Autowired
    ProductService service;

    @GetMapping("/query/{id}")
    @ApiOperation("查询单个产品详情")
    public ProductDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.queryById(id);
    }

    @GetMapping("/list")
    @ApiOperation("查询产品列表")
    public List<ProductDTO> list() {
        return service.list();
    }

    @GetMapping("/page/list")
    @ApiOperation("分页查询产品列表")
    public PageData<ProductDTO> pageList(@Valid ProductPageRequest pageRequest) {
        return service.pageList(pageRequest);
    }

    @PostMapping("/add")
    @ApiOperation("添加产品")
    public ProductDTO add(@Valid @RequestBody ProductRequest request) {
        return service.add(request);
    }

    @PostMapping("/add/batch")
    @ApiOperation("批量添加产品")
    public List<ProductDTO> add(@Valid @RequestBody List<ProductRequest> requests) {
        return service.addBatch(requests);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("更新产品")
    public ProductDTO update(@NotBlank @PathVariable("id") String id,
                             @Valid @RequestBody ProductUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除产品")
    public boolean deleteById(@NotBlank @PathVariable("id") String id) {
        return service.deleteById(id);
    }

    @DeleteMapping("/delete/batch")
    @ApiOperation("批量删除产品")
    public void deleteBatch(@RequestBody List<String> ids) {
        service.deleteBatch(ids);
    }
}