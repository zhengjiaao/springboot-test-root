package com.zja.controller;

import com.zja.util.RedisUtil;
import com.zja.util.RedisUtil2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/29 10:41
 */
@Api(tags = {"RedisController"},description = "redis双数据源测试")
@RequestMapping(value = "rest/redis")
@RestController
@Slf4j
public class RedisController {

    /**
     * 封装的工具类：默认 127.0.0.1
     */
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedisUtil2 redisUtil2;

    @ApiOperation(value = "redis数据源1",httpMethod = "GET")
    @RequestMapping(value = "v1/redis1",method = RequestMethod.GET)
    public Object redis1(@ApiParam(value = "reids key值") @RequestParam String key,
                         @ApiParam(value = "redis value值") @RequestParam String value){
        boolean set = redisUtil.set(key, value);
        log.info("保存结果："+set);
        if (set){
            return redisUtil.get(key);
        }
        return null;
    }

    @ApiOperation(value = "redis数据源2",httpMethod = "GET")
    @RequestMapping(value = "v2/redis2",method = RequestMethod.GET)
    public Object redis2(@ApiParam(value = "reids key值") @RequestParam String key,
                         @ApiParam(value = "redis value值") @RequestParam String value){
        boolean set = redisUtil2.set(key, value);
        log.info("保存结果："+set);
        if (set){
            return redisUtil2.get(key);
        }
        return null;
    }

}
