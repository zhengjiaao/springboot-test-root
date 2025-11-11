package com.zja.tree.dirtree.controller;

import com.zja.tree.dirtree.entity.enums.TemplateType;
import com.zja.tree.dirtree.model.response.InitTreeResult;
import com.zja.tree.dirtree.service.JsonTreeInitializationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 10:28
 */
@RestController
@RequestMapping("/api/directory/tree/json")
@Slf4j
public class JsonTreeInitializationController {

    @Autowired
    private JsonTreeInitializationService jsonTreeInitializationService;

    /**
     * 从文件路径初始化目录树
     */
    @PostMapping("/file")
    public ResponseEntity<InitTreeResult> initializeFromFile(@RequestParam String filePath) {
        try {
            InitTreeResult result = jsonTreeInitializationService.initializeFromJsonFile(filePath);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            log.error("从文件初始化目录树失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * 从类路径文件初始化目录树
     */
    @PostMapping("/classpath")
    public ResponseEntity<InitTreeResult> initializeFromClasspath(@RequestParam String classpath) {
        try {
            InitTreeResult result = jsonTreeInitializationService.initializeFromClasspath(classpath);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            log.error("从类路径初始化目录树失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * 使用模板初始化目录树
     */
    @PostMapping("/template/{templateType}")
    public ResponseEntity<InitTreeResult> initializeFromTemplate(
            @PathVariable String templateType) {
        try {
            TemplateType type = TemplateType.fromName(templateType);
            InitTreeResult result = jsonTreeInitializationService.initializeFromTemplate(type);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("使用模板初始化目录树失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * 从JSON字符串初始化目录树
     */
    @PostMapping("/string")
    public ResponseEntity<InitTreeResult> initializeFromJsonString(@RequestBody String jsonString) {
        try {
            InitTreeResult result = jsonTreeInitializationService.initializeFromJsonString(jsonString);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            log.error("从JSON字符串初始化目录树失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * 上传JSON文件初始化目录树
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InitTreeResult> initializeFromUploadedFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam String businessType,
            @RequestParam(required = false) String createdBy,
            @RequestParam(defaultValue = "false") boolean overwrite) {
        try {
            InitTreeResult result = jsonTreeInitializationService.initializeFromUploadedFile(
                    file, businessType, createdBy, overwrite);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            log.error("从上传文件初始化目录树失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * 异步初始化目录树
     */
    @PostMapping("/async/file")
    public ResponseEntity<Map<String, Object>> initializeFromFileAsync(@RequestParam String filePath) {
        try {
            CompletableFuture<InitTreeResult> future = jsonTreeInitializationService.initializeFromJsonFileAsync(filePath);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "目录树初始化任务已提交，正在后台处理");
            response.put("filePath", filePath);
            response.put("status", "processing");

            return ResponseEntity.accepted().body(response);
        } catch (IOException e) {
            log.error("异步初始化目录树失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * 获取所有可用模板
     */
    @GetMapping("/templates")
    public ResponseEntity<List<TemplateType>> getAvailableTemplates() {
        List<TemplateType> templates = jsonTreeInitializationService.getAvailableTemplates();
        return ResponseEntity.ok(templates);
    }

    /**
     * 验证JSON文件
     */
    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateJsonFile(@RequestParam String filePath) {
        try {
            boolean isValid = jsonTreeInitializationService.validateJsonFile(filePath);

            Map<String, Object> response = new HashMap<>();
            response.put("filePath", filePath);
            response.put("valid", isValid);
            response.put("message", isValid ? "JSON文件格式正确" : "JSON文件格式错误");

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("验证JSON文件失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * 生成目录树JSON模板
     */
    @GetMapping("/template/generate")
    public ResponseEntity<String> generateJsonTemplate(@RequestParam String businessType) {
        try {
            String template = jsonTreeInitializationService.generateJsonTemplate(businessType);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setContentDispositionFormData("attachment", businessType + "-template.json");

            return new ResponseEntity<>(template, headers, HttpStatus.OK);
        } catch (IOException e) {
            log.error("生成JSON模板失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}