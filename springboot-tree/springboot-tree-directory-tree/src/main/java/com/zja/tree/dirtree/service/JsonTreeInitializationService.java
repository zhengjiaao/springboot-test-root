package com.zja.tree.dirtree.service;

import com.zja.tree.dirtree.entity.enums.TemplateType;
import com.zja.tree.dirtree.model.dto.JsonTreeDefinition;
import com.zja.tree.dirtree.model.response.InitTreeResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 10:28
 */
public interface JsonTreeInitializationService {

    /**
     * 从JSON文件初始化目录树
     */
    InitTreeResult initializeFromJsonFile(String filePath) throws IOException;

    /**
     * 从类路径JSON文件初始化目录树
     */
    InitTreeResult initializeFromClasspath(String classpath) throws IOException;

    /**
     * 使用预定义模板初始化目录树
     */
    InitTreeResult initializeFromTemplate(TemplateType templateType) throws IOException;

    /**
     * 从JSON字符串初始化目录树
     */
    InitTreeResult initializeFromJsonString(String jsonString) throws IOException;

    /**
     * 从上传的JSON文件初始化目录树
     */
    InitTreeResult initializeFromUploadedFile(MultipartFile file, String businessType, String createdBy, boolean overwrite) throws IOException;

    /**
     * 从树定义初始化目录树
     */
    InitTreeResult initializeFromTreeDefinition(JsonTreeDefinition treeDefinition) throws IOException;

    /**
     * 获取所有可用模板
     */
    List<TemplateType> getAvailableTemplates();

    /**
     * 验证JSON文件格式
     */
    boolean validateJsonFile(String filePath) throws IOException;

    /**
     * 异步初始化
     */
    CompletableFuture<InitTreeResult> initializeFromJsonFileAsync(String filePath) throws IOException;

    /**
     * 生成目录树JSON模板
     */
    String generateJsonTemplate(String businessType) throws IOException;
}