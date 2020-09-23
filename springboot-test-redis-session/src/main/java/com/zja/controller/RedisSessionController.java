package com.zja.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Date: 2019-12-05 14:56
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@RestController
@RequestMapping(value = "rest/redis")
@Api(tags = {"RedisSessionController"}, description = "redis存session")
public class RedisSessionController {

    @RequestMapping(value = "save/session",method = {RequestMethod.GET})
    @ApiOperation(value = "数据存到session中",notes = "session自动存到redis")
    public Object redisSave(@ApiParam(value = "传入key值") @RequestParam String key,
                            @ApiParam(value = "传入value值") @RequestParam String value,
                            HttpServletRequest httpServletRequest){
        HttpSession httpSession = httpServletRequest.getSession();
        //将数据存到session中，测试session是否自动保存到redis中
        httpSession.setAttribute(key,value);
        return httpSession.getAttribute(key);
    }
}
