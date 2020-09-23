package com.zja.controller;

import com.zja.dto.PushObjectDto;
import com.zja.utils.AppWebSocketUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.java_websocket.WebSocket;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 *推送服务模块
 */
@RestController
@RequestMapping(value = "app/webSocket")
@Api(tags = {"WebSocketController"}, description = "推送服务")
public class AppWebSockeController {

    @ApiOperation(value = "Http触发-推送消息")
    @RequestMapping(value = "v1/http/push", method = RequestMethod.POST)
    public Object httpsendMessage(@RequestBody @ApiParam(value = "推送的版本信息", required = true)PushObjectDto pushObjectDto) throws Exception {

        WebSocket webSocket = AppWebSocketUtil.getWebSocketByDevice(pushObjectDto.getReceiver());
            if (null != webSocket) {
                //处理接收人在线情况
                AppWebSocketUtil.sendMessageToOnlineDevice(webSocket, pushObjectDto.getPushMessage());
            }else {
                //处理接收人不在线情况 -消息存redis
                return "推送成功-接收人不在线";
            }
        String result = "推送成功-消息："+pushObjectDto.getPushMessage();
        return result;
    }

    @ApiOperation(value = "Http触发-获取所有当前在线的人")
    @RequestMapping(value = "v1/getall", method = RequestMethod.GET)
    public Object getAllUser(){

        Collection<String> allDevices = AppWebSocketUtil.getAllDevices();
        if (null != allDevices) {
            return allDevices;
        }
        return "没有在线的人";
    }
}
