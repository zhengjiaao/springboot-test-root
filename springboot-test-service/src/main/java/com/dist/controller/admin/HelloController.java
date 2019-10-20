package com.dist.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: springboot
 * @Date: 2018/12/25 16:17
 * @Author: Mr.Zheng
 * @Description:
 */
@RestController  //返回的是json数据格式
@RequestMapping("/hello")
@Api(tags = {"DEMO-HelloController"}, description = "hello测试")
public class HelloController {

    /**
     * 启动：SpringbootApplication
     * 访问地址：http://127.0.0.1:8080/hello
     * @return
     */
    @ApiOperation(value = "hello-测试", notes = "hello-测试")
    @RequestMapping(value = "v1/get/hello", method = RequestMethod.GET)
    public String hello(){
        return "Hello Spring Boot1";
    }

}
