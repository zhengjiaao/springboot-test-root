package com.zja.init.spring;

import com.zja.init.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: zhengja
 * @Date: 2025-09-18 16:00
 */
@Slf4j
@Component
public class IndicatorModelMappingTableInitV2 {

    @Autowired
    private UserService userService;

    // 监听 ApplicationReadyEvent 事件，当 Spring 容器启动完成后，该方法会被调用
    @EventListener(ApplicationReadyEvent.class)
    public void loadDataAfterStartup() {
        CompletableFuture.supplyAsync(this::loadDataIntoMemory).thenAccept(result -> {
            log.info("指标模型映射表初始化完成-v2");
        }).exceptionally(throwable -> {
            log.error("初始化指标模型映射表失败，不影响系统正常启动", throwable);
            handleInitFailure();
            return null;
        });
    }

    private Object loadDataIntoMemory() {
        // 数据加载逻辑
        String userInfo = userService.getUserInfo();
        log.info("v2-用户信息：{}", userInfo);
        return new Object();
    }

    private void handleInitFailure() {
        // 处理初始化失败的情况
        String userInfo = userService.getUserInfo();
        log.info("v2-2-用户信息：{}", userInfo);
    }
}