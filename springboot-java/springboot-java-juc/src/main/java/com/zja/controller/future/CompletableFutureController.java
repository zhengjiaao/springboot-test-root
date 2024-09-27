package com.zja.controller.future;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 接口层（一般与页面、功能对应）
 *
 * @author: zhengja
 * @since: 2024/09/24 9:14
 */
@Validated
@RestController
@RequestMapping("/rest/")
@Api(tags = {"管理页面"})
public class CompletableFutureController {

    @Autowired
    CompletableFutureService service;

    @GetMapping("/runAsync")
    @ApiOperation("runAsync-无返回值")
    public void runAsync() throws ExecutionException, InterruptedException {
        service.runAsync();
    }

    @GetMapping("/supplyAsync")
    @ApiOperation("supplyAsync-有返回值")
    public Object supplyAsync() throws ExecutionException, InterruptedException {
        return service.supplyAsync();
    }

    @GetMapping("/supplyAsync/v2")
    @ApiOperation("supplyAsync-v2-有返回值")
    public Object supplyAsyncV2() throws ExecutionException, InterruptedException {
        return service.supplyAsyncV2();
    }

    @GetMapping("/supplyAsync/v3")
    @ApiOperation("supplyAsync-v3-有返回值")
    public Object supplyAsyncV3() throws ExecutionException, InterruptedException {
        return service.supplyAsyncV3();
    }

}