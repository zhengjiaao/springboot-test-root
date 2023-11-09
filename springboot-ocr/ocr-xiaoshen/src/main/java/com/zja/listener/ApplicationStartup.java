/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-09 10:18
 * @Since:
 */
package com.zja.listener;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author: zhengja
 * @since: 2023/11/09 10:18
 */
@Component
class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final WebServerApplicationContext applicationContext;

    public ApplicationStartup(WebServerApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            int port = applicationContext.getWebServer().getPort();

            System.out.println("应用已启动，访问地址：http://" + hostAddress + ":" + port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}