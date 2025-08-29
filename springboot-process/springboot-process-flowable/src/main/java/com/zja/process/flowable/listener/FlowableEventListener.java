package com.zja.process.flowable.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Author: zhengja
 * @Date: 2025-08-13 14:53
 */
@Component
public class FlowableEventListener {
    // 监听流程结束事件
//    @EventListener
//    public void onProcessCompleted(FlowableProcessEndedEvent event) {
//        String processId = event.getProcessInstanceId();
//        auditService.logProcessCompletion(processId);  // 自定义审计逻辑
//    }
}