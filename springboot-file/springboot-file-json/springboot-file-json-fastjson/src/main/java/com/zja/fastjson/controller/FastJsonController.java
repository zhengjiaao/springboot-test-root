package com.zja.fastjson.controller;

import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author: zhengja
 * @since: 2024/01/19 15:29
 */
@RestController
@RequestMapping(value = "rest/fastjson")
public class FastJsonController {

    @ApiOperation(value = "测试 fastjson 序列化格式")
    @GetMapping(value = "v1/get")
    public Object get() {
        User user = new User();
        user.setName("李四");
        user.setAge(18);
        user.setCreatDate(new Date());

        return user;
    }

    @Data
    static class User {
        private String name;
        private int age;
        private Date creatDate;
    }

}
