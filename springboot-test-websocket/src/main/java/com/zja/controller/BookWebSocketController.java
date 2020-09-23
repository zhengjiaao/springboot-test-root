package com.zja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class BookWebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    //广播推送消息
    //@Scheduled(fixedRate = 100000)
    public void sendTopicMessage() {
        System.out.println("后台广播推送！");
        String msg = "这是一条消息";
        this.template.convertAndSend("/topic/getResponse",msg);
    }

    //一对一推送消息
    //@Scheduled(fixedRate = 200000)
    public void sendQueueMessage() {
        System.out.println("后台一对一推送！");
        String msg = "这是一条消息2";
        //消息接收人
        String user = "1";
        this.template.convertAndSendToUser( user,"/queue/getData",msg);
    }

    //客户端主动发送消息到服务端，服务端马上回应指定的客户端消息
    //类似http无状态请求，但是有质的区别
    //websocket可以从服务器指定发送哪个客户端，而不像http只能响应请求端

    //群聊：方法用于群发测试
    @MessageMapping("/massRequest")
    //SendTo 发送至 Broker 下的指定订阅路径
    @SendTo("/mass/getResponse")
    public String mass(String chatRoomRequest){
        return chatRoomRequest;
    }

    //单独聊天 :方法用于一对一测试
    @MessageMapping("/aloneRequest")
    public String alone(String chatRoomRequest){
        //消息接收人
        String user = "1";
        String msg = "这是一条消息3";
        this.template.convertAndSendToUser(user,"/alone/getResponse",msg);
        return chatRoomRequest;
    }

}
