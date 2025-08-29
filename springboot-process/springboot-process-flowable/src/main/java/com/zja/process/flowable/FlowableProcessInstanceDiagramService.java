package com.zja.process.flowable;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: zhengja
 * @Date: 2025-08-08 16:48
 */
@Service
public class FlowableProcessInstanceDiagramService {

/*    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessDiagramGenerator processDiagramGenerator;

    *//**
     * 根据流程实例ID获取流程图
     *//*
    public void getProcessInstanceDiagram(String processInstanceId, HttpServletResponse response) throws IOException {
        // 获取流程实例的当前执行节点
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        // 获取流程定义的ID
        String processDefinitionId = processInstance.getProcessDefinitionId();

        // 获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

        // 获取流程图
        InputStream diagramStream = repositoryService.getProcessDiagram(processDefinition.getId());

        // 设置响应类型为PNG
        response.setContentType("image/png");
        response.getOutputStream().write(diagramStream.readAllBytes());
    }*/
}