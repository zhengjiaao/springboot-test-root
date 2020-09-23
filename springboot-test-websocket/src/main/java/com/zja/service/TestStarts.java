package com.zja.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/4/9 10:51
 */
@Component
public class TestStarts implements InitializingBean {

    //8887
    @Value("${websocket.testPort}")
    private int testPort;

    @Bean
    public TestChatWebSocketServer webSocketChatServer() throws UnknownHostException {
        return new TestChatWebSocketServer(testPort);
    }

    @Autowired
    TestChatWebSocketServer webSocketChatServer;

    @Override
    public void afterPropertiesSet(){
        try {
            webSocketChatServer.start();
            System.out.println("备用房间已开启,等待客户端接入的端口号: " + webSocketChatServer.getPort());
        }catch (Exception e){
            System.out.println("备用房间创建异常，端口为: " + testPort);
        }
    }
}
