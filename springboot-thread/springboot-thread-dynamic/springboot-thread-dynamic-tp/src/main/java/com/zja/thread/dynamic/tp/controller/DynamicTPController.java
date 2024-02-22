package com.zja.thread.dynamic.tp.controller;

import com.zja.thread.dynamic.tp.service.DynamicTPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author: zhengja
 * @since: 2024/02/22 13:34
 */
@Validated
@RestController
@RequestMapping("/rest/dynamic/tp")
@Api(tags = {"动态线程池测试实例页面"})
public class DynamicTPController {

    @Autowired
    DynamicTPService service;

    @Value("${name}")
    public String name;

    @GetMapping("/v1/jucThreadPoolExecutor/taskExample")
    @ApiOperation("动态线程池测试实例")
    public Object v1() throws InterruptedException {
        service.jucThreadPoolExecutor_taskExample();
        System.out.println(name);
        return "success";
    }

    @GetMapping("/v2/threadPoolTaskExecutor/taskExample")
    @ApiOperation("动态线程池测试实例")
    public Object v2() throws InterruptedException {
        service.threadPoolTaskExecutor_taskExample();
        return "success";
    }

    @GetMapping("/v3/dtpExecutor0_taskExample/taskExample")
    @ApiOperation("动态线程池测试实例")
    public Object v3() throws InterruptedException {
        service.dtpExecutor0_taskExample();
        return "success";
    }

    @GetMapping("/v4/dtpExecutor1_taskExample/taskExample")
    @ApiOperation("动态线程池测试实例")
    public Object v4() throws InterruptedException {
        service.dtpExecutor1_taskExample();
        return "success";
    }

    @GetMapping("/v5/eagerDtpExecutor_taskExample/taskExample")
    @ApiOperation("动态线程池测试实例")
    public Object v5() throws InterruptedException {
        service.eagerDtpExecutor_taskExample();
        return "success";
    }

    @GetMapping("/v6/orderedDtpExecutor_taskExample/taskExample")
    @ApiOperation("动态线程池测试实例")
    public Object v6() throws InterruptedException {
        service.orderedDtpExecutor_taskExample();
        return "success";
    }

    @GetMapping("/v7/scheduledDtpExecutor_taskExample/taskExample")
    @ApiOperation("动态线程池测试实例")
    public Object v7() throws InterruptedException {
        service.scheduledDtpExecutor_taskExample();
        return "success";
    }

}