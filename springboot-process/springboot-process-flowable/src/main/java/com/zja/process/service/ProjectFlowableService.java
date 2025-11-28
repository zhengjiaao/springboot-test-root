package com.zja.process.service;

import com.zja.process.dao.ProjectRepository;
import com.zja.process.model.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-10-23 15:01
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectFlowableService {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final ProjectRepository projectRepository;

    // 创建项目
    public Project createProject(String projectName) {
        log.info("开始创建项目: {}", projectName);

        // 创建新的项目
        Project project = new Project();
        // project.setProjectId(projectId);
        project.setProjectName(projectName);
        project.setLevel("county");
        project.setStatus("县级整改");  // 初始状态
        project.setCreateTime(LocalDateTime.now());

        // 保存项目到数据库
        project = projectRepository.save(project);

        log.info("项目创建成功，项目ID: {}, 项目名称: {}", project.getId(), projectName);
        return project;
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    /**
     * 启动流程实例，并绑定项目 ID
     */
    public Project startProjectProcess(String projectName) {
        log.info("开始启动项目流程，项目名称: {}", projectName);

        Project project = createProject(projectName);
        Long projectId = project.getId();

        // 启动流程并传递项目 ID
        Map<String, Object> variables = new HashMap<>();
        variables.put("projectId", projectId);
        variables.put("projectName", projectName);

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("projectProcess", variables);

        // 获取流程实例 ID
        String processInstanceId = processInstance.getId();

        // 查询流程启动后的第一个任务
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();

        // 如果存在任务，则记录第一个任务的ID和名称
        if (!tasks.isEmpty()) {
            Task firstTask = tasks.get(0);
            project.setTaskId(firstTask.getId());
            project.setTaskName(firstTask.getName());
        }

        // 更新项目
        project.setProcessInstanceId(processInstanceId);
        project.setUpdateTime(LocalDateTime.now());
        projectRepository.save(project);

        log.info("项目流程启动成功，项目ID: {}, 流程实例ID: {}", project.getId(), processInstanceId);
        return project;
    }

    /**
     * 完成任务并更新项目状态
     */
    public void completeTask(String taskId2, String userId, String operation, Long projectId) {
        log.info("开始处理任务完成请求，任务ID: {}, 用户ID: {}, 操作类型: {}, 项目ID: {}",
                taskId2, userId, operation, projectId);

        Project project = getProjectById(projectId);
        if (project == null) {
            log.error("项目不存在，项目ID: {}", projectId);
            return;
        }
        if (project.getProcessInstanceId() == null) {
            log.error("项目流程不存在，项目ID: {}", projectId);
            return;
        }
        if (project.getTaskId() == null) {
            log.error("项目任务不存在，项目ID: {}", projectId);
            return;
        }

        String taskId = project.getTaskId();

        // 获取当前用户级别：省、市、县
        String currentUserLevel = "市";

        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            taskService.claim(task.getId(), userId);  // 声明任务

            // 根据操作决定流程走向
            if ("reject".equals(operation)) {
                log.info("执行退回操作，将任务退回至上报环节");
                // 如果是退回操作，退回到上报环节
                runtimeService.createChangeActivityStateBuilder()
                        .processInstanceId(task.getProcessInstanceId())
                        .moveActivityIdTo(task.getTaskDefinitionKey(), "reportTask")
                        .changeState();

                // 更新项目状态为 "RETURNED"
                if (currentUserLevel.equals("省")) {
                    updateProject(projectId, "省级退回");
                } else if (currentUserLevel.equals("市")) {
                    updateProject(projectId, "市级退回");
                } else {
                    throw new IllegalArgumentException("用户级别错误");
                }
                log.info("任务退回处理完成，项目ID: {} 状态已更新", projectId);
            } else if ("pass".equals(operation)) {
                log.info("执行通过操作，任务名称: {}", task.getName());
                // 正常完成任务，判断当前任务并更新项目状态
                if ("审查".equals(task.getName())) {
                    updateProject(projectId, "市级审查");
                    log.info("审查任务完成，项目ID: {} 状态已更新为 市级审查", projectId);
                } else if ("审核".equals(task.getName())) {
                    updateProject(projectId, "省级审核");
                    log.info("审核任务完成，项目ID: {} 状态已更新为 省级审核", projectId);
                }
                taskService.complete(task.getId());
                log.info("任务完成处理完毕，任务ID: {}", taskId);
            } else if ("end".equals(operation)) {
                log.info("执行结束操作");
                // 如果是结束操作，流程结束
                updateProject(projectId, "审核通过");
                taskService.complete(task.getId());
                log.info("流程结束操作完成，项目ID: {} 状态已更新为 审核通过", projectId);
            } else {
                taskService.complete(task.getId());
                log.info("默认操作完成任务，任务ID: {}", taskId);
            }
        } else {
            log.warn("未找到指定任务，任务ID: {}", taskId);
        }
    }

    /**
     * 更新项目状态和任务信息
     */
    private void updateProject(Long projectId, String newStatus) {
        log.debug("开始更新项目状态，项目ID: {}, 新状态: {}", projectId, newStatus);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> {
                    log.error("项目不存在，项目ID: {}", projectId);
                    return new RuntimeException("项目不存在");
                });

        project.setStatus(newStatus);
        project.setUpdateTime(LocalDateTime.now());

        // 查询当前流程实例的最新任务信息
        if (project.getProcessInstanceId() != null) {
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(project.getProcessInstanceId())
                    .list();

            if (!tasks.isEmpty()) {
                Task currentTask = tasks.get(0);
                project.setTaskId(currentTask.getId());
                project.setTaskName(currentTask.getName());
                // 可以根据需要设置其他任务相关信息
            } else {
                // 如果没有任务，说明流程已结束，依旧存储最后一个环节的任务名称
                // project.setTaskId(null);
                // project.setTaskName(null);
            }
        }

        projectRepository.save(project);  // 保存更新后的项目

        log.debug("项目状态更新成功，项目ID: {}, 新状态: {}", projectId, newStatus);
    }
}
