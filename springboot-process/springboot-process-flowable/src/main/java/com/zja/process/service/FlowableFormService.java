package com.zja.process.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zja.process.dao.ProcessFormRepository;
import com.zja.process.exception.FormLoadException;
import com.zja.process.exception.FormNotFoundException;
import com.zja.process.model.ProcessForm;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.form.api.FormModel;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-08-15 14:44
 */
@Service
@Transactional
public class FlowableFormService {

    @Autowired
    private ProcessFormRepository processFormRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    // 获取任务表单模型
    public FormModel getFormModel(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String formKey = task.getFormKey();
        return loadFormDefinition(formKey);
    }

    // 提交表单数据
    public void submitForm(String taskId, Map<String, Object> formData, MultipartFile[] files) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        // 保存表单数据
        ProcessForm form = new ProcessForm();
        form.setProcessInstanceId(task.getProcessInstanceId());
        form.setTaskId(taskId);
        form.setFormKey(task.getFormKey());
        form.setSubmitTime(LocalDateTime.now());
        form.setFormData(processFormData(formData, files));
        processFormRepository.save(form);

        // 设置流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put(task.getFormKey() + "_submitted", true);
        variables.putAll(formData);

        // 完成任务
        taskService.complete(taskId, variables);
    }

    // 获取历史表单数据
    public List<ProcessForm> getFormHistory(String processInstanceId) {
        return processFormRepository.findByProcessInstanceId(processInstanceId);
    }

    private Map<String, Object> processFormData(Map<String, Object> formData, MultipartFile[] files) {
        // 处理文件上传
        if (files != null) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileId = saveFile(file);
                    formData.put(file.getName(), fileId);
                }
            }
        }
        return formData;
    }

    private String saveFile(MultipartFile file) {
        try {
            // 示例：保存到项目资源目录下的 uploads 文件夹（需确保目录存在）
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = "uploads/" + fileName;
            file.transferTo(new java.io.File(filePath));
            return fileName; // 可根据实际需求返回完整路径或文件ID
        } catch (IOException e) {
            throw new RuntimeException("文件保存失败: " + file.getOriginalFilename(), e);
        }
    }

    private FormModel loadFormDefinition(String formKey) {
        // 从classpath加载表单定义
        String resourcePath = "forms/" + formKey + ".json";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new FormNotFoundException("表单未找到: " + formKey);
            }
            return new ObjectMapper().readValue(is, FormModel.class);
        } catch (IOException e) {
            throw new FormLoadException("表单加载失败: " + formKey, e);
        }
    }
}