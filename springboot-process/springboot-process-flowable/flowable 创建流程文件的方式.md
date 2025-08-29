创建 `process.bpmn20.xml` 流程文件的方式主要有以下三类，涵盖手工编写、可视化设计及程序化生成：

------

### 一、**手工编写 XML 文件**

适用于熟悉 BPMN 2.0 规范的用户，直接通过代码编辑器编写。
**步骤示例** 137：

1. **创建文件**：新建 XML 文件，命名为 `process.bpmn20.xml` 或 `process.bpmn`（后缀必须正确）。

2. **定义基础结构**：

   ```xml
   <definitions 
     xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
     xmlns:flowable="http://flowable.org/bpmn"
     targetNamespace="CustomNamespace">
     <process id="myProcess" name="示例流程">
       <!-- 流程元素（如开始事件、用户任务、结束事件） -->
       <startEvent id="start" />
       <userTask id="task1" name="审批任务" />
       <sequenceFlow sourceRef="start" targetRef="task1" />
       <endEvent id="end" />
       <sequenceFlow sourceRef="task1" targetRef="end" />
     </process>
   </definitions>
   ```

3. **关键属性**：

   - `id`：流程唯一标识，用于通过 `RuntimeService.startProcessInstanceByKey("myProcess")` 启动流程 27。
   - `targetNamespace`：分类流程实例的命名空间（可自定义）。

------

### 二、**使用可视化设计器工具**

适合图形化设计流程，自动生成 XML。

#### 1. **Eclipse/IDEA 插件**

- **Eclipse**：
  - 安装插件：`Help → Install New Software` → 输入地址 `http://www.flowable.org/designer/update/` 468。
  - 创建流程：`File → New → Flowable Project` → 右键目录创建 `Flowable Diagram`，设计后保存自动生成 `.bpmn20.xml`。
- **IDEA**：
  - 安装 Flowable 插件 → 右键项目 `New → Flowable BPMN 2.0 File` → 通过图形界面拖拽设计 48。

#### 2. **Flowable Modeler（Web 版）**

方式一：下载并部署 flowable-ui（含Flowable Modeler）：
- 部署 Flowable UI：将 `flowable-ui.war` 放入 Tomcat 的 `webapps` 目录并启动 48。
- 访问 `http://localhost:8080/flowable-ui` → 登录后进入 **Modeler 应用** → 创建流程模型 → 设计完成后直接导出为 `.bpmn20.xml` 文件 8。

方式二：springboot 集成 Flowable Modeler：
- 添加依赖：
```xml
<dependency>
    <groupId>org.flowable</groupId>
    <artifactId>flowable-spring-boot-starter-ui-idm</artifactId>
    <version>${flowable.version}</version>
</dependency>
<dependency>
    <groupId>org.flowable</groupId>
    <artifactId>flowable-spring-boot-starter-ui-modeler</artifactId>
    <version>${flowable.version}</version>
</dependency>
```
- 访问：http://localhost:8080/

#### 3. **Web 版设计工具**

- 如 **bpmn.io**：在线设计 BPMN 流程图，导出为标准 BPMN 2.0 XML。

------

### 三、**通过代码动态生成**

适用于流程需动态创建的场景（如集成到 Spring Boot 应用）。

#### 1. **从流程模型生成**

```java
@Service
public class BpmnGeneratorService {
    @Autowired
    private RepositoryService repositoryService;

    public String generateBpmnXml(String modelId) throws Exception {
        Model model = repositoryService.getModel(modelId);
        byte[] modelJson = repositoryService.getModelEditorSource(model.getId());
        JsonNode modelNode = new ObjectMapper().readTree(modelJson);
        // 转换为 BPMN 模型
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        // 生成 XML
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
        return new String(bpmnBytes, "UTF-8");
    }
}
```

**适用场景**：在流程设计器中保存的模型需转换为可部署的 XML 。

#### 2. **从已部署流程导出**

```java
InputStream xmlStream = repositoryService.getResourceAsStream(
    deploymentId, processDefinition.getResourceName()
);
```

**用途**：获取已部署流程的原始 XML 文件（例如备份或迁移）。

------

### **对比总结**

| **方式**     | **适用场景**               | **工具/技术**                       | **特点**                 |
| :----------- | :------------------------- | :---------------------------------- | :----------------------- |
| 手工编写 XML | 简单流程或熟悉 BPMN 的用户 | 文本编辑器（如 VS Code）            | 灵活，无需依赖工具       |
| 可视化设计器 | 复杂流程的图形化设计       | Eclipse/IDEA 插件、Flowable Modeler | 直观，支持拖拽和自动校验 |
| 程序动态生成 | 流程动态创建或系统集成     | Flowable API + Spring Boot          | 自动化，适合二次开发     |

------

**选择建议**：

- **快速原型设计** → 使用 **Flowable Modeler** 或 **Eclipse 插件** 。
- **集成到应用系统** → 采用 **代码生成** 方式 。
- **调试或学习 BPMN 语法** → 手工编写 XML 。
