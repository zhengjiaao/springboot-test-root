package com.zja.process.controller;

import com.zja.process.model.ProcessForm;
import com.zja.process.service.FlowableFormService;
import org.flowable.form.api.FormModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-08-15 14:45
 */
@RestController
@RequestMapping("/api/form")
public class FormController {

    @Autowired
    private FlowableFormService flowableFormService;

    // 获取任务表单
    @GetMapping("/task/{taskId}")
    public FormModel getTaskForm(@PathVariable String taskId) {
        return flowableFormService.getFormModel(taskId);
    }

    // 提交表单
    @PostMapping(value = "/submit/{taskId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> submitTaskForm(
            @PathVariable String taskId,
            @RequestPart("formData") Map<String, Object> formData,
            @RequestPart(value = "files", required = false) MultipartFile[] files) {

        flowableFormService.submitForm(taskId, formData, files);
        return ResponseEntity.ok().build();
    }

    // 获取流程表单历史
    @GetMapping("/history/{processInstanceId}")
    public List<ProcessForm> getFormHistory(@PathVariable String processInstanceId) {
        return flowableFormService.getFormHistory(processInstanceId);
    }
}