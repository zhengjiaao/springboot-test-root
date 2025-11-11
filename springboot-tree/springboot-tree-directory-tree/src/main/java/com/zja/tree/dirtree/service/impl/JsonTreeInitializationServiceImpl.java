package com.zja.tree.dirtree.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zja.tree.dirtree.entity.DirectoryNode;
import com.zja.tree.dirtree.enums.TemplateType;
import com.zja.tree.dirtree.model.dto.JsonTreeDefinition;
import com.zja.tree.dirtree.model.dto.TreeNodeData;
import com.zja.tree.dirtree.model.request.InitTreeRequest;
import com.zja.tree.dirtree.model.response.InitTreeResult;
import com.zja.tree.dirtree.repository.DirectoryNodeRepository;
import com.zja.tree.dirtree.service.JsonTreeInitializationService;
import com.zja.tree.dirtree.service.TreeInitializationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: zhengja
 * @Date: 2025-11-06 10:28
 */
@Service
@Slf4j
public class JsonTreeInitializationServiceImpl implements JsonTreeInitializationService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private TreeInitializationService treeInitializationService;

    @Autowired
    private DirectoryNodeRepository directoryNodeRepository;

    @Override
    public InitTreeResult initializeFromJsonFile(String filePath) throws IOException {
        log.info("从JSON文件初始化目录树: {}", filePath);

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("文件不存在: " + filePath);
        }

        JsonTreeDefinition treeDefinition = objectMapper.readValue(path.toFile(), JsonTreeDefinition.class);
        return initializeFromTreeDefinition(treeDefinition);
    }

    @Override
    public InitTreeResult initializeFromClasspath(String classpath) throws IOException {
        log.info("从类路径文件初始化目录树: {}", classpath);

        Resource resource = resourceLoader.getResource(classpath);
        if (!resource.exists()) {
            throw new IOException("类路径文件不存在: " + classpath);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            JsonTreeDefinition treeDefinition = objectMapper.readValue(inputStream, JsonTreeDefinition.class);
            return initializeFromTreeDefinition(treeDefinition);
        }
    }

    @Override
    public InitTreeResult initializeFromTemplate(TemplateType templateType) throws IOException {
        // 添加参数验证
        if (templateType == null) {
            throw new IllegalArgumentException("模板类型不能为空");
        }

        log.info("使用模板初始化目录树: {}", templateType);

        try {
            String templatePath = templateType.getTemplatePath();
            InitTreeResult result = initializeFromClasspath(templatePath);

            log.info("模板初始化完成，创建节点数: {}", result.getCreatedNodes().size());
            return result;
        } catch (Exception e) {
            log.error("使用模板初始化目录树失败: templateType={}", templateType, e);
            throw new IOException("模板初始化失败: " + e.getMessage(), e);
        }
    }

    @Override
    public InitTreeResult initializeFromJsonString(String jsonString) throws IOException {
        log.info("从JSON字符串初始化目录树");

        JsonTreeDefinition treeDefinition = objectMapper.readValue(jsonString, JsonTreeDefinition.class);
        return initializeFromTreeDefinition(treeDefinition);
    }

    @Override
    public InitTreeResult initializeFromUploadedFile(MultipartFile file, String businessType, String createdBy, boolean overwrite) throws IOException {
        log.info("处理上传的JSON文件: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new IOException("上传的文件为空");
        }

        // 验证文件类型
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".json")) {
            throw new IOException("只支持JSON文件");
        }

        JsonTreeDefinition treeDefinition = objectMapper.readValue(file.getInputStream(), JsonTreeDefinition.class);

        // 覆盖业务类型和创建者
        if (!StringUtils.isEmpty(businessType)) {
            treeDefinition.setBusinessType(businessType);
        }
        if (!StringUtils.isEmpty(createdBy)) {
            treeDefinition.setCreatedBy(createdBy);
        }
        treeDefinition.setOverwrite(overwrite);

        return initializeFromTreeDefinition(treeDefinition);
    }

    @Override
    public List<TemplateType> getAvailableTemplates() {
        return Arrays.asList(TemplateType.values());
    }

    @Override
    public boolean validateJsonFile(String filePath) throws IOException {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return false;
            }

            JsonTreeDefinition treeDefinition = objectMapper.readValue(path.toFile(), JsonTreeDefinition.class);
            return validateTreeDefinition(treeDefinition);
        } catch (Exception e) {
            log.warn("JSON文件验证失败: {}", e.getMessage());
            return false;
        }
    }

    @Async
    @Override
    public CompletableFuture<InitTreeResult> initializeFromJsonFileAsync(String filePath) throws IOException {
        return CompletableFuture.completedFuture(initializeFromJsonFile(filePath));
    }

    @Override
    public String generateJsonTemplate(String businessType) throws IOException {
        // 查询现有的目录结构作为模板
        List<DirectoryNode> nodes = directoryNodeRepository.findByBusinessType(businessType);

        JsonTreeDefinition template = JsonTreeDefinition.builder()
                .name(businessType + "目录树模板")
                .description("自动生成的" + businessType + "目录树模板")
                .version("1.0")
                .businessType(businessType)
                .createdBy("system")
                .overwrite(false)
                .nodes(buildJsonNodesFromDirectoryNodes(nodes))
                .build();

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(template);
    }

    /**
     * 从树定义初始化目录树
     */
    @Override
    public InitTreeResult initializeFromTreeDefinition(JsonTreeDefinition treeDefinition) throws IOException {
        if (!validateTreeDefinition(treeDefinition)) {
            throw new IOException("JSON树定义验证失败");
        }

        // 转换为InitTreeRequest
        InitTreeRequest request = convertToInitTreeRequest(treeDefinition);

        // 调用现有的初始化服务
        return treeInitializationService.initializeTree(request);
    }

    /**
     * 验证树定义
     */
    private boolean validateTreeDefinition(JsonTreeDefinition treeDefinition) {
        if (treeDefinition == null) {
            return false;
        }
        if (treeDefinition.getBusinessType() == null || treeDefinition.getBusinessType().trim().isEmpty()) {
            return false;
        }
        if (treeDefinition.getNodes() == null || treeDefinition.getNodes().isEmpty()) {
            return false;
        }
        return validateJsonNodes(treeDefinition.getNodes());
    }

    /**
     * 验证JSON节点
     */
    private boolean validateJsonNodes(List<JsonTreeDefinition.JsonNodeDefinition> nodes) {
        for (JsonTreeDefinition.JsonNodeDefinition node : nodes) {
            if (node.getName() == null || node.getName().trim().isEmpty()) {
                return false;
            }
            if (node.getNodeType() == null) {
                return false;
            }
            // 递归验证子节点
            if (node.getChildren() != null && !validateJsonNodes(node.getChildren())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 转换为InitTreeRequest
     */
    private InitTreeRequest convertToInitTreeRequest(JsonTreeDefinition treeDefinition) {
        List<TreeNodeData> treeNodes = convertJsonNodesToTreeNodeData(treeDefinition.getNodes());

        return InitTreeRequest.builder()
                .businessType(treeDefinition.getBusinessType())
                .createdBy(treeDefinition.getCreatedBy())
                .overwrite(treeDefinition.isOverwrite())
                .nodes(treeNodes)
                .build();
    }

    /**
     * 转换JSON节点为TreeNodeData
     */
    private List<TreeNodeData> convertJsonNodesToTreeNodeData(List<JsonTreeDefinition.JsonNodeDefinition> jsonNodes) {
        // 添加空值检查避免 NullPointerException
        if (jsonNodes == null || jsonNodes.isEmpty()) {
            return new ArrayList<>();
        }

        List<TreeNodeData> treeNodes = new ArrayList<>();

        for (JsonTreeDefinition.JsonNodeDefinition jsonNode : jsonNodes) {
            // 添加对单个jsonNode的空值检查
            if (jsonNode == null) {
                continue;
            }

            TreeNodeData treeNode = TreeNodeData.builder()
                    .name(jsonNode.getName())
                    .alias(jsonNode.getAlias())
                    .description(jsonNode.getDescription())
                    .icon(jsonNode.getIcon())
                    .nodeType(jsonNode.getNodeType())
                    .sortOrder(jsonNode.getSortOrder())
                    .businessId(jsonNode.getBusinessId())
                    .customAttributes(jsonNode.getCustomAttributes())
                    // .children(convertJsonNodesToTreeNodeData(jsonNode.getChildren()))
                    // 对children进行空值检查后再递归调用
                    .children(convertJsonNodesToTreeNodeData(
                            jsonNode.getChildren() != null ? jsonNode.getChildren() : new ArrayList<>()))
                    .build();

            treeNodes.add(treeNode);
        }

        return treeNodes;
    }

    /**
     * 从DirectoryNode构建JSON节点
     */
    private List<JsonTreeDefinition.JsonNodeDefinition> buildJsonNodesFromDirectoryNodes(List<DirectoryNode> directoryNodes) {
        // 构建树形结构
        List<DirectoryNode> tree = treeInitializationService.buildTree(directoryNodes);
        return convertDirectoryNodesToJsonNodes(tree);
    }

    /**
     * 转换DirectoryNode为JSON节点
     */
    private List<JsonTreeDefinition.JsonNodeDefinition> convertDirectoryNodesToJsonNodes(List<DirectoryNode> directoryNodes) {
        List<JsonTreeDefinition.JsonNodeDefinition> jsonNodes = new ArrayList<>();

        for (DirectoryNode directoryNode : directoryNodes) {
            JsonTreeDefinition.JsonNodeDefinition jsonNode = JsonTreeDefinition.JsonNodeDefinition.builder()
                    .name(directoryNode.getName())
                    .alias(directoryNode.getAlias())
                    .description(directoryNode.getDescription())
                    .icon(directoryNode.getIcon())
                    .nodeType(directoryNode.getNodeType())
                    .sortOrder(directoryNode.getSortOrder())
                    .businessId(directoryNode.getBusinessId())
                    .customAttributes(directoryNode.getCustomAttributes())
                    .children(convertDirectoryNodesToJsonNodes(directoryNode.getChildren()))
                    .build();

            jsonNodes.add(jsonNode);
        }

        return jsonNodes;
    }

    /**
     * 更新业务类型
     */
    private void updateBusinessType(List<DirectoryNode> nodes, String newBusinessType) {
        for (DirectoryNode node : nodes) {
            node.setBusinessType(newBusinessType);
            directoryNodeRepository.save(node);
        }
    }
}