package com.zja.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/4/15 14:15
 */
@Component
public class AppStarts implements InitializingBean {

    //8989
    @Value("${websocket.appPort}")
    private int appPort;

    @Bean
    public AppVersionWebSocketServer webSocketAppVersionServer() throws UnknownHostException {
        return new AppVersionWebSocketServer(appPort);
    }

    @Autowired
    AppVersionWebSocketServer webSocketAppVersionServer;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            webSocketAppVersionServer.start();
            System.out.println("App版本推送房间已开启,等待客户端接入的端口号: " + webSocketAppVersionServer.getPort());
        }catch (Exception e){
            System.out.println("App版本推房间创建异常，端口为: " + appPort);
        }
    }
}
