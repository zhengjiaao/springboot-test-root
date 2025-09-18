package com.zja.init.spring;

import com.zja.init.service.UserService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: zhengja
 * @Date: 2025-09-18 15:57
 */
@Getter
@Slf4j
@Component
public class IndicatorModelMappingTableInitV4 {

    private volatile boolean initialized = false;
    private volatile boolean fallbackUsed = false;

    @Autowired
    private UserService userService;

    // 监听 ApplicationReadyEvent 事件，当 Spring 容器启动完成后，该方法会被调用
    @EventListener(ApplicationReadyEvent.class)
    public void loadDataAfterStartup() {
        CompletableFuture.runAsync(() -> {
            try {
                loadDataIntoMemory();
                initialized = true;
                log.info("指标模型映射表初始化完成-v4");
            } catch (Exception e) {
                log.error("初始化指标模型映射表失败，使用降级方案", e);
                try {
                    loadFallbackData();
                    fallbackUsed = true;
                    initialized = true;
                    log.info("使用降级数据初始化完成");
                } catch (Exception fallbackException) {
                    log.error("降级方案也失败了，系统将以有限功能运行", fallbackException);
                }
            }
        });
    }

    private void loadDataIntoMemory() {
        // 主要数据加载逻辑
        String userInfo = userService.getUserInfo();
        log.info("v4-用户信息：{}", userInfo);
    }

    private void loadFallbackData() {
        // 降级数据加载逻辑（如加载默认配置或缓存数据）
        String userInfo = userService.getUserInfo();
        log.info("v4-2-用户信息：{}", userInfo);
    }

}