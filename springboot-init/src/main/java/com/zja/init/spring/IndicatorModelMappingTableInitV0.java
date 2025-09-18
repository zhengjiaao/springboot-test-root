package com.zja.init.spring;

import com.zja.init.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author: zhengja
 * @Date: 2025-09-18 16:01
 */
@Slf4j
@Component
public class IndicatorModelMappingTableInitV0 {

    @Autowired
    private UserService userService;

    // 监听 ApplicationReadyEvent 事件，当 Spring 容器启动完成后，该方法会被调用
    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void asyncLoadData() {
        try {
            loadDataIntoMemory();
            log.info("指标模型映射表初始化完成-v0");
        } catch (Exception e) {
            log.error("初始化指标模型映射表失败，不影响系统正常启动", e);
            // 可以设置默认值或标记初始化失败状态
            handleInitFailure();
        }
    }

    private void loadDataIntoMemory() {
        // 数据加载逻辑
        String userInfo = userService.getUserInfo();
        log.info("v0-用户信息：{}", userInfo);
    }

    private void handleInitFailure() {
        // 处理初始化失败的情况，如设置默认值或标记
    }
}