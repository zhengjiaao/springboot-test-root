# springboot-db-neo4j-manager

**开发自定义类(Neo4jManager)进行操作Neo4j图数据库, `Neo4jManager`类中存在大量方法提供使用，`Neo4jManagerTest`类提供了完整的单元测试，大大提高了理解每个方法的使用方式。**         

> springboot data neo4j 已经封装了支持neo4j图数据操作，而且效果也不错，但是有个缺点就是能不能更加灵活操作neo4j图库。
> 例如，实现动态创建节点，并动态创建属性。springboot data neo4j就很难实现了，原因是必须先创建节点类(node)和关系类(Relationship)，
> 导致开发工作量增加，当存在很多节点和关系时，维护成本会很高，当然，在特定场景中使用是很便捷的，比较好理解。


Neo4jManager.java类 部分方法展示

> `Neo4jManager`类中存在大量方法提供使用

```java
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

}

```


Neo4jManagerTest.java 单元测试类

> `Neo4jManagerTest`类提供了完整的单元测试示例，提供更友好的理解 `Neo4jManager` 类中方法的使用。


```java
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

    //创建节点

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

}

```

以上是部分代码功能演示，`Neo4jManager`类中存在很多大量方法提供使用。
