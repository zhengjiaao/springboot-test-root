package com.zja.process.controller.parentchild;

import com.zja.process.service.parentchild.v2.ParentChildSyncServiceV2;
import org.flowable.engine.HistoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询父任务、子任务等任务完成明细（包括办理人、完成时间、状态等）
 *
 * @Author: zhengja
 * @Date: 2025-09-01 15:26
 */
@RestController
@RequestMapping("/history/tasks")
public class HistoryTaskQueryController {

    @Autowired
    private HistoryService historyService;

    private static final String PARENT_PROCESS_KEY = "parentProcessV2";
    private static final String CHILD_PROCESS_KEY = "childProcessV2";

    /**
     * 查询父流程的任务明细（含已完成、未完成）
     */
    @GetMapping("/parent/{parentBizKey}")
    public List<Map<String, Object>> getParentTaskHistory(@PathVariable String parentBizKey
                                                         /* @RequestParam(required = false) String parentId*/) {
        return historyService.createHistoricTaskInstanceQuery()
                // .processInstanceId(parentId)
                .processInstanceBusinessKey(parentBizKey)
                .processDefinitionKey(PARENT_PROCESS_KEY)
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list()
                .stream()
                .map(task -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("taskId", task.getId());
                    map.put("taskName", task.getName());
                    map.put("assignee", task.getAssignee());
                    map.put("startTime", task.getCreateTime());
                    map.put("endTime", task.getEndTime());
                    map.put("status", task.getEndTime() == null ? "未完成" : "已完成");
                    return map;
                }).collect(Collectors.toList());
    }

    /**
     * 查询子流程任务明细（含已完成、未完成），根据父流程ID
     */
    @GetMapping("/parent/{parentBizKey}/children")
    public List<Map<String, Object>> getChildTaskHistory(@PathVariable String parentBizKey) {
        // 找到所有子流程实例，假设子流程启动时都设置了 parentId 变量（父流程 ID）
        List<HistoricProcessInstance> children =
                historyService.createHistoricProcessInstanceQuery()
                        .processDefinitionKey(CHILD_PROCESS_KEY)
                        // .variableValueEquals("parentId", parentId)
                        .variableValueEquals(ParentChildSyncServiceV2.VAR_PARENT_BIZ_KEY, parentBizKey)
                        .list();

        if (children.isEmpty()) {
            return new ArrayList<>();
        }

        // 查询所有子流程的任务历史
        return historyService.createHistoricTaskInstanceQuery()
                .processInstanceIdIn(Arrays.asList(children.stream().map(HistoricProcessInstance::getId).toArray(String[]::new)))
                .orderByHistoricTaskInstanceStartTime()
                .asc()
                .list()
                .stream()
                .map(task -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("taskId", task.getId());
                    map.put("taskName", task.getName());
                    map.put("assignee", task.getAssignee());
                    map.put("processInstanceId", task.getProcessInstanceId());
                    map.put("startTime", task.getCreateTime());
                    map.put("endTime", task.getEndTime());
                    map.put("status", task.getEndTime() == null ? "未完成" : "已完成");
                    return map;
                }).collect(Collectors.toList());
    }
}
