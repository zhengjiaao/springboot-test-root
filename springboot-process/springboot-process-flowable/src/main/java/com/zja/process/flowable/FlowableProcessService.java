package com.zja.process.flowable;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zhengja
 * @Date: 2025-08-08 16:27
 */
//@Service
public class FlowableProcessService {

//    @Autowired
    private RuntimeService runtimeService;

    // 启动流程实例
    public ProcessInstance startSimpleProcessV1() {
        // 启动流程
        return runtimeService.startProcessInstanceByKey("simpleProcess-V1");
    }

    public ProcessInstance startUserRegistrationProcess() {
        return runtimeService.startProcessInstanceByKey("userRegistrationProcess");
    }

    public ProcessInstance startLeaveRequestProcess() {
        return runtimeService.startProcessInstanceByKey("leaveRequestProcess");
    }

    public ProcessInstance startOrderProcess() {
        return runtimeService.startProcessInstanceByKey("orderProcess");
    }
}