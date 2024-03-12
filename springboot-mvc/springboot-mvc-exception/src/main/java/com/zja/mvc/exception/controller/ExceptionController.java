package com.zja.mvc.exception.controller;

import com.zja.mvc.exception.exception.TestException;
import com.zja.mvc.exception.service.ExceptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 异常模拟
 *
 * @author: zhengja
 * @since: 2024/02/01 9:55
 */
@Validated
@RestController
@RequestMapping("/exception")
@Api(tags = {"ExceptionController"}, description = "异常测试模块")
public class ExceptionController {

    final ExceptionService exceptionService;

    public ExceptionController(ExceptionService exceptionService) {
        this.exceptionService = exceptionService;
    }

    @GetMapping("get/v1")
    @ApiOperation(value = "异常接口-数据异常")
    public String v1(@ApiParam(value = "用户名称", defaultValue = "李四", required = true) @RequestParam String name) {
        System.out.println("name=" + name);
        return exceptionService.getUserName(name);
    }

    @GetMapping("get/v2")
    @ApiOperation(value = "异常接口-业务处理异常")
    public void v2() {
        exceptionService.loginVerify();
    }

    @GetMapping("get/v3")
    @ApiOperation(value = "异常接口-继承 Exception 抛出异常，需要手动抛出异常")
    public void v3() throws TestException {
        exceptionService.testException();
    }

    @GetMapping("get/v4")
    @ApiOperation(value = "异常接口-继承 RuntimeException 抛出异常，不需要手动抛出异常")
    public void v4() {
        exceptionService.testRuntimeException();
    }

    @GetMapping("get/v5")
    @ApiOperation(value = "异常接口-异常日志记录：默认异常是不会输出到日志文件中")
    public void v5() {
        exceptionService.testRuntimeExceptionLog();
    }

}
