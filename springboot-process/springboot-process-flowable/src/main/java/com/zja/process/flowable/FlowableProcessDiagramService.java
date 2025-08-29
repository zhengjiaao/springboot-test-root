package com.zja.process.flowable;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: zhengja
 * @Date: 2025-08-08 16:47
 */
@Service
public class FlowableProcessDiagramService {

/*    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessDiagramGenerator processDiagramGenerator;

    *//**
     * 根据流程定义ID获取流程图
     *//*
    public void getProcessDiagramByDefinition(String processDefinitionId, HttpServletResponse response) throws IOException {
        // 获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

        // 获取流程图的输入流
        InputStream diagramStream = repositoryService.getProcessDiagram(processDefinition.getId());

        // 设置响应头，返回图片
        response.setContentType("image/png");
        response.getOutputStream().write(diagramStream.readAllBytes());
    }*/
}