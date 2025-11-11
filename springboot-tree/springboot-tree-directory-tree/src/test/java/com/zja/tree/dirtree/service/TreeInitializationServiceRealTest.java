package com.zja.tree.dirtree.service;

import com.zja.tree.dirtree.DirTreeApplicationTest;
import com.zja.tree.dirtree.entity.DirectoryNode;
import com.zja.tree.dirtree.entity.enums.NodeType;
import com.zja.tree.dirtree.model.dto.TreeNodeData;
import com.zja.tree.dirtree.model.request.CreateDirectoryRequest;
import com.zja.tree.dirtree.model.request.InitTreeRequest;
import com.zja.tree.dirtree.model.response.InitTreeResult;
import com.zja.tree.dirtree.repository.DirectoryNodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * 不使用Mock的TreeInitializationService测试类
 */
public class TreeInitializationServiceRealTest extends DirTreeApplicationTest {

    @Autowired
    private TreeInitializationService treeInitializationService;
    @Autowired
    private DirectoryTreeService directoryTreeService;
    @Autowired
    private DirectoryNodeRepository directoryNodeRepository;

    @BeforeEach
    void setUp() {
        // 清理数据
        directoryNodeRepository.deleteAll();
    }

    @Test
    void initializeTree_ShouldCreateTree_WhenValidRequestProvided() {
        // Given
        InitTreeRequest request = createSampleInitTreeRequest();

        // When
        InitTreeResult result = treeInitializationService.initializeTree(request);

        // Then
        assertNotNull(result);
        assertEquals("TEST_BUSINESS", result.getBusinessType());
        assertEquals(1, result.getTotalNodes());
        assertFalse(result.getRootNodes().isEmpty());
        assertFalse(result.getCreatedNodes().isEmpty());

        // 验证节点已保存到数据库
        long count = directoryNodeRepository.countByBusinessType("TEST_BUSINESS");
        assertEquals(1, count);
    }

    @Test
    void initializeTree_ShouldThrowException_WhenBusinessTypeExistsAndNotOverwrite() {
        // Given
        InitTreeRequest request = InitTreeRequest.builder()
                .businessType("EXISTING_TYPE")
                .overwrite(false)
                .nodes(Collections.singletonList(
                        TreeNodeData.builder()
                                .name("Test Node")
                                .nodeType(NodeType.DIRECTORY)
                                .sortOrder(1)
                                .build()
                ))
                .build();

        // 先创建一个节点
        directoryTreeService.createNode(CreateDirectoryRequest.builder()
                .name("Existing Node")
                .businessType("EXISTING_TYPE")
                .nodeType(NodeType.DIRECTORY)
                .sortOrder(1)
                .build());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            treeInitializationService.initializeTree(request);
        });
    }

    @Test
    void initializeTree_ShouldDeleteExistingTree_WhenOverwriteIsTrue() {
        // Given
        // 先创建一些现有数据
        DirectoryNode existingNode = directoryTreeService.createNode(
                CreateDirectoryRequest.builder()
                        .name("Existing Node")
                        .businessType("EXISTING_TYPE")
                        .nodeType(NodeType.DIRECTORY)
                        .sortOrder(1)
                        .build());

        InitTreeRequest request = InitTreeRequest.builder()
                .businessType("EXISTING_TYPE")
                .overwrite(true)
                .nodes(Collections.singletonList(
                        TreeNodeData.builder()
                                .name("New Root Node")
                                .nodeType(NodeType.DIRECTORY)
                                .sortOrder(1)
                                .build()
                ))
                .build();

        // When
        InitTreeResult result = treeInitializationService.initializeTree(request);

        // Then
        assertNotNull(result);
        assertEquals("EXISTING_TYPE", result.getBusinessType());
        assertEquals(1, result.getTotalNodes());

        // 验证旧节点已被删除，新节点已创建
        List<DirectoryNode> nodes = directoryNodeRepository.findByBusinessType("EXISTING_TYPE");
        assertEquals(1, nodes.size());
        assertEquals("New Root Node", nodes.get(0).getName());
    }

    @Test
    void isBusinessTypeInitialized_ShouldReturnTrue_WhenBusinessTypeExists() {
        // Given
        // 创建一个节点
        directoryTreeService.createNode(CreateDirectoryRequest.builder()
                .name("Test Node")
                .businessType("INITIALIZED_TYPE")
                .nodeType(NodeType.DIRECTORY)
                .sortOrder(1)
                .build());

        // When
        boolean result = treeInitializationService.isBusinessTypeInitialized("INITIALIZED_TYPE");

        // Then
        assertTrue(result);
    }

    @Test
    void isBusinessTypeInitialized_ShouldReturnFalse_WhenBusinessTypeDoesNotExist() {
        // When
        boolean result = treeInitializationService.isBusinessTypeInitialized("NON_EXISTING_TYPE");

        // Then
        assertFalse(result);
    }

    @Test
    void batchCreateNodes_ShouldCreateMultipleNodes() {
        // Given
        List<CreateDirectoryRequest> requests = Arrays.asList(
                CreateDirectoryRequest.builder()
                        .name("Node1")
                        .businessType("TEST_BUSINESS_01")
                        .nodeType(NodeType.DIRECTORY)
                        .sortOrder(0)
                        .build(),
                CreateDirectoryRequest.builder()
                        .name("Node2")
                        .businessType("TEST_BUSINESS_02")
                        .nodeType(NodeType.DIRECTORY)
                        .sortOrder(0)
                        .build()
        );

        // When
        List<DirectoryNode> result = treeInitializationService.batchCreateNodes(requests);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Node1", result.get(0).getName());
        assertEquals("Node2", result.get(1).getName());

        // 验证节点已保存到数据库
        assertEquals(1, directoryNodeRepository.countByBusinessType("TEST_BUSINESS_01"));
        assertEquals(1, directoryNodeRepository.countByBusinessType("TEST_BUSINESS_02"));
    }

    @Test
    void initializeTreeAsync_ShouldReturnCompletableFuture() {
        // Given
        InitTreeRequest request = createSampleInitTreeRequest();

        // When
        CompletableFuture<InitTreeResult> futureResult = treeInitializationService.initializeTreeAsync(request);

        // Then
        assertNotNull(futureResult);
        assertFalse(futureResult.isCompletedExceptionally());

        InitTreeResult result = futureResult.join();
        assertNotNull(result);
        assertEquals("TEST_BUSINESS", result.getBusinessType());
    }

    @Test
    void buildTree_ShouldConstructTreeFromFlatList() {
        // Given
        DirectoryNode rootNode = new DirectoryNode();
        rootNode.setId(1L);
        rootNode.setParentId(-1L);
        rootNode.setName("Root");
        rootNode.setSortOrder(1);

        DirectoryNode childNode = new DirectoryNode();
        childNode.setId(2L);
        childNode.setParentId(1L);
        childNode.setName("Child");
        childNode.setSortOrder(1);

        List<DirectoryNode> flatList = Arrays.asList(childNode, rootNode); // Unordered list

        // When
        List<DirectoryNode> result = treeInitializationService.buildTree(flatList);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Root", result.get(0).getName());
        assertEquals(1, result.get(0).getChildren().size());
        assertEquals("Child", result.get(0).getChildren().get(0).getName());
    }

    @Test
    void buildTree_ShouldReturnEmptyList_WhenInputIsNull() {
        // When
        List<DirectoryNode> result = treeInitializationService.buildTree(null);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void buildTree_ShouldReturnEmptyList_WhenInputIsEmpty() {
        // When
        List<DirectoryNode> result = treeInitializationService.buildTree(new ArrayList<>());

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private InitTreeRequest createSampleInitTreeRequest() {
        TreeNodeData treeNodeData = new TreeNodeData();
        treeNodeData.setName("Root Node");
        treeNodeData.setNodeType(NodeType.DIRECTORY);
        treeNodeData.setSortOrder(1);

        return InitTreeRequest.builder()
                .businessType("TEST_BUSINESS")
                .createdBy("testUser")
                .overwrite(false)
                .nodes(Collections.singletonList(treeNodeData))
                .build();
    }

}
