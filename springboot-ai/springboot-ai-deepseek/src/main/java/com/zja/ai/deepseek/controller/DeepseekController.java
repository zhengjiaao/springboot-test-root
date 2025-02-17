package com.zja.ai.deepseek.controller;

import com.zja.ai.deepseek.service.DeepseekApiClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 接口层（一般与页面、功能对应）
 *
 * @author: zhengja
 * @since: 2025/02/17 15:58
 */
@Validated
@RestController
@RequestMapping("/rest/deepseek/")
@Api(tags = {"deepseek对话管理页面"})
public class DeepseekController {

    @Autowired
    private DeepseekApiClient deepseekApiClient;

    @GetMapping("/chat")
    @ApiOperation("智能助手-乐于帮助您的助手")
    public String queryById(@RequestParam("message") String message) {
        return deepseekApiClient.sendMessageToDeepseek(message);
    }

}