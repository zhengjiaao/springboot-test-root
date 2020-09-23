package com.zja.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("rest/dist")
@Api(tags = {"HelloController"}, description = "Springboot首次使用测试")
public class HelloController {

    @Autowired
    private Mapper mapper;

    @ApiOperation(value = "返回html页面",notes = "返回页面必须用@Controller 不能用@RestController",httpMethod = "GET")
    @RequestMapping(value = "v1/html/hello",method = RequestMethod.GET)
    public String hello(){
        System.out.println("跳转进入index.html页面");
        //return new ModelAndView("index");
        return "hello";
    }

    @ResponseBody
    @ApiOperation(value = "返回string数据", notes = "测试")
    @RequestMapping(value = "v1/get/json/string", method = RequestMethod.GET)
    public String str(){
        return "Hello Spring Boot1";
    }

    @ResponseBody
    @ApiOperation(value = "返回Object数据", notes = "测试")
    @RequestMapping(value = "v1/get/json/object", method = RequestMethod.GET)
    public Object obj(){
        return "Hello Spring Boot2";
    }

}
