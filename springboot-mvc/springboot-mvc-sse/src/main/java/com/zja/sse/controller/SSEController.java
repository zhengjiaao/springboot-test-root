package com.zja.sse.controller;

import com.zja.sse.service.SSEService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 消息 接口层（一般与页面、功能对应）
 *
 * @author: zhengja
 * @since: 2025/02/27 16:52
 */
@Validated
@RestController
@RequestMapping("/sse")
@Api(tags = {"SSE 消息管理页面"})
public class SSEController {

    @Autowired
    SSEService service;

    @GetMapping(value = "/handleSse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ApiOperation("SSE 连接接口")
    public SseEmitter handleSse() {
        return service.handleSse();
    }

    @PostMapping("/sendMessage")
    @ApiOperation("发送消息接口")
    public String sendMessage(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        return service.sendMessage(message);
    }

}