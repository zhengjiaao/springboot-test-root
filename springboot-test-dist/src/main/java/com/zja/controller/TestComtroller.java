package com.zja.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Date: 2019-11-22 14:43
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RestController
@RequestMapping("rest/test")
@Api(tags = {"TestComtroller"}, description = "测试")
public class TestComtroller {

    @GetMapping("get/test")
    public Object test(){
        return "成功";
    }
}
