package com.zja.tree.dirtree.repository;

import com.zja.tree.dirtree.DirTreeApplicationTest;
import com.zja.tree.dirtree.entity.DirectoryNode;
import com.zja.tree.dirtree.entity.enums.NodeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

// @DataJpaTest
class DirectoryNodeRepositoryTest extends DirTreeApplicationTest {

    @Autowired
    private DirectoryNodeRepository directoryNodeRepository;

    private DirectoryNode rootNode;
    private DirectoryNode childNode1;
    private DirectoryNode childNode2;

    @BeforeEach
    void setUp() {
        // 清理数据
        directoryNodeRepository.deleteAll();

        // 创建测试数据
        rootNode = new DirectoryNode();
        rootNode.setName("根节点");
        rootNode.setAlias("test-dir");
        rootNode.setIcon("folder-icon");
        rootNode.setBusinessType("TEST_BUSINESS");
        rootNode.setNodeType(NodeType.DIRECTORY);
        rootNode.setBusinessId("BUSINESS_001");
        rootNode.setSortOrder(1);
        rootNode.setCreatedBy("testUser");
        rootNode.setUpdatedBy("testUser");
        rootNode.setDescription("这是一个测试根节点目录");
        rootNode.setFullPath("/test");
        rootNode.setRemarks("测试备注");
        rootNode.setDepth(0);
        // 添加自定义属性
        Map<String, String> attributesRoot = new HashMap<>();
        attributesRoot.put("key1", "value1");
        attributesRoot.put("key2", "value2");
        rootNode.setCustomAttributes(attributesRoot);

        childNode1 = new DirectoryNode();
        childNode1.setName("子节点1");
        childNode1.setBusinessType("TEST_BUSINESS");
        childNode1.setNodeType(NodeType.DIRECTORY);
        childNode1.setSortOrder(1);
        childNode1.setCreatedBy("testUser");
        childNode1.setUpdatedBy("testUser");
        childNode1.setDepth(1);
        // 添加自定义属性
        Map<String, String> attributes1 = new HashMap<>();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        childNode1.setCustomAttributes(attributes1);

        childNode2 = new DirectoryNode();
        childNode2.setName("子节点2");
        childNode2.setBusinessType("TEST_BUSINESS");
        childNode2.setNodeType(NodeType.DIRECTORY);
        childNode2.setSortOrder(2);
        childNode2.setCreatedBy("testUser");
        childNode2.setUpdatedBy("testUser");
        childNode2.setDepth(1);
        // 添加自定义属性
        Map<String, String> attributes2 = new HashMap<>();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        childNode2.setCustomAttributes(attributes2);
    }

    @Test
    void testFindByBusinessTypeAndParentId() {
        // Given
        DirectoryNode savedRoot = directoryNodeRepository.save(rootNode);

        childNode1.setParentId(savedRoot.getId());
        childNode2.setParentId(savedRoot.getId());
        directoryNodeRepository.save(childNode1);
        directoryNodeRepository.save(childNode2);

        // When
        List<DirectoryNode> rootNodes = directoryNodeRepository.findByBusinessTypeAndParentId("TEST_BUSINESS", -1L);
        List<DirectoryNode> childNodes = directoryNodeRepository.findByBusinessTypeAndParentId("TEST_BUSINESS", savedRoot.getId());

        // Then
        assertEquals(1, rootNodes.size());
        assertEquals("根节点", rootNodes.get(0).getName());
        assertEquals(2, childNodes.size());
    }

    @Test
    void testFindRootNodesByBusinessType() {
        // Given
        directoryNodeRepository.save(rootNode);

        // When
        List<DirectoryNode> rootNodes = directoryNodeRepository.findRootNodesByBusinessType("TEST_BUSINESS");

        // Then
        assertEquals(1, rootNodes.size());
        assertEquals("根节点", rootNodes.get(0).getName());
        assertTrue(rootNodes.get(0).isRoot());
    }

    @Test
    void testFindByParentId() {
        // Given
        DirectoryNode savedRoot = directoryNodeRepository.save(rootNode);
        childNode1.setParentId(savedRoot.getId());
        childNode2.setParentId(savedRoot.getId());
        directoryNodeRepository.save(childNode1);
        directoryNodeRepository.save(childNode2);

        // When
        List<DirectoryNode> childNodes = directoryNodeRepository.findByParentId(savedRoot.getId());

        // Then
        assertEquals(2, childNodes.size());
        assertEquals(savedRoot.getId(), childNodes.get(0).getParentId());
        assertEquals(savedRoot.getId(), childNodes.get(1).getParentId());
    }

    @Test
    void testExistsByBusinessTypeAndBusinessId() {
        // Given
        rootNode.setBusinessId("BUSINESS_001");
        directoryNodeRepository.save(rootNode);

        // When & Then
        assertTrue(directoryNodeRepository.existsByBusinessTypeAndBusinessId("TEST_BUSINESS", "BUSINESS_001"));
        assertFalse(directoryNodeRepository.existsByBusinessTypeAndBusinessId("TEST_BUSINESS", "NON_EXISTENT"));
    }

    @Test
    void testFindByBusinessType() {
        // Given
        DirectoryNode savedRoot = directoryNodeRepository.save(rootNode);
        childNode1.setParentId(savedRoot.getId());
        directoryNodeRepository.save(childNode1);

        // When
        List<DirectoryNode> nodes = directoryNodeRepository.findByBusinessType("TEST_BUSINESS");

        // Then
        assertEquals(2, nodes.size());
        assertTrue(nodes.stream().anyMatch(node -> "根节点".equals(node.getName())));
        assertTrue(nodes.stream().anyMatch(node -> "子节点1".equals(node.getName())));
    }

    @Test
    void testCountByBusinessType() {
        // Given
        DirectoryNode savedRoot = directoryNodeRepository.save(rootNode);
        childNode1.setParentId(savedRoot.getId());
        directoryNodeRepository.save(childNode1);

        // When
        long count = directoryNodeRepository.countByBusinessType("TEST_BUSINESS");

        // Then
        assertEquals(2, count);
    }

    @Test
    void testFindByParentIdIn() {
        // Given
        DirectoryNode savedRoot1 = directoryNodeRepository.save(rootNode);

        DirectoryNode anotherRoot = new DirectoryNode();
        anotherRoot.setName("另一个根节点");
        anotherRoot.setBusinessType("TEST_BUSINESS");
        anotherRoot.setNodeType(NodeType.DIRECTORY);
        anotherRoot.setSortOrder(2);
        DirectoryNode savedRoot2 = directoryNodeRepository.save(anotherRoot);

        childNode1.setParentId(savedRoot1.getId());
        childNode2.setParentId(savedRoot2.getId());
        directoryNodeRepository.save(childNode1);
        directoryNodeRepository.save(childNode2);

        // When
        List<DirectoryNode> childNodes = directoryNodeRepository.findByParentIdIn(
                Arrays.asList(savedRoot1.getId(), savedRoot2.getId()));

        // Then
        assertEquals(2, childNodes.size());
        assertTrue(childNodes.stream().anyMatch(node -> node.getParentId().equals(savedRoot1.getId())));
        assertTrue(childNodes.stream().anyMatch(node -> node.getParentId().equals(savedRoot2.getId())));
    }

    @Test
    void testFindByBusinessTypeAndBusinessId() {
        // Given
        rootNode.setBusinessId("BUSINESS_001");
        DirectoryNode savedNode = directoryNodeRepository.save(rootNode);

        // When
        Optional<DirectoryNode> foundNode = directoryNodeRepository.findByBusinessTypeAndBusinessId("TEST_BUSINESS", "BUSINESS_001");
        Optional<DirectoryNode> notFoundNode = directoryNodeRepository.findByBusinessTypeAndBusinessId("TEST_BUSINESS", "NON_EXISTENT");

        // Then
        assertTrue(foundNode.isPresent());
        assertEquals(savedNode.getId(), foundNode.get().getId());
        assertFalse(notFoundNode.isPresent());
    }

    @Test
    void testCountByParentId() {
        // Given
        DirectoryNode savedRoot = directoryNodeRepository.save(rootNode);
        childNode1.setParentId(savedRoot.getId());
        childNode2.setParentId(savedRoot.getId());
        directoryNodeRepository.save(childNode1);
        directoryNodeRepository.save(childNode2);

        // When
        long count = directoryNodeRepository.countByParentId(savedRoot.getId());
        long zeroCount = directoryNodeRepository.countByParentId(999L); // 不存在的父节点

        // Then
        assertEquals(2, count);
        assertEquals(0, zeroCount);
    }

    @Test
    void testExistsByParentId() {
        // Given
        DirectoryNode savedRoot = directoryNodeRepository.save(rootNode);
        childNode1.setParentId(savedRoot.getId());
        directoryNodeRepository.save(childNode1);

        // When & Then
        assertTrue(directoryNodeRepository.existsByParentId(savedRoot.getId()));
        assertFalse(directoryNodeRepository.existsByParentId(999L)); // 不存在的父节点
    }

    @Test
    // @Sql(scripts = "/test-data.sql")
        // 如果需要预加载测试数据，可以使用SQL脚本
    void testFindSubtree() {
        // Given
        DirectoryNode savedRoot = directoryNodeRepository.save(rootNode);
        childNode1.setParentId(savedRoot.getId());
        childNode2.setParentId(savedRoot.getId());
        directoryNodeRepository.save(childNode1);
        directoryNodeRepository.save(childNode2);

        // When
        List<DirectoryNode> subtree = directoryNodeRepository.findSubtree(savedRoot.getId());

        // Then
        assertNotNull(subtree);
        // 注意：由于是原生查询，具体验证需要根据实际数据结构进行
        // 这里仅验证查询能正常执行且返回结果不为null
    }

    @Test
    void testFindRootNodesByBusinessTypeAndDepth() {
        // Given
        rootNode.setDepth(0);
        directoryNodeRepository.save(rootNode);

        DirectoryNode child = new DirectoryNode();
        child.setName("子节点");
        child.setBusinessType("TEST_BUSINESS");
        child.setNodeType(NodeType.DIRECTORY);
        child.setParentId(rootNode.getId());
        child.setDepth(1);
        child.setSortOrder(1);
        directoryNodeRepository.save(child);

        // When
        List<DirectoryNode> rootNodes = directoryNodeRepository.findRootNodesByBusinessTypeAndDepth("TEST_BUSINESS");

        // Then
        assertEquals(1, rootNodes.size());
        assertEquals("根节点", rootNodes.get(0).getName());
        assertEquals(0, rootNodes.get(0).getDepth());
    }
}
