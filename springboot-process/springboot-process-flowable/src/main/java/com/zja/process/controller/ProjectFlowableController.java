package com.zja.process.controller;

import com.zja.process.model.Project;
import com.zja.process.service.ProjectFlowableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhengja
 * @Date: 2025-10-23 15:02
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectFlowableController {

    private final ProjectFlowableService projectFlowableService;

    // 流程启动成功，项目与流程已绑定
    @PostMapping("/start")
    public Project start(@RequestParam String projectName) {
        return projectFlowableService.startProjectProcess(projectName);
    }

    @PostMapping("/complete/{taskId}")
    public String complete(@PathVariable String taskId, @RequestParam String userId, @RequestParam String operation, @RequestParam Long projectId) {
        projectFlowableService.completeTask(taskId, userId, operation, projectId);
        return "任务处理完成";
    }
}

