package com.zja.process.service;

import com.zja.process.ProcessFlowableApplicationTests;
import com.zja.process.model.Project;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: zhengja
 * @Date: 2025-10-23 15:09
 */
public class ProjectFlowableServiceTest extends ProcessFlowableApplicationTests {

    @Autowired
    private ProjectFlowableService projectFlowableService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    private String projectName;

    @BeforeEach
    void setUp() {
        projectName = "测试项目_" + System.currentTimeMillis();
    }

    @Test
    void testCreateProject() {
        // When
        Project project = projectFlowableService.createProject(projectName);

        // Then
        assertNotNull(project);
        assertNotNull(project.getId());
        assertEquals(projectName, project.getProjectName());
        assertEquals("县级整改", project.getStatus());
        assertNotNull(project.getCreateTime());
        assertNull(project.getProcessInstanceId());
    }

    @Test
    void testStartProjectProcess() {
        // When
        Project project = projectFlowableService.startProjectProcess(projectName);

        // Then
        assertNotNull(project);
        assertNotNull(project.getId());
        assertEquals(projectName, project.getProjectName());
        assertEquals("县级整改", project.getStatus());
        assertNotNull(project.getCreateTime());
        assertNotNull(project.getProcessInstanceId());
        assertNotNull(project.getUpdateTime());

        // 验证流程实例已创建
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(project.getProcessInstanceId())
                .singleResult();
        assertNotNull(processInstance);
    }

    @Test
    void testCompleteTask_PassOperation() {
        // Given: 启动流程
        Project project = projectFlowableService.startProjectProcess(projectName);

        // 查询当前任务
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(project.getProcessInstanceId())
                .list();

        assertFalse(tasks.isEmpty());
        Task task = tasks.get(0);

        // When: 完成任务
        projectFlowableService.completeTask(
                task.getId(),
                "testUser",
                "pass",
                project.getId()
        );

        // Then: 验证任务已完成，项目状态已更新
        Project updatedProject = projectFlowableService.getProjectById(project.getId());
        assertNotNull(updatedProject);
        // 根据任务名称更新状态，初始任务通常是"审查"
        assertEquals("REVIEWED", updatedProject.getStatus());
    }

    @Test
    void testCompleteTask_EndOperation() {
        // Given: 启动流程
        Project project = projectFlowableService.startProjectProcess(projectName);

        // 查询当前任务
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(project.getProcessInstanceId())
                .list();

        assertFalse(tasks.isEmpty());
        Task task = tasks.get(0);

        // When: 结束任务
        projectFlowableService.completeTask(
                task.getId(),
                "testUser",
                "end",
                project.getId()
        );

        // Then: 验证项目状态已更新为"审核通过"
        Project updatedProject = projectFlowableService.getProjectById(project.getId());
        assertNotNull(updatedProject);
        assertEquals("审核通过", updatedProject.getStatus());
    }

    @Test
    void testCompleteTask_RejectOperation() {
        // Given: 启动流程
        Project project = projectFlowableService.startProjectProcess(projectName);

        // 查询当前任务
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(project.getProcessInstanceId())
                .list();

        assertFalse(tasks.isEmpty());
        Task task = tasks.get(0);

        // When: 退回任务
        projectFlowableService.completeTask(
                task.getId(),
                "testUser",
                "reject",
                project.getId()
        );

        // Then: 验证项目状态已更新为"省级退回"
        Project updatedProject = projectFlowableService.getProjectById(project.getId());
        assertNotNull(updatedProject);
        assertEquals("省级退回", updatedProject.getStatus());
    }
}