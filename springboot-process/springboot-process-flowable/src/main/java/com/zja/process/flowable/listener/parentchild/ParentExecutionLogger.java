package com.zja.process.flowable.listener.parentchild;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * @Author: zhengja
 * @Date: 2025-09-04 17:52
 */
@Component("parentExecutionLogger")
@Slf4j
public class ParentExecutionLogger implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        if ("start".equals(execution.getEventName())) {
            log.info("[EXEC-LISTENER] 父流程启动, bizKey={}", execution.getProcessInstanceBusinessKey());
        } else if ("end".equals(execution.getEventName())) {
            log.info("[EXEC-LISTENER] 父流程结束, bizKey={}", execution.getProcessInstanceBusinessKey());
        }
    }
}