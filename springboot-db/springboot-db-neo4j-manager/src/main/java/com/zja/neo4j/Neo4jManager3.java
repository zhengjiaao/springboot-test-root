///**
// * @Company: 上海数慧系统技术有限公司
// * @Department: 数据中心
// * @Author: 郑家骜[ào]
// * @Email: zhengja@dist.com.cn
// * @Date: 2023-03-25 22:16
// * @Since:
// */
//package com.zja.neo4j;
//
//import org.neo4j.graphdb.*;
//import org.neo4j.graphdb.factory.GraphDatabaseFactory;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///** neo4j-java-driver 3.5.x 对应 neo4j server 3.x
// * Neo4j数据库管理类
// */
//public class Neo4jManager3 {
//
//    private GraphDatabaseService graphDb;
//    private static final String DB_PATH = "data/graph.db";
//
//    public Neo4jManager3() {
//        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(new File(DB_PATH));
//        registerShutdownHook(graphDb);
//    }
//
//    private void registerShutdownHook(final GraphDatabaseService graphDb) {
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {
//                graphDb.shutdown();
//            }
//        });
//    }
//
//    /**
//     * 创建节点
//     * @param label 节点标签
//     * @param properties 节点属性
//     * @return 创建的节点
//     * @example
//     * Neo4jManager manager = new Neo4jManager();
//     * Map<String, Object> properties = new HashMap<>();
//     * properties.put("name", "John Doe");
//     * properties.put("age", 30);
//     * Label label = Label.label("Person");
//     * Node node = manager.createNode(label, properties);
//     */
//    public Node createNode(Label label, Map<String, Object> properties) {
//        Node node;
//        try (Transaction tx = graphDb.beginTx()) {
//            node = graphDb.createNode(label);
//            for (Map.Entry<String, Object> entry : properties.entrySet()) {
//                node.setProperty(entry.getKey(), entry.getValue());
//            }
//            tx.success();
//        }
//        return node;
//    }
//
//
//    // 创建两个节点并创建关系
//    // public void createNode() {
//    //     Node node1 = createNode(Label.label("Person"), Collections.singletonMap("name", "Alice"));
//    //     Node node2 = createNode(Label.label("Person"), Collections.singletonMap("name", "Bob"));
//    //     createRelationship(String.valueOf(node1.getId()), String.valueOf(node2.getId()), RelationshipType.withName("FRIEND"));
//    // }
//
//
//    /**
//     * 更新节点
//     * @param nodeId 节点id
//     * @param properties 节点属性
//     * @example
//     * Neo4jManager manager = new Neo4jManager();
//     * Map<String, Object> properties = new HashMap<>();
//     * properties.put("name", "John Doe");
//     * properties.put("age", 30);
//     * manager.updateNode("1", properties);
//     */
//    public void updateNode(String nodeId, Map<String, Object> properties) {
//        try (Transaction tx = graphDb.beginTx()) {
//            Node node = graphDb.getNodeById(nodeId);
//            for (Map.Entry<String, Object> entry : properties.entrySet()) {
//                node.setProperty(entry.getKey(), entry.getValue());
//            }
//            tx.success();
//        }
//    }
//
//    /**
//     * 查询节点
//     * @param nodeId 节点id
//     * @return 查询到的节点
//     * @example
//     * Neo4jManager manager = new Neo4jManager();
//     * Node node = manager.getNode("1");
//     */
//    public Node getNode(String nodeId) {
//        try (Transaction tx = graphDb.beginTx()) {
//            Node node = graphDb.getNodeById(nodeId);
//            tx.success();
//            return node;
//        }
//    }
//
//    // 分页查询
//    public List<Node> getNodes(int pageNumber, int pageSize) {
//        try (Transaction tx = graphDb.beginTx()) {
//            ResourceIterator<Node> result = graphDb.findNodes(Label.label("Node"));
//            List<Node> nodes = new ArrayList<>();
//            int count = 0;
//            while (result.hasNext()) {
//                Node node = result.next();
//                if (count >= (pageNumber - 1) * pageSize && count < pageNumber * pageSize) {
//                    nodes.add(node);
//                }
//                count++;
//            }
//            tx.success();
//            return nodes;
//        }
//    }
//
//    /**
//     * 按属性查询节点并分页
//     * @param propertyName 属性名
//     * @param propertyValue 属性值
//     * @param pageNumber 页码
//     * @param pageSize 每页大小
//     * @return 查询到的节点列表
//     * @example
//     * Neo4jManager manager = new Neo4jManager();
//     * List<Node> nodes = manager.getNodesByProperty("name", "John Doe", 1, 10);
//     */
//    public List<Node> getNodesByProperty(String propertyName, Object propertyValue, int pageNumber, int pageSize) {
//        try (Transaction tx = graphDb.beginTx()) {
//            ResourceIterator<Node> result = graphDb.findNodes(Label.label("Node"), propertyName, propertyValue);
//            List<Node> nodes = new ArrayList<>();
//            int count = 0;
//            while (result.hasNext()) {
//                Node node = result.next();
//                if (count >= (pageNumber - 1) * pageSize && count < pageNumber * pageSize) {
//                    nodes.add(node);
//                }
//                count++;
//            }
//            tx.success();
//            return nodes;
//        }
//    }
//
//    /**
//     * 删除节点
//     * @param nodeId 节点id
//     * @example
//     * Neo4jManager manager = new Neo4jManager();
//     * manager.deleteNode("1");
//     */
//    public void deleteNode(String nodeId) {
//        try (Transaction tx = graphDb.beginTx()) {
//            Node node = graphDb.executeTransactionally().getNodeById(nodeId);
//            node.delete();
//            tx.success();
//        }
//    }
//
//    /**
//     * 创建关系
//     * @param startNodeId 起始节点id
//     * @param endNodeId 终止节点id
//     * @param type 关系类型
//     * @example
//     * Neo4jManager manager = new Neo4jManager();
//     * manager.createRelationship("1", "2", RelationshipType.withName("FRIEND"));
//     */
//    public void createRelationship(String startNodeId, String endNodeId, RelationshipType type) {
//        try (Transaction tx = graphDb.beginTx()) {
//            Node startNode = graphDb.getNodeById(startNodeId);
//            Node endNode = graphDb.getNodeById(endNodeId);
//            Relationship relationship = startNode.createRelationshipTo(endNode, type);
//            tx.success();
//        }
//    }
//
//    /**
//     * 查询所有节点和关系
//     * @example
//     * Neo4jManager manager = new Neo4jManager();
//     * manager.getAllNodesAndRelationships();
//     */
//    public void getAllNodesAndRelationships() {
//        try (Transaction tx = graphDb.beginTx()) {
//            ResourceIterator<Node> result = graphDb.getAllNodes().iterator();
//            while (result.hasNext()) {
//                Node node = result.next();
//                System.out.println("Node: " + node.getProperty("name"));
//                for (Relationship relationship : node.getRelationships()) {
//                    System.out.println("Relationship: " + relationship.getType().name() + " to " + relationship.getOtherNode(node).getProperty("name"));
//                }
//            }
//            tx.success();
//        }
//    }
//
//
//    /**
//     * 查询两个节点的所有关系
//     * @param startNodeId 起始节点id
//     * @param endNodeId 终止节点id
//     * @return 查询到的关系列表
//     * @example
//     * Neo4jManager manager = new Neo4jManager();
//     * List<Map<String, Object>> relationships = manager.getNodeRelationships("1", "2");
//     */
//    public List<Map<String, Object>> getNodeRelationships(String startNodeId, String endNodeId) {
//        List<Map<String, Object>> relationships = new ArrayList<>();
//        try (Transaction tx = graphDb.beginTx()) {
//            Node startNode = graphDb.getNodeById(startNodeId);
//            Node endNode = graphDb.getNodeById(endNodeId);
//            for (Relationship relationship : startNode.getRelationships()) {
//                if (relationship.getOtherNode(startNode).equals(endNode)) {
//                    Map<String, Object> relationshipMap = new HashMap<>();
//                    relationshipMap.put("startNode", relationship.getStartNode().getId());
//                    relationshipMap.put("endNode", relationship.getEndNode().getId());
//                    relationshipMap.put("type", relationship.getType().name());
//                    relationships.add(relationshipMap);
//                }
//            }
//            tx.success();
//        }
//        return relationships;
//    }
//
//    /**
//     * 查询某个节点的所有关系
//     * @param nodeId 节点id
//     * @example
//     * Neo4jManager manager = new Neo4jManager();
//     * manager.getNodeRelationships("1");
//     */
//    public List<Map<String, Object>> getNodeRelationships(String nodeId) {
//        List<Map<String, Object>> relationships = new ArrayList<>();
//        try (Transaction tx = graphDb.beginTx()) {
//            Node node = graphDb.getNodeById(nodeId);
//            System.out.println("Node: " + node.getProperty("name"));
//            for (Relationship relationship : node.getRelationships()) {
//                System.out.println("Relationship: " + relationship.getType().name() + " to " + relationship.getOtherNode(node).getProperty("name"));
//                Map<String, Object> relationshipMap = new HashMap<>();
//                relationshipMap.put("startNode", relationship.getStartNode().getId());
//                relationshipMap.put("endNode", relationship.getEndNode().getId());
//                relationshipMap.put("type", relationship.getType().name());
//                relationships.add(relationshipMap);
//            }
//            tx.success();
//        }
//        return relationships;
//    }
//
//    /**
//     * 查询某个节点的某几类关系
//     * @param nodeId 节点id
//     * @param types 关系类型
//     * @example
//     * Neo4jManager manager = new Neo4jManager();
//     * manager.getNodeRelationshipsByType("1", RelationshipType.withName("FRIEND"));
//     */
//    public List<Map<String, Object>> getNodeRelationshipsByType2(String nodeId, RelationshipType... types) {
//        List<Map<String, Object>> relationships = new ArrayList<>();
//        try (Transaction tx = graphDb.beginTx()) {
//            Node node = graphDb.getNodeById(nodeId);
//            System.out.println("Node: " + node.getProperty("name"));
//            for (Relationship relationship : node.getRelationships(types)) {
//                System.out.println("Relationship: " + relationship.getType().name() + " to " + relationship.getOtherNode(node).getProperty("name"));
//                Map<String, Object> relationshipMap = new HashMap<>();
//                relationshipMap.put("startNode", relationship.getStartNode().getId());
//                relationshipMap.put("endNode", relationship.getEndNode().getId());
//                relationshipMap.put("type", relationship.getType().name());
//                relationships.add(relationshipMap);
//            }
//            tx.success();
//        }
//        return relationships;
//    }
//
//
//    /**
//     * 更新关系
//     * @param relationshipId 关系id
//     * @param newType 新的关系类型
//     * @example
//     * Neo4jManager manager = new Neo4jManager();
//     * manager.updateRelationship("1", RelationshipType.withName("FRIEND"));
//     */
//    public void updateRelationship(String relationshipId, RelationshipType newType) {
//        try (Transaction tx = graphDb.beginTx()) {
//            Relationship relationship = tx.getRelationshipById(relationshipId);
//            relationship.setType(newType);
//            tx.success();
//        }
//    }
//
//    /**
//     * 删除关系
//     * @param relationshipId 关系id
//     * @example
//     * Neo4jManager manager = new Neo4jManager();
//     * manager.deleteRelationship("1");
//     */
//    public void deleteRelationship(String relationshipId) {
//        try (Transaction tx = graphDb.beginTx()) {
//            Relationship relationship = tx.getRelationshipById(relationshipId);
//            relationship.delete();
//            tx.success();
//        }
//    }
//
//}
