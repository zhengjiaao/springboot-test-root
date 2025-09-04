package com.zja.process.controller.parentchild;

import com.zja.process.service.parentchild.v2.ParentChildSyncServiceV2;
import org.flowable.engine.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 已完成的子流程（历史查询）
 *
 * @Author: zhengja
 * @Date: 2025-09-01 15:19
 */
@RestController
@RequestMapping("/history")
public class HistoryQueryController {

    @Autowired
    private HistoryService historyService;

    private static final String PARENT_PROCESS_KEY = "parentProcessV2";
    private static final String CHILD_PROCESS_KEY = "childProcessV2";

    /**
     * 查询所有父流程（含已完成）
     */
    @GetMapping("/parent/all")
    public List<Map<String, Object>> getAllParents() {
        return getAllProcess(PARENT_PROCESS_KEY);
    }

    /**
     * 查询所有子流程（含已完成）
     */
    @GetMapping("/child/all")
    public List<Map<String, Object>> getAllChild() {
        return getAllProcess(CHILD_PROCESS_KEY);
    }

    private List<Map<String, Object>> getAllProcess(String processKey) {
        return historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processKey)
                .orderByProcessInstanceStartTime()
                .desc()
                .list()
                .stream()
                .map(pi -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", pi.getId());
                    map.put("businessKey", pi.getBusinessKey());
                    map.put("startTime", pi.getStartTime());
                    map.put("endTime", pi.getEndTime());
                    map.put("status", pi.getEndTime() == null ? "运行中" : "已完成");
                    return map;
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据父流程 parentBizKey 查询子流程（含已完成）
     */
    @GetMapping("/{parentBizKey}/children")
    public List<Map<String, Object>> getAllChildren(@PathVariable String parentBizKey) {
        return historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(CHILD_PROCESS_KEY)
                .variableValueEquals(ParentChildSyncServiceV2.VAR_PARENT_BIZ_KEY, parentBizKey)
                .orderByProcessInstanceStartTime()
                .asc()
                .list()
                .stream()
                .map(pi -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", pi.getId());
                    map.put("businessKey", pi.getBusinessKey());
                    map.put("startTime", pi.getStartTime());
                    map.put("endTime", pi.getEndTime());
                    map.put("status", pi.getEndTime() == null ? "运行中" : "已完成");
                    return map;
                })
                .collect(Collectors.toList());
    }

    /**
     * 统计子流程数量（含已完成）
     */
    @GetMapping("/{parentBizKey}/children/count")
    public long countAllChildren(@PathVariable String parentBizKey) {
        return historyService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(CHILD_PROCESS_KEY)
                .variableValueEquals(ParentChildSyncServiceV2.VAR_PARENT_BIZ_KEY, parentBizKey)
                .count();
    }
}
