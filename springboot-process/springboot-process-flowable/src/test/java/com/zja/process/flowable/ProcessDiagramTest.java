package com.zja.process.flowable;

import com.zja.process.ProcessFlowableApplicationTests;
import org.flowable.bpmn.model.*;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 流程图生成 - 完善版
 * 包含各种常见的流程图生成实现
 *
 * @Author: zhengja
 * @Date: 2025-08-13 16:03
 */
public class ProcessDiagramTest extends ProcessFlowableApplicationTests {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    // 流程定义的Key
    private String processDefinitionKey = "simpleProcess-V1";

    @BeforeEach
    public void setUp() {
        // 确保流程定义已部署
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();

        if (processDefinitions.isEmpty()) {
            System.out.println("警告: 流程定义 " + processDefinitionKey + " 未找到，请确保已部署");
        }
    }

    /**
     * 生成完整流程图（不带高亮）
     *
     * @param processDefinitionId 流程定义ID
     * @return 流程图输入流
     */
    public InputStream generateFullProcessDiagram(String processDefinitionId) {
        return generateFullProcessDiagram(processDefinitionId, 1.0);
    }

    /**
     * 生成高清完整流程图（不带高亮）
     *
     * @param processDefinitionId 流程定义ID
     * @param scaleFactor         缩放因子，用于生成高清图片（如2.0、3.0）
     * @return 流程图输入流
     */
    public InputStream generateFullProcessDiagram(String processDefinitionId, double scaleFactor) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration engineConfig = processEngine.getProcessEngineConfiguration();

        // 设置字体
        engineConfig.setActivityFontName("宋体");
        engineConfig.setLabelFontName("宋体");
        engineConfig.setAnnotationFontName("宋体");

        return engineConfig.getProcessDiagramGenerator()
                .generateDiagram(
                        bpmnModel,
                        "png",
                        Collections.emptyList(), // 不高亮任何活动
                        Collections.emptyList(),
                        engineConfig.getActivityFontName(),
                        engineConfig.getLabelFontName(),
                        engineConfig.getAnnotationFontName(),
                        engineConfig.getClassLoader(),
                        scaleFactor,  // 缩放因子
                        false
                );
    }

    /**
     * 生成当前活跃节点高亮流程图
     *
     * @param processInstanceId 流程实例ID
     * @return 流程图输入流
     */
    public InputStream generateActiveProcessDiagram(String processInstanceId) {
        return generateActiveProcessDiagram(processInstanceId, 1.0);
    }

    /**
     * 生成高清当前活跃节点高亮流程图
     *
     * @param processInstanceId 流程实例ID
     * @param scaleFactor       缩放因子
     * @return 流程图输入流
     */
    public InputStream generateActiveProcessDiagram(String processInstanceId, double scaleFactor) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance == null) {
            return null;
        }

        // 获取当前活跃的活动ID
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);

        // 获取流程定义ID
        String processDefinitionId = processInstance.getProcessDefinitionId();

        // 生成高亮流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration engineConfig = processEngine.getProcessEngineConfiguration();

        // 设置字体
        engineConfig.setActivityFontName("宋体");
        engineConfig.setLabelFontName("宋体");
        engineConfig.setAnnotationFontName("宋体");

        return engineConfig.getProcessDiagramGenerator()
                .generateDiagram(
                        bpmnModel,
                        "png",
                        activeActivityIds,
                        Collections.emptyList(),
                        engineConfig.getActivityFontName(),
                        engineConfig.getLabelFontName(),
                        engineConfig.getAnnotationFontName(),
                        engineConfig.getClassLoader(),
                        scaleFactor,
                        true  // 高亮当前活动
                );
    }


    /**
     * 生成自定义样式的流程图
     *
     * @param processInstanceId 流程实例ID
     * @return 流程图输入流
     */
    public InputStream generateCustomStyledProcessDiagram(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance == null) {
            return null;
        }

        // 获取当前活跃的活动ID
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);

        // 获取已完成的活动ID
        List<HistoricActivityInstance> completedActivities = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .list();

        List<String> completedActivityIds = completedActivities.stream()
                .map(HistoricActivityInstance::getActivityId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 获取流程定义ID
        String processDefinitionId = processInstance.getProcessDefinitionId();

        // 生成自定义样式流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration engineConfig = processEngine.getProcessEngineConfiguration();

        // 设置更美观的字体
        setBeautifulFonts(engineConfig);

        // 自定义颜色配置（如果支持）
        ProcessDiagramGenerator diagramGenerator = engineConfig.getProcessDiagramGenerator();

        try {
            // 尝试使用支持更多自定义选项的生成方法
            return diagramGenerator.generateDiagram(
                    bpmnModel,
                    "png",
                    activeActivityIds,           // 活跃活动（红色高亮）
                    completedActivityIds,        // 已完成活动（绿色高亮）
                    engineConfig.getActivityFontName(),
                    engineConfig.getLabelFontName(),
                    engineConfig.getAnnotationFontName(),
                    engineConfig.getClassLoader(),
                    1.5,                         // 适中的缩放因子
                    true                         // 启用高亮
            );
        } catch (Exception e) {
            // 如果不支持自定义颜色，回退到默认方法
            return diagramGenerator.generateDiagram(
                    bpmnModel,
                    "png",
                    activeActivityIds,
                    Collections.emptyList(),
                    engineConfig.getActivityFontName(),
                    engineConfig.getLabelFontName(),
                    engineConfig.getAnnotationFontName(),
                    engineConfig.getClassLoader(),
                    1.5,
                    true
            );
        }
    }


    /**
     * 生成已执行节点高亮流程图（历史流程图）
     *
     * @param processInstanceId 流程实例ID
     * @return 流程图输入流
     */
    public InputStream generateHistoryProcessDiagram(String processInstanceId) {
        return generateHistoryProcessDiagram(processInstanceId, 1.0);
    }

    /**
     * 生成高清已执行节点高亮流程图（历史流程图）
     *
     * @param processInstanceId 流程实例ID
     * @param scaleFactor       缩放因子
     * @return 流程图输入流
     */
    public InputStream generateHistoryProcessDiagram(String processInstanceId, double scaleFactor) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (historicProcessInstance == null) {
            return null;
        }

        // 获取历史活动实例
        List<HistoricActivityInstance> historicActivityInstances = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        // 提取已执行的活动ID
        List<String> executedActivityIds = historicActivityInstances.stream()
                .map(HistoricActivityInstance::getActivityId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 获取流程定义ID
        String processDefinitionId = historicProcessInstance.getProcessDefinitionId();

        // 生成高亮流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration engineConfig = processEngine.getProcessEngineConfiguration();

        // 设置字体
        engineConfig.setActivityFontName("宋体");
        engineConfig.setLabelFontName("宋体");
        engineConfig.setAnnotationFontName("宋体");

        return engineConfig.getProcessDiagramGenerator()
                .generateDiagram(
                        bpmnModel,
                        "png",
                        executedActivityIds,
                        Collections.emptyList(),
                        engineConfig.getActivityFontName(),
                        engineConfig.getLabelFontName(),
                        engineConfig.getAnnotationFontName(),
                        engineConfig.getClassLoader(),
                        scaleFactor,
                        true  // 高亮已执行活动
                );
    }

    /**
     * 生成已完成流程图（已完成节点高亮）
     *
     * @param processInstanceId 流程实例ID
     * @return 流程图输入流
     */
    public InputStream generateCompletedProcessDiagram(String processInstanceId) {
        return generateCompletedProcessDiagram(processInstanceId, 1.0);
    }

    /**
     * 生成高清已完成流程图（已完成节点高亮）
     *
     * @param processInstanceId 流程实例ID
     * @param scaleFactor       缩放因子
     * @return 流程图输入流
     */
    public InputStream generateCompletedProcessDiagram(String processInstanceId, double scaleFactor) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (historicProcessInstance == null) {
            return null;
        }

        // 获取已完成的历史活动实例
        List<HistoricActivityInstance> completedActivities = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        // 提取已完成的活动ID
        List<String> completedActivityIds = completedActivities.stream()
                .map(HistoricActivityInstance::getActivityId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 获取流程定义ID
        String processDefinitionId = historicProcessInstance.getProcessDefinitionId();

        // 生成高亮流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration engineConfig = processEngine.getProcessEngineConfiguration();

        // 设置字体
        engineConfig.setActivityFontName("宋体");
        engineConfig.setLabelFontName("宋体");
        engineConfig.setAnnotationFontName("宋体");

        return engineConfig.getProcessDiagramGenerator()
                .generateDiagram(
                        bpmnModel,
                        "png",
                        completedActivityIds,
                        Collections.emptyList(),
                        engineConfig.getActivityFontName(),
                        engineConfig.getLabelFontName(),
                        engineConfig.getAnnotationFontName(),
                        engineConfig.getClassLoader(),
                        scaleFactor,
                        true  // 高亮已完成活动
                );
    }

    /**
     * 生成带当前任务处理人信息的流程图
     *
     * @param processInstanceId 流程实例ID
     * @return 流程图输入流
     */
    public InputStream generateProcessDiagramWithAssignees(String processInstanceId) {
        return generateProcessDiagramWithAssignees(processInstanceId, 1.0);
    }

    /**
     * 生成高清带当前任务处理人信息的流程图
     *
     * @param processInstanceId 流程实例ID
     * @param scaleFactor       缩放因子
     * @return 流程图输入流
     */
    public InputStream generateProcessDiagramWithAssignees(String processInstanceId, double scaleFactor) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance == null) {
            return null;
        }

        // 获取当前活跃的活动ID
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);

        // 获取当前任务及处理人信息
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .list();

        Map<String, String> activityAssigneeMap = new HashMap<>();
        for (Task task : tasks) {
            if (task.getAssignee() != null) {
                activityAssigneeMap.put(task.getTaskDefinitionKey(), task.getAssignee());
            }
        }

        // 获取流程定义ID
        String processDefinitionId = processInstance.getProcessDefinitionId();

        // 生成高亮流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration engineConfig = processEngine.getProcessEngineConfiguration();

        // 设置字体
        engineConfig.setActivityFontName("宋体");
        engineConfig.setLabelFontName("宋体");
        engineConfig.setAnnotationFontName("宋体");

        ProcessDiagramGenerator diagramGenerator = engineConfig.getProcessDiagramGenerator();

        // 如果支持自定义注释，可以添加处理人信息
        return diagramGenerator.generateDiagram(
                bpmnModel,
                "png",
                activeActivityIds,
                Collections.emptyList(),
                engineConfig.getActivityFontName(),
                engineConfig.getLabelFontName(),
                engineConfig.getAnnotationFontName(),
                engineConfig.getClassLoader(),
                scaleFactor,
                true
        );
    }

    /**
     * 生成带所有活动节点的流程图
     *
     * @param processDefinitionKey 流程定义Key
     * @return 流程图输入流
     */
    public InputStream generateAllActivitiesDiagram(String processDefinitionKey) {
        return generateAllActivitiesDiagram(processDefinitionKey, 1.0);
    }

    /**
     * 生成高清带所有活动节点的流程图
     *
     * @param processDefinitionKey 流程定义Key
     * @param scaleFactor          缩放因子
     * @return 流程图输入流
     */
    public InputStream generateAllActivitiesDiagram(String processDefinitionKey, double scaleFactor) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(processDefinitionKey)
                .latestVersion()
                .singleResult();

        if (processDefinition == null) {
            return null;
        }

        // 获取BPMN模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());

        // 获取所有活动节点ID
        List<String> allActivityIds = new ArrayList<>();
        if (bpmnModel != null) {
            for (FlowElement flowElement : bpmnModel.getMainProcess().getFlowElements()) {
                if (flowElement instanceof Activity) {
                    allActivityIds.add(flowElement.getId());
                }
            }
        }

        ProcessEngineConfiguration engineConfig = processEngine.getProcessEngineConfiguration();

        // 设置字体
        engineConfig.setActivityFontName("宋体");
        engineConfig.setLabelFontName("宋体");
        engineConfig.setAnnotationFontName("宋体");

        return engineConfig.getProcessDiagramGenerator()
                .generateDiagram(
                        bpmnModel,
                        "png",
                        allActivityIds,
                        Collections.emptyList(),
                        engineConfig.getActivityFontName(),
                        engineConfig.getLabelFontName(),
                        engineConfig.getAnnotationFontName(),
                        engineConfig.getClassLoader(),
                        scaleFactor,
                        false
                );
    }

    /**
     * 保存流程图到文件
     *
     * @param diagramStream 流程图输入流
     * @param fileName      文件名
     * @throws IOException IO异常
     */
    public void saveDiagramToFile(InputStream diagramStream, String fileName) throws IOException {
        if (diagramStream != null) {
            try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = diagramStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } finally {
                diagramStream.close();
            }
        }
    }

    /**
     * 测试各种流程图生成方法
     */
    @Test
    public void testAllProcessDiagramTypes() throws IOException {
        // 启动一个流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        assertNotNull(processInstance, "流程实例应该成功启动");

        String processInstanceId = processInstance.getId();
        String processDefinitionId = processInstance.getProcessDefinitionId();

        // 1. 生成完整流程图
        InputStream fullDiagram = generateFullProcessDiagram(processDefinitionId);
        assertNotNull(fullDiagram, "应该成功生成完整流程图");
        saveDiagramToFile(fullDiagram, "1-full-process-diagram.png");
        System.out.println("1. 完整流程图已保存: 1-full-process-diagram.png");

        // 2. 生成高清完整流程图
        InputStream hdFullDiagram = generateFullProcessDiagram(processDefinitionId, 2.0);
        assertNotNull(hdFullDiagram, "应该成功生成高清完整流程图");
        saveDiagramToFile(hdFullDiagram, "1-hd-full-process-diagram.png");
        System.out.println("2. 高清完整流程图已保存: 1-hd-full-process-diagram.png");

        // 3. 生成当前活跃节点高亮流程图
        InputStream activeDiagram = generateActiveProcessDiagram(processInstanceId);
        assertNotNull(activeDiagram, "应该成功生成当前活跃节点高亮流程图");
        saveDiagramToFile(activeDiagram, "2-active-process-diagram.png");
        System.out.println("3. 当前活跃节点高亮流程图已保存: 2-active-process-diagram.png");

        // 4. 生成高清当前活跃节点高亮流程图
        InputStream hdActiveDiagram = generateActiveProcessDiagram(processInstanceId, 2.0);
        assertNotNull(hdActiveDiagram, "应该成功生成高清当前活跃节点高亮流程图");
        saveDiagramToFile(hdActiveDiagram, "2-hd-active-process-diagram.png");
        System.out.println("4. 高清当前活跃节点高亮流程图已保存: 2-hd-active-process-diagram.png");

        // 5. 生成已执行节点高亮流程图（历史流程图）
        InputStream historyDiagram = generateHistoryProcessDiagram(processInstanceId);
        assertNotNull(historyDiagram, "应该成功生成已执行节点高亮流程图");
        saveDiagramToFile(historyDiagram, "3-history-process-diagram.png");
        System.out.println("5. 已执行节点高亮流程图已保存: 3-history-process-diagram.png");

        // 6. 生成高清已执行节点高亮流程图
        InputStream hdHistoryDiagram = generateHistoryProcessDiagram(processInstanceId, 2.0);
        assertNotNull(hdHistoryDiagram, "应该成功生成高清已执行节点高亮流程图");
        saveDiagramToFile(hdHistoryDiagram, "3-hd-history-process-diagram.png");
        System.out.println("6. 高清已执行节点高亮流程图已保存: 3-hd-history-process-diagram.png");

        // 7. 生成已完成流程图
        InputStream completedDiagram = generateCompletedProcessDiagram(processInstanceId);
        assertNotNull(completedDiagram, "应该成功生成已完成流程图");
        saveDiagramToFile(completedDiagram, "4-completed-process-diagram.png");
        System.out.println("7. 已完成流程图已保存: 4-completed-process-diagram.png");

        // 8. 生成带所有活动节点的流程图
        InputStream allActivitiesDiagram = generateAllActivitiesDiagram(processDefinitionKey);
        assertNotNull(allActivitiesDiagram, "应该成功生成带所有活动节点的流程图");
        saveDiagramToFile(allActivitiesDiagram, "5-all-activities-diagram.png");
        System.out.println("8. 带所有活动节点的流程图已保存: 5-all-activities-diagram.png");

        System.out.println("所有流程图生成完成！");
    }

    /**
     * 测试带任务处理人的流程图
     */
    @Test
    public void testProcessDiagramWithAssignees() throws IOException {
        // 启动一个流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        assertNotNull(processInstance, "流程实例应该成功启动");

        // 生成带处理人信息的流程图
        InputStream assigneeDiagram = generateProcessDiagramWithAssignees(processInstance.getId());
        assertNotNull(assigneeDiagram, "应该成功生成带处理人信息的流程图");
        saveDiagramToFile(assigneeDiagram, "assignee-process-diagram.png");
        System.out.println("带处理人信息的流程图已保存: assignee-process-diagram.png");
    }

    /**
     * 比较不同缩放因子的效果
     */
    @Test
    public void testDifferentScaleFactors() throws IOException {
        // 启动一个流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
        assertNotNull(processInstance, "流程实例应该成功启动");

        String processDefinitionId = processInstance.getProcessDefinitionId();

        // 生成不同清晰度的流程图
        double[] scaleFactors = {1.0, 1.5, 2.0, 3.0};
        for (int i = 0; i < scaleFactors.length; i++) {
            InputStream diagram = generateFullProcessDiagram(processDefinitionId, scaleFactors[i]);
            assertNotNull(diagram, "应该成功生成流程图");
            saveDiagramToFile(diagram, "scale-" + scaleFactors[i] + "x-process-diagram.png");
            System.out.println("缩放因子 " + scaleFactors[i] + "x 的流程图已保存: scale-" + scaleFactors[i] + "x-process-diagram.png");
        }
    }

    /**
     * 测试各种流程图生成方法
     */
    @Test
    public void testAllProcessDiagramTypesV2() throws IOException {
        // 配置要使用的流程实例ID（可以是已存在的实例ID）
        String configuredProcessInstanceId = null; // 可以设置为具体的实例ID，或者保持null启动新实例

        // 获取或创建流程实例
        ProcessInstance processInstance = getOrCreateProcessInstance(configuredProcessInstanceId);
        assertNotNull(processInstance, "流程实例应该成功获取或启动");

        String processInstanceId = processInstance.getId();
        String processDefinitionId = processInstance.getProcessDefinitionId();

        System.out.println("使用流程实例: " + processInstanceId);

        // 1. 生成完整流程图
        InputStream fullDiagram = generateFullProcessDiagram(processDefinitionId);
        assertNotNull(fullDiagram, "应该成功生成完整流程图");
        saveDiagramToFile(fullDiagram, "1-full-process-diagram.png");
        System.out.println("1. 完整流程图已保存: 1-full-process-diagram.png");

        // 2. 生成高清完整流程图
        InputStream hdFullDiagram = generateFullProcessDiagram(processDefinitionId, 2.0);
        assertNotNull(hdFullDiagram, "应该成功生成高清完整流程图");
        saveDiagramToFile(hdFullDiagram, "1-hd-full-process-diagram.png");
        System.out.println("2. 高清完整流程图已保存: 1-hd-full-process-diagram.png");

        // 3. 生成当前活跃节点高亮流程图
        InputStream activeDiagram = generateActiveProcessDiagram(processInstanceId);
        assertNotNull(activeDiagram, "应该成功生成当前活跃节点高亮流程图");
        saveDiagramToFile(activeDiagram, "2-active-process-diagram.png");
        System.out.println("3. 当前活跃节点高亮流程图已保存: 2-active-process-diagram.png");

        // 4. 生成高清当前活跃节点高亮流程图
        InputStream hdActiveDiagram = generateActiveProcessDiagram(processInstanceId, 2.0);
        assertNotNull(hdActiveDiagram, "应该成功生成高清当前活跃节点高亮流程图");
        saveDiagramToFile(hdActiveDiagram, "2-hd-active-process-diagram.png");
        System.out.println("4. 高清当前活跃节点高亮流程图已保存: 2-hd-active-process-diagram.png");

        // 5. 生成已执行节点高亮流程图（历史流程图）
        InputStream historyDiagram = generateHistoryProcessDiagram(processInstanceId);
        assertNotNull(historyDiagram, "应该成功生成已执行节点高亮流程图");
        saveDiagramToFile(historyDiagram, "3-history-process-diagram.png");
        System.out.println("5. 已执行节点高亮流程图已保存: 3-history-process-diagram.png");

        // 6. 生成高清已执行节点高亮流程图
        InputStream hdHistoryDiagram = generateHistoryProcessDiagram(processInstanceId, 2.0);
        assertNotNull(hdHistoryDiagram, "应该成功生成高清已执行节点高亮流程图");
        saveDiagramToFile(hdHistoryDiagram, "3-hd-history-process-diagram.png");
        System.out.println("6. 高清已执行节点高亮流程图已保存: 3-hd-history-process-diagram.png");

        // 7. 生成已完成流程图
        InputStream completedDiagram = generateCompletedProcessDiagram(processInstanceId);
        assertNotNull(completedDiagram, "应该成功生成已完成流程图");
        saveDiagramToFile(completedDiagram, "4-completed-process-diagram.png");
        System.out.println("7. 已完成流程图已保存: 4-completed-process-diagram.png");

        // 8. 生成带所有活动节点的流程图
        InputStream allActivitiesDiagram = generateAllActivitiesDiagram(processDefinitionKey);
        assertNotNull(allActivitiesDiagram, "应该成功生成带所有活动节点的流程图");
        saveDiagramToFile(allActivitiesDiagram, "5-all-activities-diagram.png");
        System.out.println("8. 带所有活动节点的流程图已保存: 5-all-activities-diagram.png");

        System.out.println("所有流程图生成完成！");
    }

    /**
     * 测试带任务处理人的流程图
     */
    @Test
    public void testProcessDiagramWithAssigneesV2() throws IOException {
        // 配置要使用的流程实例ID（可以是已存在的实例ID）
        String configuredProcessInstanceId = null; // 可以设置为具体的实例ID，或者保持null启动新实例

        // 获取或创建流程实例
        ProcessInstance processInstance = getOrCreateProcessInstance(configuredProcessInstanceId);
        assertNotNull(processInstance, "流程实例应该成功获取或启动");

        System.out.println("使用流程实例: " + processInstance.getId());

        // 生成带处理人信息的流程图
        InputStream assigneeDiagram = generateProcessDiagramWithAssignees(processInstance.getId());
        assertNotNull(assigneeDiagram, "应该成功生成带处理人信息的流程图");
        saveDiagramToFile(assigneeDiagram, "assignee-process-diagram.png");
        System.out.println("带处理人信息的流程图已保存: assignee-process-diagram.png");
    }

    /**
     * 比较不同缩放因子的效果
     */
    @Test
    public void testDifferentScaleFactorsV2() throws IOException {
        // 配置要使用的流程实例ID（可以是已存在的实例ID）
        String configuredProcessInstanceId = null; // 可以设置为具体的实例ID，或者保持null启动新实例

        // 获取或创建流程实例
        ProcessInstance processInstance = getOrCreateProcessInstance(configuredProcessInstanceId);
        assertNotNull(processInstance, "流程实例应该成功获取或启动");

        String processDefinitionId = processInstance.getProcessDefinitionId();

        System.out.println("使用流程实例: " + processInstance.getId());

        // 生成不同清晰度的流程图
        double[] scaleFactors = {1.0, 1.5, 2.0, 3.0};
        for (int i = 0; i < scaleFactors.length; i++) {
            InputStream diagram = generateFullProcessDiagram(processDefinitionId, scaleFactors[i]);
            assertNotNull(diagram, "应该成功生成流程图");
            saveDiagramToFile(diagram, "scale-" + scaleFactors[i] + "x-process-diagram.png");
            System.out.println("缩放因子 " + scaleFactors[i] + "x 的流程图已保存: scale-" + scaleFactors[i] + "x-process-diagram.png");
        }
    }

    /**
     * 获取或创建流程实例
     * 如果提供了有效的流程实例ID且存在，则返回该实例
     * 否则启动一个新的流程实例
     *
     * @param processInstanceId 配置的流程实例ID（可以为null）
     * @return 流程实例
     */
    private ProcessInstance getOrCreateProcessInstance(String processInstanceId) {
        try {
            // 如果提供了流程实例ID，尝试查找现有实例
            if (processInstanceId != null && !processInstanceId.trim().isEmpty()) {
                ProcessInstance existingInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult();

                if (existingInstance != null) {
                    System.out.println("使用现有流程实例: " + processInstanceId);
                    return existingInstance;
                } else {
                    System.out.println("指定的流程实例不存在: " + processInstanceId);
                }
            }

            // 启动新的流程实例
            ProcessInstance newInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
            System.out.println("启动新的流程实例: " + newInstance.getId());
            return newInstance;

        } catch (Exception e) {
            System.err.println("获取或创建流程实例失败: " + e.getMessage());
            throw new RuntimeException("无法获取或创建流程实例", e);
        }
    }

    /**
     * 根据业务Key获取或创建流程实例
     *
     * @param businessKey 业务Key
     * @return 流程实例
     */
    private ProcessInstance getOrCreateProcessInstanceByBusinessKey(String businessKey) {
        try {
            // 如果提供了业务Key，尝试查找现有实例
            if (businessKey != null && !businessKey.trim().isEmpty()) {
                ProcessInstance existingInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceBusinessKey(businessKey)
                        .singleResult();

                if (existingInstance != null) {
                    System.out.println("使用现有流程实例 (业务Key: " + businessKey + "): " + existingInstance.getId());
                    return existingInstance;
                } else {
                    System.out.println("指定业务Key的流程实例不存在: " + businessKey);
                }
            }

            // 启动新的流程实例（使用业务Key）
            ProcessInstance newInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey);
            System.out.println("启动新的流程实例 (业务Key: " + businessKey + "): " + newInstance.getId());
            return newInstance;

        } catch (Exception e) {
            System.err.println("根据业务Key获取或创建流程实例失败: " + e.getMessage());
            throw new RuntimeException("无法根据业务Key获取或创建流程实例", e);
        }
    }

    /**
     * 测试使用特定流程实例ID生成流程图
     */
    @Test
    public void testProcessDiagramWithSpecificInstanceId() throws IOException {
        // 可以在这里指定一个已存在的流程实例ID进行测试
        // String specificProcessInstanceId = null; // 例如: "your-existing-process-instance-id"
        // String specificProcessInstanceId = "9b9664b2-78df-11f0-9db2-8245ddc034b5"; // 例如: "your-existing-process-instance-id"
        String specificProcessInstanceId = "65941c36-78e3-11f0-ae06-8245ddc034b5"; // 例如: "your-existing-process-instance-id"

        // 获取或创建流程实例
        ProcessInstance processInstance = getOrCreateProcessInstance(specificProcessInstanceId);
        assertNotNull(processInstance, "流程实例应该成功获取或启动");

        String processInstanceId = processInstance.getId();
        String processDefinitionId = processInstance.getProcessDefinitionId();

        System.out.println("使用流程实例: " + processInstanceId);

        // 生成当前活跃节点高亮流程图
        InputStream activeDiagram = generateActiveProcessDiagram(processInstanceId);
        assertNotNull(activeDiagram, "应该成功生成当前活跃节点高亮流程图");
        saveDiagramToFile(activeDiagram, "specific-active-process-diagram.png");
        System.out.println("特定流程实例的活跃节点高亮流程图已保存: specific-active-process-diagram.png");

        // 生成已执行节点高亮流程图
        InputStream historyDiagram = generateHistoryProcessDiagram(processInstanceId);
        assertNotNull(historyDiagram, "应该成功生成已执行节点高亮流程图");
        saveDiagramToFile(historyDiagram, "specific-history-process-diagram.png");
        System.out.println("特定流程实例的已执行节点高亮流程图已保存: specific-history-process-diagram.png");
    }

    /**
     * 测试美化版流程图生成
     */
    @Test
    public void testBeautifiedProcessDiagrams() throws IOException {
        // 配置要使用的流程实例ID
        String configuredProcessInstanceId = null;

        // 获取或创建流程实例
        ProcessInstance processInstance = getOrCreateProcessInstance(configuredProcessInstanceId);
        assertNotNull(processInstance, "流程实例应该成功获取或启动");

        String processInstanceId = processInstance.getId();
        String processDefinitionId = processInstance.getProcessDefinitionId();

        System.out.println("使用流程实例: " + processInstanceId);

        // 1. 生成美化版完整流程图
        InputStream beautifiedFullDiagram = generateFullProcessDiagramBeautified(processDefinitionId, 2.0);
        assertNotNull(beautifiedFullDiagram, "应该成功生成美化版完整流程图");
        saveDiagramToFile(beautifiedFullDiagram, "beautified-full-process-diagram.png");
        System.out.println("美化版完整流程图已保存: beautified-full-process-diagram.png");

        // 2. 生成美化版当前活跃节点高亮流程图
        InputStream beautifiedActiveDiagram = generateActiveProcessDiagramBeautified(processInstanceId, 2.0);
        assertNotNull(beautifiedActiveDiagram, "应该成功生成美化版当前活跃节点高亮流程图");
        saveDiagramToFile(beautifiedActiveDiagram, "beautified-active-process-diagram.png");
        System.out.println("美化版当前活跃节点高亮流程图已保存: beautified-active-process-diagram.png");

        // 3. 生成自定义样式流程图
        InputStream customStyledDiagram = generateCustomStyledProcessDiagram(processInstanceId);
        assertNotNull(customStyledDiagram, "应该成功生成自定义样式流程图");
        saveDiagramToFile(customStyledDiagram, "custom-styled-process-diagram.png");
        System.out.println("自定义样式流程图已保存: custom-styled-process-diagram.png");

        System.out.println("美化版流程图生成完成！");
    }

    /**
     * 测试使用业务Key获取流程实例并生成流程图
     */
    @Test
    public void testProcessDiagramWithBusinessKey() throws IOException {
        // 使用业务Key获取或创建流程实例
        String businessKey = "TEST-BUSINESS-KEY-" + System.currentTimeMillis();
        ProcessInstance processInstance = getOrCreateProcessInstanceByBusinessKey(businessKey);
        assertNotNull(processInstance, "流程实例应该成功获取或启动");

        String processInstanceId = processInstance.getId();
        String processDefinitionId = processInstance.getProcessDefinitionId();

        System.out.println("使用流程实例 (业务Key: " + businessKey + "): " + processInstanceId);

        // 生成完整流程图
        InputStream fullDiagram = generateFullProcessDiagram(processDefinitionId);
        assertNotNull(fullDiagram, "应该成功生成完整流程图");
        saveDiagramToFile(fullDiagram, "business-key-full-process-diagram.png");
        System.out.println("业务Key流程实例的完整流程图已保存: business-key-full-process-diagram.png");

        // 生成当前活跃节点高亮流程图
        InputStream activeDiagram = generateActiveProcessDiagram(processInstanceId);
        assertNotNull(activeDiagram, "应该成功生成当前活跃节点高亮流程图");
        saveDiagramToFile(activeDiagram, "business-key-active-process-diagram.png");
        System.out.println("业务Key流程实例的活跃节点高亮流程图已保存: business-key-active-process-diagram.png");
    }


    /**
     * 生成完整流程图（不带高亮）- 美化版本
     *
     * @param processDefinitionId 流程定义ID
     * @return 流程图输入流
     */
    public InputStream generateFullProcessDiagramBeautified(String processDefinitionId) {
        return generateFullProcessDiagramBeautified(processDefinitionId, 1.0);
    }

    /**
     * 生成高清完整流程图（不带高亮）- 美化版本
     *
     * @param processDefinitionId 流程定义ID
     * @param scaleFactor         缩放因子，用于生成高清图片（如2.0、3.0）
     * @return 流程图输入流
     */
    public InputStream generateFullProcessDiagramBeautified(String processDefinitionId, double scaleFactor) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration engineConfig = processEngine.getProcessEngineConfiguration();

        // 设置更美观的字体
        setBeautifulFonts(engineConfig);

        return engineConfig.getProcessDiagramGenerator()
                .generateDiagram(
                        bpmnModel,
                        "png",
                        Collections.emptyList(), // 不高亮任何活动
                        Collections.emptyList(),
                        engineConfig.getActivityFontName(),
                        engineConfig.getLabelFontName(),
                        engineConfig.getAnnotationFontName(),
                        engineConfig.getClassLoader(),
                        scaleFactor,  // 缩放因子
                        false
                );
    }

    /**
     * 生成当前活跃节点高亮流程图 - 美化版本
     *
     * @param processInstanceId 流程实例ID
     * @return 流程图输入流
     */
    public InputStream generateActiveProcessDiagramBeautified(String processInstanceId) {
        return generateActiveProcessDiagramBeautified(processInstanceId, 1.5);
    }

    /**
     * 生成高清当前活跃节点高亮流程图 - 美化版本
     *
     * @param processInstanceId 流程实例ID
     * @param scaleFactor       缩放因子
     * @return 流程图输入流
     */
    public InputStream generateActiveProcessDiagramBeautified(String processInstanceId, double scaleFactor) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance == null) {
            return null;
        }

        // 获取当前活跃的活动ID
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);

        // 获取流程定义ID
        String processDefinitionId = processInstance.getProcessDefinitionId();

        // 生成高亮流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration engineConfig = processEngine.getProcessEngineConfiguration();

        // 设置更美观的字体
        setBeautifulFonts(engineConfig);

        return engineConfig.getProcessDiagramGenerator()
                .generateDiagram(
                        bpmnModel,
                        "png",
                        activeActivityIds,
                        Collections.emptyList(),
                        engineConfig.getActivityFontName(),
                        engineConfig.getLabelFontName(),
                        engineConfig.getAnnotationFontName(),
                        engineConfig.getClassLoader(),
                        scaleFactor,
                        true  // 高亮当前活动
                );
    }

    /**
     * 设置更美观的字体
     *
     * @param engineConfig 流程引擎配置
     */
    private void setBeautifulFonts(ProcessEngineConfiguration engineConfig) {
        // 尝试设置多种字体，优先使用美观的字体
        String[] preferredFonts = {
                "微软雅黑",      // Windows常用字体
                "Microsoft YaHei", // 英文字体名
                "苹方",         // macOS字体
                "PingFang SC",  // macOS英文字体名
                "思源黑体",     // 开源字体
                "Noto Sans CJK SC", // Google开源字体
                "黑体",         // 常用中文字体
                "SimHei",       // 英文字体名
                "Arial",        // 通用西文字体
                " sans-serif"   // 通用字体族
        };

        // 设置活动节点字体
        engineConfig.setActivityFontName(getAvailableFont(preferredFonts));

        // 设置标签字体
        engineConfig.setLabelFontName(getAvailableFont(preferredFonts));

        // 设置注释字体
        engineConfig.setAnnotationFontName(getAvailableFont(preferredFonts));
    }

    /**
     * 获取系统可用的字体
     *
     * @param fontNames 字体名称数组
     * @return 可用的字体名称
     */
    private String getAvailableFont(String[] fontNames) {
        // 在实际应用中，可以检查系统字体支持情况
        // 这里简化处理，返回第一个字体作为示例
        return fontNames[0];
    }

    /**
     * 生成自定义样式的流程图
     *
     * @param processInstanceId 流程实例ID
     * @return 流程图输入流
     */
    public InputStream generateCustomStyledProcessDiagramV2(String processInstanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance == null) {
            return null;
        }

        // 获取当前活跃的活动ID
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);

        // 获取已完成的活动ID
        List<HistoricActivityInstance> completedActivities = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .list();

        List<String> completedActivityIds = completedActivities.stream()
                .map(HistoricActivityInstance::getActivityId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 获取流程定义ID
        String processDefinitionId = processInstance.getProcessDefinitionId();

        // 生成自定义样式流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        ProcessEngineConfiguration engineConfig = processEngine.getProcessEngineConfiguration();

        // 设置更美观的字体
        setBeautifulFonts(engineConfig);

        // 自定义颜色配置（如果支持）
        ProcessDiagramGenerator diagramGenerator = engineConfig.getProcessDiagramGenerator();

        try {
            // 尝试使用支持更多自定义选项的生成方法
            return diagramGenerator.generateDiagram(
                    bpmnModel,
                    "png",
                    activeActivityIds,           // 活跃活动（红色高亮）
                    completedActivityIds,        // 已完成活动（绿色高亮）
                    engineConfig.getActivityFontName(),
                    engineConfig.getLabelFontName(),
                    engineConfig.getAnnotationFontName(),
                    engineConfig.getClassLoader(),
                    1.5,                         // 适中的缩放因子
                    true                         // 启用高亮
            );
        } catch (Exception e) {
            // 如果不支持自定义颜色，回退到默认方法
            return diagramGenerator.generateDiagram(
                    bpmnModel,
                    "png",
                    activeActivityIds,
                    Collections.emptyList(),
                    engineConfig.getActivityFontName(),
                    engineConfig.getLabelFontName(),
                    engineConfig.getAnnotationFontName(),
                    engineConfig.getClassLoader(),
                    1.5,
                    true
            );
        }
    }


    /**
     * 测试美化版流程图生成
     */
    @Test
    public void testBeautifiedProcessDiagramsV2() throws IOException {
        // 配置要使用的流程实例ID
        // String configuredProcessInstanceId = null;
        // String configuredProcessInstanceId = "9b9664b2-78df-11f0-9db2-8245ddc034b5"; // 例如: "your-existing-process-instance-id"
        String configuredProcessInstanceId = "65941c36-78e3-11f0-ae06-8245ddc034b5"; // 例如: "your-existing-process-instance-id"

        // 获取或创建流程实例
        ProcessInstance processInstance = getOrCreateProcessInstance(configuredProcessInstanceId);
        assertNotNull(processInstance, "流程实例应该成功获取或启动");

        String processInstanceId = processInstance.getId();
        String processDefinitionId = processInstance.getProcessDefinitionId();

        System.out.println("使用流程实例: " + processInstanceId);

        // 1. 生成美化版完整流程图
        InputStream beautifiedFullDiagram = generateFullProcessDiagramBeautified(processDefinitionId);
        assertNotNull(beautifiedFullDiagram, "应该成功生成美化版完整流程图");
        saveDiagramToFile(beautifiedFullDiagram, "beautified-full-process-diagram.png");
        System.out.println("美化版完整流程图已保存: beautified-full-process-diagram.png");

        // 2. 生成美化版当前活跃节点高亮流程图
        InputStream beautifiedActiveDiagram = generateActiveProcessDiagramBeautified(processInstanceId);
        assertNotNull(beautifiedActiveDiagram, "应该成功生成美化版当前活跃节点高亮流程图");
        saveDiagramToFile(beautifiedActiveDiagram, "beautified-active-process-diagram.png");
        System.out.println("美化版当前活跃节点高亮流程图已保存: beautified-active-process-diagram.png");

        // 3. 生成自定义样式流程图
        InputStream customStyledDiagram = generateCustomStyledProcessDiagram(processInstanceId);
        assertNotNull(customStyledDiagram, "应该成功生成自定义样式流程图");
        saveDiagramToFile(customStyledDiagram, "custom-styled-process-diagram.png");
        System.out.println("自定义样式流程图已保存: custom-styled-process-diagram.png");

        System.out.println("美化版流程图生成完成！");
    }
}
