/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-16 9:29
 * @Since:
 */
package com.zja.listener.mylistener;

import com.zja.listener.event.EventListener;
import com.zja.listener.event.EventMulticaster;
import com.zja.listener.event.SimpleEventMulticaster;
import com.zja.listener.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/02/16 9:29
 */
@Configuration("eventConfig1")
public class EventConfig {

    /**
     * 注册一个事件发布者bean
     * @param eventListeners
     * @return
     */
    @Bean
    @Autowired(required = false)
    public EventMulticaster eventMulticaster(List<EventListener> eventListeners) {
        SimpleEventMulticaster simpleEventMulticaster = new SimpleEventMulticaster();
        if (eventListeners != null) {
            eventListeners.parallelStream().forEach(simpleEventMulticaster::addEventListener);
        }
        return simpleEventMulticaster;
    }

    /**
     * 用户注册服务
     * @param eventMulticaster
     * @return
     */
    @Bean
    public RegisterService registerService(EventMulticaster eventMulticaster) {
        RegisterService registerService = new RegisterService();
        registerService.setEventMulticaster(eventMulticaster);
        return registerService;
    }

 /*   @Bean
    public EventListener<RegisterSuccessEvent> successEventEventListener() {
        return new SendSMSUserRegisterSuccessListener();
    }

    @Bean
    public EventListener<RegisterSuccessEvent> eventEventListener() {
        return new GetCouponRegisterSuccessListener();
    }*/

}
