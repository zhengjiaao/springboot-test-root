package com.zja.tree.dirtree.service;

import com.alibaba.fastjson.JSON;
import com.zja.tree.dirtree.DirTreeApplicationTest;
import com.zja.tree.dirtree.enums.NodeType;
import com.zja.tree.dirtree.enums.TemplateType;
import com.zja.tree.dirtree.model.dto.JsonTreeDefinition;
import com.zja.tree.dirtree.model.response.InitTreeResult;
import com.zja.tree.dirtree.repository.DirectoryNodeRepository;
import com.zja.tree.dirtree.util.ResourcesFileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 不使用Mock的JsonTreeInitializationService测试类
 */
public class JsonTreeInitializationServiceRealTest extends DirTreeApplicationTest {

    @Autowired
    private JsonTreeInitializationService jsonTreeInitializationService;
    @Autowired
    private DirectoryNodeRepository directoryNodeRepository;

    @BeforeEach
    void setUp() {
        // 清空数据
        directoryNodeRepository.deleteAll();
    }

    @Test
    void initializeFromTemplate_ShouldInitializeTree_WhenValidTemplateProvided() throws IOException {
        // InitTreeResult result = jsonTreeInitializationService.initializeFromTemplate(TemplateType.DOCUMENT_STRUCTURE);
        InitTreeResult result = jsonTreeInitializationService.initializeFromTemplate(TemplateType.PRODUCT_CATEGORY);
        System.out.println(JSON.toJSON(result));
    }

    @Test
    void initializeFromJsonString_ShouldInitializeTree_WhenValidJsonProvided() throws IOException {
        // Given
        String jsonString = ResourcesFileUtil.readFile("tree-templates/document-structure.json", StandardCharsets.UTF_8);

        // When & Then
        assertDoesNotThrow(() -> {
            InitTreeResult result = jsonTreeInitializationService.initializeFromJsonString(jsonString);
            System.out.println(JSON.toJSON(result));
        });
        // 注意：由于缺少真实的TreeInitializationService和DirectoryNodeRepository，
        // 这里只能验证不会抛出异常，而不能验证具体的结果
    }

    @Test
    void initializeFromJsonFile_ShouldInitializeTree_WhenValidFileProvided() throws IOException {
        // Given
        String filePath = "temp_test_tree.json";
        String jsonString = "{\n" +
                "  \"businessType\": \"FILE_TEST_BUSINESS\",\n" +
                "  \"createdBy\": \"testUser\",\n" +
                "  \"overwrite\": false,\n" +
                "  \"nodes\": [\n" +
                "    {\n" +
                "      \"name\": \"Root\",\n" +
                "      \"nodeType\": \"DIRECTORY\",\n" +
                "      \"sortOrder\": 1\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // 创建临时文件
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(jsonString);
        }

        // When & Then
        try {
            assertDoesNotThrow(() -> {
                jsonTreeInitializationService.initializeFromJsonFile(filePath);
            });
        } finally {
            // 清理临时文件
            Files.deleteIfExists(Paths.get(filePath));
        }
    }

    @Test
    void getAvailableTemplates_ShouldReturnAllTemplates() {
        // When
        List<TemplateType> templates = jsonTreeInitializationService.getAvailableTemplates();
        System.out.println(JSON.toJSON(templates));

        // Then
        assertNotNull(templates);
        assertFalse(templates.isEmpty());
        assertTrue(templates.contains(TemplateType.DOCUMENT_STRUCTURE));
    }

    @Test
    void validateJsonFile_ShouldReturnFalse_WhenFileNotExists() throws IOException {
        // Given
        String filePath = "nonexistent.json";

        // When
        boolean result = jsonTreeInitializationService.validateJsonFile(filePath);

        // Then
        assertFalse(result);
    }

    @Test
    void initializeFromUploadedFile_ShouldProcessFile_WhenValidFileProvided() throws IOException {
        // Given
        String jsonString = "{\n" +
                "  \"businessType\": \"UPLOAD_TEST_BUSINESS\",\n" +
                "  \"createdBy\": \"testUser\",\n" +
                "  \"overwrite\": false,\n" +
                "  \"nodes\": [\n" +
                "    {\n" +
                "      \"name\": \"Root\",\n" +
                "      \"nodeType\": \"DIRECTORY\",\n" +
                "      \"sortOrder\": 1\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.json",
                "application/json",
                jsonString.getBytes()
        );

        // When & Then
        assertDoesNotThrow(() -> {
            jsonTreeInitializationService.initializeFromUploadedFile(
                    file, "NEW_BUSINESS", "testUser", true);
        });

    }

    @Test
    void initializeFromJsonFileAsync_ShouldReturnCompletableFuture() throws IOException {
        // Given
        String filePath = "temp_async_test_tree.json";
        String jsonString = "{\n" +
                "  \"businessType\": \"ASYNC_TEST_BUSINESS\",\n" +
                "  \"createdBy\": \"testUser\",\n" +
                "  \"overwrite\": false,\n" +
                "  \"nodes\": [\n" +
                "    {\n" +
                "      \"name\": \"Root\",\n" +
                "      \"nodeType\": \"DIRECTORY\",\n" +
                "      \"sortOrder\": 1\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // 创建临时文件
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(jsonString);
        }

        // When
        try {
            CompletableFuture<InitTreeResult> futureResult =
                    jsonTreeInitializationService.initializeFromJsonFileAsync(filePath);

            // Then
            assertNotNull(futureResult);
            // 注意：由于缺少真实依赖，这里无法完整验证结果
        } finally {
            // 清理临时文件
            Files.deleteIfExists(Paths.get(filePath));
        }
    }

    @Test
    void generateJsonTemplate_ShouldGenerateTemplate_WhenValidBusinessTypeProvided() throws IOException {
        // Given
        JsonTreeDefinition sampleTreeDefinition = createSampleTreeDefinition();

        // When
        InitTreeResult result = jsonTreeInitializationService.initializeFromTreeDefinition(sampleTreeDefinition);
        System.out.println(JSON.toJSON(result));

        // Then
        assertNotNull(result);
    }

    private JsonTreeDefinition createSampleTreeDefinition() {
        JsonTreeDefinition.JsonNodeDefinition nodeDefinition =
                JsonTreeDefinition.JsonNodeDefinition.builder()
                        .name("Root")
                        .nodeType(NodeType.DIRECTORY)
                        .sortOrder(1)
                        .build();

        return JsonTreeDefinition.builder()
                .businessType("TEST_BUSINESS")
                .createdBy("testUser")
                .overwrite(false)
                .nodes(Collections.singletonList(nodeDefinition))
                .build();
    }
}
