package com.zja.controller;

import com.zja.redis.listener.example2.RedisSendMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: zhengja
 * @since: 2019/6/11 14:05
 */
@Api(tags = {"redis 监听器"})
@RestController
@RequestMapping(value = "/redis/listener")
public class RedisListenerController {
    @Resource
    RedisSendMessage redisSendMessage; // 发布 订阅

    @ApiOperation(value = "redis-发布", notes = "redis-发布")
    @RequestMapping(value = "SendMessage", method = RequestMethod.GET)
    public void testPush(@ApiParam(value = "发布的消息内容", required = true) @RequestParam("body") String body) {
        redisSendMessage.sendMessage(body);
    }
}
