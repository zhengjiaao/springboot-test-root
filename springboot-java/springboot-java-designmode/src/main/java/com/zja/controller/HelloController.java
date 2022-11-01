package com.zja.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-23 10:18
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc： Hello测试
 */
@Controller
@RequestMapping("rest/dist")
@Api(tags = {"Test-HelloController"}, description = "Hello 页面跳转测试")
public class HelloController {

    @RequestMapping(value = "html/index/v1", method = RequestMethod.GET)
    @ApiOperation(value = "浏览器访问-跳转到index页面", notes = "必须用@Controller 不能用@RestController", httpMethod = "GET")
    public ModelAndView index() {
        //跳转到index.html页面
        return new ModelAndView("index");
    }

    @RequestMapping(value = "html/hello/v1", method = RequestMethod.GET)
    @ApiOperation(value = "浏览器访问-跳转到hello页面", notes = "必须用@Controller 不能用@RestController", httpMethod = "GET")
    public String hello() {
        //跳转到hello.html页面
        return "hello";
    }

    @ResponseBody
    @ApiOperation(value = "返回string数据", notes = "测试")
    @RequestMapping(value = "v1/get/json/string", method = RequestMethod.GET)
    public String str() {
        return "Hello Spring Boot1";
    }

    @ResponseBody
    @ApiOperation(value = "返回Object数据", notes = "测试")
    @RequestMapping(value = "v1/get/json/object", method = RequestMethod.GET)
    public Object obj() {
        return "Hello Spring Boot2";
    }

}
