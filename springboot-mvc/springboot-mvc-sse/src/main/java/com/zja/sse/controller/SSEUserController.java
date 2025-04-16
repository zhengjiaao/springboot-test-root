package com.zja.sse.controller;

import com.zja.sse.service.SSEUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Author: zhengja
 * @Date: 2025-02-27 17:26
 */
@Validated
@RestController
@RequestMapping("/sse/user")
@Api(tags = {"用户消息管理页面"})
public class SSEUserController {

    @Autowired
    SSEUserService service;

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperation("SSE 连接接口")
    public SseEmitter handleSse(@RequestParam String userId) {
        return service.handleSse(userId);
    }

    @PostMapping("/send")
    @ApiOperation("发送消息接口")
    public String sendMessage(@RequestParam String targetUserId, @RequestParam String message) {
        return service.sendMessage(targetUserId, message);
    }
}
