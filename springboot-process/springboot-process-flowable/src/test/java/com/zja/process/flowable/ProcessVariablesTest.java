package com.zja.process.flowable;

import com.zja.process.ProcessFlowableApplicationTests;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-09-01 20:42
 */
public class ProcessVariablesTest extends ProcessFlowableApplicationTests {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    static final String processInstanceId = "92fdb871-872b-11f0-90b7-8245ddc034b5";


    // 设置流程变量(在 Flowable 中设置流程变量有多种方式)
    @Test
    public void testSetProcessVariables() {
        // 1. 启动流程时设置变量
        Map<String, Object> startVariables = new HashMap<>();
        startVariables.put("applicant", "John Doe");
        startVariables.put("amount", 1000);
        startVariables.put("priority", "high");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("parentProcessV2", startVariables);
        String processInstanceId = processInstance.getId();

        // 2. 运行时设置变量
        runtimeService.setVariable(processInstanceId, "status", "inProgress");

        // 3. 批量设置变量
        Map<String, Object> runtimeVariables = new HashMap<>();
        runtimeVariables.put("reviewer", "Jane Smith");
        runtimeVariables.put("reviewDate", new Date());
        runtimeService.setVariables(processInstanceId, runtimeVariables);

        // 4. 通过任务设置变量
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (task != null) {
            Map<String, Object> taskVariables = new HashMap<>();
            taskVariables.put("taskStatus", "completed");
            taskVariables.put("completionTime", new Date());

            // 完成任务并设置变量
            taskService.complete(task.getId(), taskVariables);
        }

        // 5. 验证变量设置
        Map<String, Object> allVariables = getHistoricVariables(processInstanceId);
        System.out.println("所有流程变量: " + allVariables);
    }


    /**
     * 获取某个流程实例的所有历史变量
     */
    @Test
    public void testGetHistoricVariablesByProcessInstanceId() {
        Map<String, Object> variables = getHistoricVariables(processInstanceId);
        System.out.println(variables);
    }

    public Map<String, Object> getHistoricVariables(String processInstanceId) {
        List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        Map<String, Object> map = new HashMap<>();
        for (HistoricVariableInstance var : variables) {
            map.put(var.getVariableName(), var.getValue());
        }
        return map;
    }



}
