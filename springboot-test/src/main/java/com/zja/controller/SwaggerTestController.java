package com.zja.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ResponseHeader;
import org.springframework.web.bind.annotation.*;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-23 10:18
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：swagger使用测试
 */
@RestController
@RequestMapping("swagger/test")
@Api(tags = {"Test-SwaggerTestController"}, description = "swagger使用测试")
public class SwaggerTestController {

    @GetMapping("get/v1")
    @ResponseHeader
    @ApiOperation(value = "get测试 接口说明", notes = "可选-接口附加说明")
    public Object getTest() {
        return "get";
    }

    @PostMapping("post/v1")
    @ApiOperation(value = "post测试 接口说明", notes = "可选-接口附加说明")
    public Object postTest() {
        return "post";
    }

    @PutMapping("put/v1")
    @ApiOperation(value = "put测试 接口说明", notes = "可选-接口附加说明")
    public Object putTest() {
        return "put";
    }

    @DeleteMapping("delete/v1")
    @ApiOperation(value = "delete测试 接口说明", notes = "可选-接口附加说明")
    public Object deleteTest() {
        return "delete";
    }


    @GetMapping("get/v2")
    @ApiOperation(value = "get测试 接口说明", notes = "可选-接口附加说明")
    public Object getTest(@ApiParam(value = "参数说明", defaultValue = "参数默认值", required = true) @RequestParam String value) {
        return "get" + value;
    }

    @PostMapping("post/v2/{value}")
    @ApiOperation(value = "post测试 接口说明", notes = "可选-接口附加说明")
    public Object postTest(@ApiParam(value = "参数说明", defaultValue = "可选-参数默认值", required = true) @PathVariable String value) {
        return "post" + value;
    }

    @PutMapping("put/v2/{value}")
    @ApiOperation(value = "put测试 接口说明", notes = "可选-接口附加说明")
    public Object putTest(@ApiParam(value = "参数说明", defaultValue = "可选-参数默认值", required = true) @PathVariable String value) {
        return "put" + value;
    }

    @DeleteMapping("delete/v2")
    @ApiOperation(value = "delete测试 接口说明", notes = "可选-接口附加说明")
    public Object deleteTest(@ApiParam(value = "参数说明", defaultValue = "参数默认值", required = true) @RequestParam String value) {
        return "delete" + value;
    }

}
