/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-03-27 17:16
 * @Since:
 */
package com.zja.neo4j;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Record;
import org.neo4j.driver.Value;
import org.neo4j.driver.exceptions.ServiceUnavailableException;
import org.neo4j.driver.types.Entity;
import org.neo4j.driver.types.Relationship;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * neo4j-java-driver 4.4.x 对应 neo4j server 4.x
 * @author: zhengja
 * @since: 2023/03/27 17:16
 */
@SpringBootTest
public class Neo4jManagerTest {

    private static Neo4jManager neo4jManager;

    @BeforeAll
    public static void setUp() {
        //String uri = "bolt://localhost:7687";
        String uri = "bolt://192.168.159.145:7687/neo4j";
        String user = "neo4j";
        String password = "password";
        neo4jManager = new Neo4jManager(uri, user, password);

        System.out.println(neo4jManager.connect());
    }

    @AfterAll
    public static void disconnect() {
        neo4jManager.disconnect();
    }

    //创建、更新节点

    @Test
    public void testLabelExists() {
        assertFalse(neo4jManager.checkNodeLabelExists("NonExistentLabel"));
        neo4jManager.createEmptyNodeLabel("TestLabel");
        assertTrue(neo4jManager.checkNodeLabelExists("TestLabel"));
    }

    @Test
    public void testUpdateNodeLabelName() {
        neo4jManager.createEmptyNodeLabel("TestLabel");
        neo4jManager.updateNodeLabelName("TestLabel","TestLabel2");

    }

    @Test
    public void testCreateNode() {
        // create node
        Map<String, Object> node = new HashMap<>();
        node.put("name", "Alice");
        node.put("age", 30);
        neo4jManager.createNode("Person", node);

        Map<String, Object> node2 = new HashMap<>();
        node2.put("name", "Alice");
        node2.put("age", 40);
        neo4jManager.createNode("Person", node2);

        List<String> nodeLabels = neo4jManager.queryAllNodeLabels();
        for (String label : nodeLabels) {
            System.out.println(label);
        }
    }

    @Test
    public void testUpdateNode() {
        Map<String, Object> newProperties = new HashMap<>();
        newProperties.put("age", 31);
        assertDoesNotThrow(() -> neo4jManager.updateNode("Person", "name", "Alice", newProperties));
    }

    //查询节点

    @Test
    public void testQueryNodes() {
        List<Record> nodes = neo4jManager.queryNodes("Person", 100);
        for (Record node : nodes) {
            Iterable<String> labels = node.get("n").asNode().labels();
            for (String label : labels) {
                System.out.println("label：" + label);
                //label：Person
            }

            System.out.println("asMap：" + node.get("n").asNode().asMap());
            //asMap：{name=Alice, age=30}

            Iterable<Value> values1 = node.get("n").asNode().values();
            for (Value value : values1) {
                System.out.println("values1：" + value);
                //values1："Alice"
                //values1：30
                //values1："Bob"
                //values1：25
            }

            Iterable<String> keys = node.get("n").asNode().keys();
            for (String key : keys) {
                System.out.println("key：" + key);
                //key：name
                //key：age
            }

            Entity entity = node.get("n").asEntity();
//            System.out.println(entity.asMap());
            //{name=Bob, age=25}

            Iterable<Value> values = entity.values();
            for (Value value : values) {
                System.out.println("values：" + value);
                //"Alice"
                //30
                //"Bob"
                //25
            }

        }

        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
    }

    @Test
    public void testQueryNodesByProperty() {
        List<Record> nodes = neo4jManager.queryNodesByProperty("Person", "name", "Alice");
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
    }

    @Test
    public void testQueryNodesByPage() {
        String query = "MATCH (n:Person) RETURN n";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("age", 30);
        List<Record> nodes = neo4jManager.queryNodesByPage(query, parameters, 1, 1);
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
    }

    @Test
    public void testQueryAllNodes() {
        List<Record> nodes = neo4jManager.queryAllNodes();
        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
    }

    @Test
    public void testQueryAllNodesCount() {
        int allNodesCount = neo4jManager.queryAllNodesCount();
        System.out.println(allNodesCount);
    }


    //删除节点

    @Test
    public void testDeleteNode() {
        assertDoesNotThrow(() -> neo4jManager.deleteNode("Person", "name", "Alice"));
    }

    @Test
    public void deleteAllNodes() {
        assertDoesNotThrow(() -> neo4jManager.deleteAllNodes("Person"));
    }


    //创建关系

    @Test
    public void testCreateAndQueryAllNodesAndRelationships() {
        //为了方便测试，先删除节点
        neo4jManager.deleteAllNodes("Person");

        // create nodes
        Map<String, Object> node1 = new HashMap<>();
        node1.put("name", "Alice");
        node1.put("age", 30);
        neo4jManager.createNode("Person", node1);

        Map<String, Object> node2 = new HashMap<>();
        node2.put("name", "Bob");
        node2.put("age", 25);
        neo4jManager.createNode("Person", node2);

        //创建关系
        // create relationships
        Map<String, Object> relationshipProperties1 = new HashMap<>();
        relationshipProperties1.put("since", 2020);
        neo4jManager.createRelationship("Person", "Person", "name", "Alice", "name", "Bob", "FRIENDS", relationshipProperties1);

        Map<String, Object> relationshipProperties2 = new HashMap<>();
        relationshipProperties2.put("since", 2021);
        neo4jManager.createRelationship("Person", "Person", "name", "Alice", "name", "Bob", "FAMILY", relationshipProperties2);

        //查询关系
        // query  nodes and relationships
        List<Relationship> relationships = neo4jManager.queryRelationshipByProperty("Person", "Person", "name", "Alice", "name", "Bob", "FAMILY");

        // check nodes and relationships exist
        assertEquals(1, relationships.size());
        Relationship relationship = relationships.get(0);
        assertEquals(2021, relationship.get("since").asInt());

        //查询关系
        // query  nodes and relationships
        List<Relationship> relationships2 = neo4jManager.queryRelationshipByProperty("Person", "Person", "name", "Alice", "name", "Bob");

        // check nodes and relationships exist
        assertEquals(2, relationships2.size());
        for (Relationship relationship1 : relationships2) {
            System.out.println(relationship1.type());
            System.out.println(relationship1.get("since").asInt());
            //FRIENDS
            //2020
            //FAMILY
            //2021
        }

    }

    @Test
    public void testCreateRelationship() {
        Map<String, Object> relationshipProperties = new HashMap<>();
        relationshipProperties.put("since", "2022-01-01");
        assertDoesNotThrow(() -> neo4jManager.createRelationship("Person", "Person", "name", "Alice", "name", "Bob", "FRIENDS", relationshipProperties));
    }

    //查询关系

    @Test
    public void testQueryAllNodesAndRelationships() {
        List<Record> records = neo4jManager.queryAllNodesAndRelationships();
        assertNotNull(records);
        assertFalse(records.isEmpty());
        System.out.println(records.size());

        List<Relationship> relationships = new ArrayList<>();
        for (Record record : records) {
            Relationship relationship = record.get("r").asRelationship();
            relationships.add(relationship);
        }

        for (Relationship relationship : relationships) {
            System.out.println(relationship.type() + " : " + relationship.asMap());
        }
    }

    @Test
    public void testQueryAllRelationships() {
        List<Record> records = neo4jManager.queryAllRelationships();
        assertNotNull(records);
        assertFalse(records.isEmpty());
        System.out.println(records.size());

        List<Relationship> relationships = new ArrayList<>();
        for (Record record : records) {
            Relationship relationship = record.get("r").asRelationship();
            relationships.add(relationship);
        }

        for (Relationship relationship : relationships) {
            System.out.println(relationship.type() + " : " + relationship.asMap());
        }

    }

    @Test
    public void testGetNodeRelationships() {
        //为了方便测试，先删除节点
        neo4jManager.deleteAllNodes("Person");

        // create nodes
        Map<String, Object> node1 = new HashMap<>();
        node1.put("name", "Alice");
        node1.put("age", 30);
        neo4jManager.createNode("Person", node1);

        Map<String, Object> node2 = new HashMap<>();
        node2.put("name", "Bob");
        node2.put("age", 25);
        neo4jManager.createNode("Person", node2);

        //创建关系
        // create relationships
        Map<String, Object> relationshipProperties1 = new HashMap<>();
        relationshipProperties1.put("since", 2020);
        neo4jManager.createRelationship("Person", "Person", "name", "Alice", "name", "Bob", "FRIEND", relationshipProperties1);

        Map<String, Object> relationshipProperties2 = new HashMap<>();
        relationshipProperties2.put("since", 2021);
        neo4jManager.createRelationship("Person", "Person", "name", "Alice", "name", "Bob", "FOLLOWS", relationshipProperties2);

        List<String> relationships = neo4jManager.queryNodeRelationships("Person");
        assertEquals(2, relationships.size());
        assertTrue(relationships.contains("FRIEND"));
        assertTrue(relationships.contains("FOLLOWS"));
    }

    @Test
    public void testGetRelationshipsBetweenNodes() {
        //为了方便测试，先删除节点
        neo4jManager.deleteAllNodes("Person");
        neo4jManager.deleteAllNodes("Company");

        Map<String, Object> properties1 = new HashMap<>();
        properties1.put("name", "Alice");
        properties1.put("age", 30);
        neo4jManager.createNode("Person", properties1);
        Map<String, Object> properties2 = new HashMap<>();
        properties2.put("name", "Bob");
        properties2.put("age", 35);
        neo4jManager.createNode("Company", properties2);

        //创建关系
        neo4jManager.createRelationship("Person", "Company", "WORKS_FOR", "name", "Alice", "name", "Bob");

        //查询关系
        List<String> relationships = neo4jManager.queryRelationshipsBetweenNodes("Person", "Company");
        assertEquals(1, relationships.size());
        assertTrue(relationships.contains("WORKS_FOR"));

        //创建关系 and 查询关系
        neo4jManager.createRelationship("Company", "Person", "EMPLOYS", "name", "Bob", "name", "Alice");
        List<String> relationships2 = neo4jManager.queryRelationshipsBetweenNodes("Company", "Person");
        assertEquals(1, relationships2.size());
        assertTrue(relationships2.contains("EMPLOYS"));
    }

    //统计关系个数

    @Test
    public void testCountNodeRelationships() {
        //为了方便测试，先删除节点
        neo4jManager.deleteAllNodes("Person");
        neo4jManager.deleteAllNodes("Company");

        // create nodes
        Map<String, Object> node1 = new HashMap<>();
        node1.put("name", "Alice");
        node1.put("age", 30);
        neo4jManager.createNode("Person", node1);

        Map<String, Object> node2 = new HashMap<>();
        node2.put("name", "Bob");
        node2.put("age", 25);
        neo4jManager.createNode("Person", node2);

        Map<String, Object> node3 = new HashMap<>();
        node3.put("name", "Lisi");
        node3.put("age", 35);
        neo4jManager.createNode("Company", node3);

        //创建关系
        // create relationships
        Map<String, Object> relationshipProperties1 = new HashMap<>();
        relationshipProperties1.put("since", 2020);
        neo4jManager.createRelationship("Person", "Person", "name", "Alice", "name", "Bob", "FRIEND", relationshipProperties1);

        Map<String, Object> relationshipProperties2 = new HashMap<>();
        relationshipProperties2.put("since", 2021);
        neo4jManager.createRelationship("Person", "Person", "name", "Alice", "name", "Bob", "FOLLOWS", relationshipProperties2);

        Map<String, Object> relationshipProperties3 = new HashMap<>();
        relationshipProperties3.put("since", 2021);
        neo4jManager.createRelationship("Person", "Company", "name", "Alice", "name", "Lisi", "EMPLOYS", relationshipProperties3);

        //统计关系个数
        Map<String, Integer> relationshipCounts = neo4jManager.countNodeRelationships("Person");
        assertEquals(3, relationshipCounts.size());
        assertEquals(1, relationshipCounts.get("FRIEND").intValue());
        assertEquals(1, relationshipCounts.get("FOLLOWS").intValue());
        assertEquals(1, relationshipCounts.get("EMPLOYS").intValue());

        //这是错误的，不能获取到关系EMPLOYS，注意节点的起始指向 是 Person --> Company
//        Map<String, Integer> relationshipCounts2 = neo4jManager.countNodeRelationships("Company");
//        assertEquals(1, relationshipCounts2.get("EMPLOYS").intValue());
    }

    @Test
    public void testQueryAllRelationshipCount() {
        int allRelationshipCount = neo4jManager.queryAllRelationshipCount();
        System.out.println(allRelationshipCount);
    }

    //删除关系

    @Test
    public void testDeleteRelationship() {
        assertDoesNotThrow(() -> neo4jManager.deleteRelationship("Person", "Person", "name", "name"));
    }


    @Test
    public void testConnection() {
        assertDoesNotThrow(() -> neo4jManager.queryAllNodes());
    }

    @Test
    public void testConnectionFailed() {
        Neo4jManager neo4jManager = new Neo4jManager("bolt://localhost:7688", "neo4j", "password");
        assertThrows(ServiceUnavailableException.class, () -> neo4jManager.queryAllNodes());
        neo4jManager.disconnect();
    }


    @Test
    public void testCreateAndDeleteNode() {
        // create node
        Map<String, Object> node = new HashMap<>();
        node.put("name", "Alice");
        node.put("age", 30);
        neo4jManager.createNode("Person", node);

        // check node exists
        List<Record> nodes = neo4jManager.queryAllNodes();
        assertEquals(1, nodes.size());

        // delete node
        neo4jManager.deleteNode("Person", "name", "Alice");

        // check node deleted
        nodes = neo4jManager.queryAllNodes();
        assertEquals(0, nodes.size());
    }

    @Test
    public void testDeleteAllNodes() {
        //为了测试，先删除所有节点数据 delete all nodes
        neo4jManager.deleteAllNodes("Person");

        // create nodes
        Map<String, Object> node1 = new HashMap<>();
        node1.put("name", "Alice");
        node1.put("age", 30);
        neo4jManager.createNode("Person", node1);

        Map<String, Object> node2 = new HashMap<>();
        node2.put("name", "Bob");
        node2.put("age", 25);
        neo4jManager.createNode("Person", node2);

        // check nodes exist
        List<Record> nodes = neo4jManager.queryAllNodes();
        assertEquals(2, nodes.size());

        // delete all nodes
        neo4jManager.deleteAllNodes("Person");

        // check nodes deleted
        nodes = neo4jManager.queryAllNodes();
        assertEquals(0, nodes.size());
    }

    @Test
    public void testQueryAllNodes2() {
        // create nodes
        Map<String, Object> node1 = new HashMap<>();
        node1.put("name", "Alice");
        node1.put("age", 30);
        neo4jManager.createNode("Person", node1);

        Map<String, Object> node2 = new HashMap<>();
        node2.put("name", "Bob");
        node2.put("age", 25);
        neo4jManager.createNode("Person", node2);

        // query all nodes
        List<Record> nodes = neo4jManager.queryAllNodes();

        // check nodes exist
        assertEquals(2, nodes.size());
    }

    @Test
    public void testCreateAndDeleteRelationship() {
        // create nodes
        Map<String, Object> node1 = new HashMap<>();
        node1.put("name", "Alice");
        node1.put("age", 30);
        neo4jManager.createNode("Person", node1);

        Map<String, Object> node2 = new HashMap<>();
        node2.put("name", "Bob");
        node2.put("age", 25);
        neo4jManager.createNode("Person", node2);

        // create relationship
        Map<String, Object> relationshipProperties = new HashMap<>();
        relationshipProperties.put("since", 2020);
        neo4jManager.createRelationship("Person", "Person", "name", "Alice", "name", "Bob", "FRIENDS", relationshipProperties);

        // check relationship exists
        List<Record> relationships = neo4jManager.queryAllRelationships();
        assertEquals(1, relationships.size());

        // delete relationship
        neo4jManager.deleteRelationship("Person", "Person", "name", "Alice", "name", "Bob", "FRIENDS");

        // check relationship deleted
        relationships = neo4jManager.queryAllRelationships();
        assertEquals(0, relationships.size());
    }

    @Test
    public void testDeleteAllRelationships() {
        // create nodes
        Map<String, Object> node1 = new HashMap<>();
        node1.put("name", "Alice");
        node1.put("age", 30);
        neo4jManager.createNode("Person", node1);

        Map<String, Object> node2 = new HashMap<>();
        node2.put("name", "Bob");
        node2.put("age", 25);
        neo4jManager.createNode("Person", node2);

        // create relationships
        Map<String, Object> relationshipProperties1 = new HashMap<>();
        relationshipProperties1.put("since", 2020);
        neo4jManager.createRelationship("Person", "Person", "name", "Alice", "name", "Bob", "FRIENDS", relationshipProperties1);

        Map<String, Object> relationshipProperties2 = new HashMap<>();
        relationshipProperties2.put("since", 2021);
        neo4jManager.createRelationship("Person", "Person", "name", "Alice", "name", "Bob", "FAMILY", relationshipProperties2);

        // check relationships exist
        List<Record> relationships = neo4jManager.queryAllRelationships();
        assertEquals(2, relationships.size());

        // delete all relationships
        neo4jManager.deleteAllRelationships("Person", "Person", null);

        // check relationships deleted
        relationships = neo4jManager.queryAllRelationships();
        assertEquals(0, relationships.size());
    }

}
