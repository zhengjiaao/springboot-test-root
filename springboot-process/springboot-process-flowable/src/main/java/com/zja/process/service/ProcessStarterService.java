package com.zja.process.service;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2025-09-01 14:39
 */
@Service
public class ProcessStarterService {

    private final RuntimeService runtimeService;

    public ProcessStarterService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public ProcessInstance startParentWithChildren(int childCount) {
        // 启动父流程
        ProcessInstance parent = runtimeService.startProcessInstanceByKey("parentProcess");

        // 启动多个子流程
        List<ProcessInstance> children = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            // ProcessInstance child = runtimeService.startProcessInstanceByKey("childProcess");
            // 关联父子流程
            ProcessInstance child = runtimeService.startProcessInstanceByKey("childProcess",
                    null, // businessKey
                    // parent.getId(), // businessKey 可以设置为父流程实例ID
                    java.util.Collections.singletonMap("parentId", parent.getId())); // 流程变量

            children.add(child);
        }

        System.out.printf("父流程 %s 启动，子流程数：%d%n", parent.getId(), children.size());
        return parent;
    }
}