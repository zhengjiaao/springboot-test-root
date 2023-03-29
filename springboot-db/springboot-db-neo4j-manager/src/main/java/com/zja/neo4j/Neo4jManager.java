/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-03-27 16:59
 * @Since:
 */
package com.zja.neo4j;

import org.neo4j.driver.*;
import org.neo4j.driver.types.Relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.neo4j.driver.Values.parameters;

/**
 * Neo4j 图库操作管理（自定义实现）
 *
 * 作用：实现灵活操作neo4j图数据库
 *
 * 已经存在 springboot data neo4j 操作neo4j图数据库封装，为什么还要自己造轮子(Neo4jManager)？
 * 原因：springboot data neo4j 不支持动态构建节点，必须要先创建好实体类(node)和关系实体类(Relationship)，才能进行构建。
 *
 * Neo4jManager支持：neo4j-java-driver 4.4.x 对应 neo4j server 4.x or 5.x
 *
 * @author: zhengja
 * @since: 2023/03/27 16:59
 */
public class Neo4jManager {
    private final Driver driver;

    public Neo4jManager(String uri) {
        this.driver = GraphDatabase.driver(uri);
    }

    public Neo4jManager(String uri, String user, String password) {
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    /**
     * 连接状态
     */
    public String connect() {
        if (this.driver != null) {
            return "已连接到Neo4j数据库";
        } else {
            return "连接失败";
        }
    }

    /**
     * 所有操作完成后，记得断开连接
     */
    public void disconnect() {
        if (this.driver != null) {
            this.driver.close();
            System.out.println("已断开与Neo4j数据库的连接");
        }
    }

    /**
     * 创建空节点标签(不建议，会创建随机一条数据)
     * @param label 节点标签
     */
    @Deprecated
    public void createEmptyNodeLabel(String label) {
        try (Session session = driver.session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                String query = "CREATE (n:" + label + ")";
                tx.run(query);
                return null;
            });
        }
    }

    /**
     * 校验存在节点标签
     */
    public boolean checkNodeLabelExists(String label) {
        try (Session session = driver.session()) {
            return session.readTransaction((TransactionWork<Boolean>) tx -> {
                String query = "MATCH (n:" + label + ") RETURN n LIMIT 1";
                Result result = tx.run(query);
                return result.hasNext();
            });
        }
    }

    /**
     * 更新节点标签名称
     * @param oldLabel 旧节点标签名称
     * @param newLabel 新节点标签名称
     */
    public void updateNodeLabelName(String oldLabel, String newLabel) {
        try (Session session = driver.session()) {
            String cypher = String.format("MATCH (n:%s) SET n:%s REMOVE n:%s", oldLabel, newLabel, oldLabel);
            session.run(cypher);
        }
    }

    /**
     * 创建节点标签
     * @param label 节点标签
     * @param properties 节点属性
     */
    public void createNode(String label, Map<String, Object> properties) throws RuntimeException {
        try (Session session = driver.session()) {
            String query = "CREATE (n:" + label + ") SET n = $properties";
            session.writeTransaction(tx -> tx.run(query, parameters("properties", properties)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create node: " + e.getMessage());
        }
    }

    /**
     * 更新节点
     * @param label 节点标签
     * @param property 节点属性
     * @param value 节点属性值
     * @param newProperties 新节点属性
     */
    public void updateNode(String label, String property, Object value, Map<String, Object> newProperties) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                String query = "MATCH (n:" + label + ") WHERE n." + property + " = $value " +
                        "SET n += $newProperties";
                tx.run(query, parameters("value", value, "newProperties", newProperties));
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to update node: " + e.getMessage());
        }
    }

    /**
     * 查询所有节点标签
     */
    public List<String> queryAllNodeLabels() {
        try (Session session = driver.session()) {
            Result result = session.run("CALL db.labels()");
            return result.stream().map(record -> record.get("label").asString()).collect(Collectors.toList());
        }
    }

    /**
     * 查询所有节点
     * @param label 节点标签
     * @param label 查询几条节点标签
     * @return 节点列表
     */
    public List<Record> queryNodes(String label, int limit) {
        try (Session session = driver.session()) {
            String query = "MATCH (n:" + label + ") RETURN n LIMIT " + limit;
            Result result = session.run(query);
            return result.list();
        }
    }

    /**
     * 根据属性查询节点
     * @param label 节点标签
     * @param property 节点属性
     * @param value 节点属性值
     * @return 节点列表
     */
    public List<Record> queryNodesByProperty(String label, String property, Object value) {
        try (Session session = driver.session()) {
            String query = "MATCH (n:" + label + ") WHERE n." + property + " = $value RETURN n";
            Result result = session.run(query, parameters("value", value));
            return result.list();
        }
    }

    /**
     * 分页查询节点
     * @param query 查询语句
     * @param parameters 查询参数
     * @param pageSize 每页大小
     * @param pageNum 页码
     * @return 节点列表
     */
    public List<Record> queryNodesByPage(String query, Map<String, Object> parameters, int pageSize, int pageNum) {
        try (Session session = driver.session()) {
            int skip = pageSize * (pageNum - 1);
            parameters.put("skip", skip);
            parameters.put("limit", pageSize);
            Result result = session.run(query + " SKIP $skip LIMIT $limit", parameters);
            return result.list();
        }
    }

    /**
     * 删除节点标签 (删除 label 中所有 node)
     * @param label 节点标签
     */
    public void deleteAllNodes(String label) {
        try (Session session = driver.session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                //DETACH 强制删除节点和节点关系
                String query = "MATCH (n:" + label + ") DETACH DELETE n";
                tx.run(query);
                return null;
            });
        }
    }

    /**
     * 删除节点
     * @param label 节点标签
     * @param property 节点属性
     * @param value 节点属性值
     */
    public void deleteNode(String label, String property, Object value) {
        try (Session session = driver.session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                String query = "MATCH (n:" + label + ") WHERE n." + property + " = $value " +
                        "DETACH DELETE n";
                tx.run(query, parameters("value", value));
                return null;
            });
        }
    }

    /**
     * 创建单节点之间的关系
     * @param label1 节点1标签
     * @param relationshipType 关系类型
     * @param propertyKey1 节点1属性键
     * @param propertyValue1 节点1属性值
     * @param propertyKey2 节点2属性键
     * @param propertyValue2 节点2属性值
     */
    public void createRelationship(String label1, String relationshipType, String propertyKey1, Object propertyValue1, String propertyKey2, Object propertyValue2) throws RuntimeException {
        try (Session session = driver.session()) {
            String query = "MATCH (a:" + label1 + " {" + propertyKey1 + ": $value1}), (b:" + label1 + " {" + propertyKey2 + ": $value2}) CREATE (a)-[r:" + relationshipType + "]->(b)";
            session.writeTransaction(tx -> tx.run(query, parameters("value1", propertyValue1, "value2", propertyValue2)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create node relationship: " + e.getMessage());
        }
    }

    /**
     * 创建两个节点之间的关系
     * @param label1 节点1标签
     * @param label2 节点2标签
     * @param relationshipType 关系类型
     * @param propertyKey1 节点1属性键
     * @param propertyValue1 节点1属性值
     * @param propertyKey2 节点2属性键
     * @param propertyValue2 节点2属性值
     */
    public void createRelationship(String label1, String label2, String relationshipType, String propertyKey1, Object propertyValue1, String propertyKey2, Object propertyValue2) throws RuntimeException {
        try (Session session = driver.session()) {
            String query = "MATCH (a:" + label1 + " {" + propertyKey1 + ": $value1}), (b:" + label2 + " {" + propertyKey2 + ": $value2}) CREATE (a)-[r:" + relationshipType + "]->(b)";
            session.writeTransaction(tx -> tx.run(query, parameters("value1", propertyValue1, "value2", propertyValue2)));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create node relationship: " + e.getMessage());
        }
    }

    /**
     * 创建关系
     * @param label1 节点1标签
     * @param label2 节点2标签
     * @param property1 节点1属性
     * @param value1 节点1属性值
     * @param property2 节点2属性
     * @param value2 节点2属性值
     * @param relationshipType 关系类型
     * @param relationshipProperties 关系属性
     */
    public void createRelationship(String label1, String label2, String property1, Object value1, String property2, Object value2, String relationshipType, Map<String, Object> relationshipProperties) {
        try (Session session = driver.session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                String query = "MATCH (a:" + label1 + ") WHERE a." + property1 + " = $value1 " +
                        "MATCH (b:" + label2 + ") WHERE b." + property2 + " = $value2 " +
                        "CREATE (a)-[r:" + relationshipType + "]->(b) SET r = $relationshipProperties";
                tx.run(query, parameters("value1", value1, "value2", value2, "relationshipProperties", relationshipProperties));
                return null;
            });
        }
    }

    /**
     * 查询两个节点的关系
     * @param label1 节点1标签
     * @param label2 节点2标签
     * @param property1 节点1属性
     * @param value1 节点1属性值
     * @param property2 节点2属性
     * @param value2 节点2属性值
     * @param relationshipType 关系类型
     */
    public List<Relationship> queryRelationshipByProperty(String label1, String label2, String property1, Object value1, String property2, Object value2, String relationshipType) {
        try (Session session = driver.session()) {
            return session.readTransaction((TransactionWork<List<Relationship>>) tx -> {
                String query = "MATCH (a:" + label1 + ") WHERE a." + property1 + " = $value1 " +
                        "MATCH (b:" + label2 + ") WHERE b." + property2 + " = $value2 " +
                        "MATCH (a)-[r:" + relationshipType + "]->(b) RETURN r";
                Result result = tx.run(query, parameters("value1", value1, "value2", value2));
                List<Relationship> relationships = new ArrayList<>();
                while (result.hasNext()) {
                    Record record = result.next();
                    Relationship relationship = record.get("r").asRelationship();
                    relationships.add(relationship);
                }
                return relationships;
            });
        }
    }

    /**
     * 查询两个节点的关系
     * @param label1 节点1标签
     * @param label2 节点2标签
     * @param property1 节点1属性
     * @param value1 节点1属性值
     * @param property2 节点2属性
     * @param value2 节点2属性值
     */
    public List<Relationship> queryRelationshipByProperty(String label1, String label2, String property1, Object value1, String property2, Object value2) {
        try (Session session = driver.session()) {
            return session.readTransaction((TransactionWork<List<Relationship>>) tx -> {
                String query = "MATCH (a:" + label1 + ") WHERE a." + property1 + " = $value1 " +
                        "MATCH (b:" + label2 + ") WHERE b." + property2 + " = $value2 " +
                        "MATCH (a)-[r]->(b) RETURN r";
                Result result = tx.run(query, parameters("value1", value1, "value2", value2));
                List<Relationship> relationships = new ArrayList<>();
                while (result.hasNext()) {
                    Record record = result.next();
                    Relationship relationship = record.get("r").asRelationship();
                    relationships.add(relationship);
                }
                return relationships;
            });
        }
    }

    /**
     * 查询节点的关系类型
     * @param label 节点标签
     * @return 节点的关系类型列表
     */
    public List<String> queryNodeRelationships(String label) {
        List<String> relationships = new ArrayList<>();
        try (Session session = driver.session()) {
            session.readTransaction((TransactionWork<Void>) tx -> {
                String query = "MATCH (a:" + label + ")-[r]->() RETURN DISTINCT type(r)";
                Result result = tx.run(query);
                while (result.hasNext()) {
                    Record record = result.next();
                    relationships.add(record.get("type(r)").asString());
                }
                return null;
            });
        }
        return relationships;
    }

    /**
     * 统计节点的关系类型数量
     * @param label 节点标签
     * @return 节点的关系类型数量映射
     */
    public Map<String, Integer> countNodeRelationships(String label) {
        Map<String, Integer> relationshipCounts = new HashMap<>();
        try (Session session = driver.session()) {
            session.readTransaction((TransactionWork<Void>) tx -> {
                String query = "MATCH (a:" + label + ")-[r]->() RETURN DISTINCT type(r), count(r) as count";
                Result result = tx.run(query);
                while (result.hasNext()) {
                    Record record = result.next();
                    relationshipCounts.put(record.get("type(r)").asString(), record.get("count").asInt());
                }
                return null;
            });
        }
        return relationshipCounts;
    }

    /**
     * 获取两个节点之间的关系类型  [起始节点 --> 结束节点]
     * @param label1 节点1标签 起始节点
     * @param label2 节点2标签 结束节点
     * @return 两个节点之间的关系类型列表
     */
    public List<String> queryRelationshipsBetweenNodes(String label1, String label2) {
        List<String> relationships = new ArrayList<>();
        try (Session session = driver.session()) {
            session.readTransaction((TransactionWork<Void>) tx -> {
                String query = "MATCH (a:" + label1 + ")-[r]->(b:" + label2 + ") RETURN DISTINCT type(r)";
                Result result = tx.run(query);
                while (result.hasNext()) {
                    Record record = result.next();
                    relationships.add(record.get("type(r)").asString());
                }
                return null;
            });
        }
        return relationships;
    }

    /**
     * 删除关系
     * @param label1 节点1标签
     * @param label2 节点2标签
     * @param relationshipType 关系类型
     */
    public void deleteAllRelationships(String label1, String label2, String relationshipType) {
        try (Session session = driver.session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                String query = "MATCH (a:" + label1 + ")-[r:" + relationshipType + "]->(b:" + label2 + ") " +
                        "DELETE r";
                tx.run(query);
                return null;
            });
        }
    }

    /**
     * 删除关系
     * @param label1 节点1标签
     * @param label2 节点2标签
     * @param property1 节点1属性
     * @param property2 节点2属性
     */
    public void deleteRelationship(String label1, String label2, String property1, String property2) {
        try (Session session = driver.session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                String query = "MATCH (a:" + label1 + ")-[r]->(b:" + label2 + ") " +
                        "WHERE a." + property1 + " = $prop1 AND b." + property2 + " = $prop2 " +
                        "DELETE r";
                tx.run(query, parameters("prop1", property1, "prop2", property2));
                return null;
            });
        }
    }

    /**
     * 删除关系
     * @param label1 节点1标签
     * @param label2 节点2标签
     * @param property1 节点1属性
     * @param value1 节点1属性值
     * @param property2 节点2属性
     * @param value2 节点2属性值
     * @param relationshipType 关系类型
     */
    public void deleteRelationship(String label1, String label2, String property1, Object value1, String property2, Object value2, String relationshipType) {
        try (Session session = driver.session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                String query = "MATCH (a:" + label1 + ")-[r:" + relationshipType + "]->(b:" + label2 + ") " +
                        "WHERE a." + property1 + " = $value1 AND b." + property2 + " = $value2 " +
                        "DELETE r";
                tx.run(query, parameters("value1", value1, "value2", value2));
                return null;
            });
        }
    }

    /**
     * 删除关系
     * @param label1 节点1标签
     * @param label2 节点2标签
     * @param relationshipType 关系类型
     * @param relationshipProperties 关系属性
     */
    public void deleteRelationship(String label1, String label2, String relationshipType, Map<String, Object> relationshipProperties) {
        try (Session session = driver.session()) {
            session.writeTransaction((TransactionWork<Void>) tx -> {
                String query = "MATCH (a:" + label1 + ")-[r:" + relationshipType + "]->(b:" + label2 + ") " +
                        "WHERE r = $relationshipProperties " +
                        "DELETE r";
                tx.run(query, parameters("relationshipProperties", relationshipProperties));
                return null;
            });
        }
    }

    /**
     * 查询所有节点
     * @return 节点列表
     */
    public List<Record> queryAllNodes() {
        try (Session session = driver.session()) {
            String query = "MATCH (n) RETURN n";
            Result result = session.run(query);
            return result.list();
        }
    }

    /**
     * 查询所有节点个数
     * @return 节点个数
     */
    public int queryAllNodesCount() {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (n) RETURN count(*) as count");
            return result.single().get("count").asInt();
        }
    }

    /**
     * 查询所有关系
     * @return 关系列表
     */
    public List<Record> queryAllRelationships() {
        try (Session session = driver.session()) {
            String query = "MATCH ()-[r]->() RETURN r";
            Result result = session.run(query);
            return result.list();
        }
    }

    /**
     * 查询所有关系个数
     * @return 所有关系个数
     */
    public int queryAllRelationshipCount() {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH ()-->() RETURN count(*) as count");
            return result.single().get("count").asInt();
        }
    }

    /**
     * 查询所有节点和关系
     * @return 节点和关系列表
     */
    public List<Record> queryAllNodesAndRelationships() {
        try (Session session = driver.session()) {
            String query = "MATCH (n)-[r]->(m) RETURN n,r,m";
            Result result = session.run(query);
            return result.list();
        }
    }


}
