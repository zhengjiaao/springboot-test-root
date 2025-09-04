package com.zja.process.flowable.listener.parentchild;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * @Author: zhengja
 * @Date: 2025-09-04 17:52
 */
@Component("childExecutionLogger")
@Slf4j
public class ChildExecutionLogger implements ExecutionListener {
    @Override
    public void notify(DelegateExecution execution) {
        if ("start".equals(execution.getEventName())) {
            log.info("[EXEC-LISTENER] 子流程启动, parentBizKey={}, childProcId={}",
                    execution.getVariable("PARENT_BIZ_KEY"), execution.getProcessInstanceId());
        } else if ("end".equals(execution.getEventName())) {
            log.info("[EXEC-LISTENER] 子流程结束, parentBizKey={}, childProcId={}",
                    execution.getVariable("PARENT_BIZ_KEY"), execution.getProcessInstanceId());
        }
    }
}
