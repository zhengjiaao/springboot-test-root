3. ### Flowable Spring Boot Starter 核心依赖介绍
   
   **依赖项**
   
   ```xml
   <dependency>
       <groupId>org.flowable</groupId>
       <artifactId>flowable-spring-boot-starter</artifactId>
       <version>6.8.1</version>
   </dependency>
   ```
   
   ------
   
   ### **核心功能概述**
   
   这是 Flowable 工作流引擎的 **核心 Spring Boot 启动器**，提供以下关键能力：
   
   1. **流程引擎自动配置** - 自动创建 `ProcessEngine`
   2. **数据库集成** - 自动管理流程表结构（支持版本迁移）
   3. **Spring 事务集成** - 与 `@Transactional` 无缝协作
   4. **服务层注入** - 自动暴露 Flowable 核心服务 Bean
   5. **事件监听** - 通过 Spring Bean 监听流程事件
   6. **异步执行器** - 自动启动作业执行器（定时任务/异步任务）
   
   > 注意：此依赖 **不包含 UI 界面和 REST API**（需额外添加 UI 模块）
   
   ------
   
   ### **核心服务接口**
   
   通过此 starter 会自动注入以下 Spring Bean：
   
   | **服务类**          | **功能**              | **常用方法示例**                           |
   | :------------------ | :-------------------- | :----------------------------------------- |
   | `RepositoryService` | 流程部署与定义管理    | `createDeployment()`, `getBpmnModel()`     |
   | `RuntimeService`    | 流程实例操作          | `startProcessInstanceByKey()`, `trigger()` |
   | `TaskService`       | 用户任务管理          | `complete()`, `createTaskQuery()`          |
   | `HistoryService`    | 历史数据查询          | `createHistoricProcessInstanceQuery()`     |
   | `ManagementService` | 引擎维护与管理        | `executeCommand()`, `getTableCount()`      |
   | `IdentityService`   | 用户/组管理（基础版） | `saveUser()`, `createMembership()`         |
   
   ------
   
   ### **典型应用场景**
   
   #### 1. 业务流程自动化
   
   ```java
   // 启动请假流程
   public void startLeaveProcess(String employeeId, int days) {
       Map<String, Object> variables = new HashMap<>();
       variables.put("employee", employeeId);
       variables.put("days", days);
       
       runtimeService.startProcessInstanceByKey("leaveApproval", variables);
   }
   ```
   
   #### 2. 自定义任务分配
   
   ```java
   // 实现动态任务分配
   @EventListener
   public void onTaskCreated(DelegateTask task) {
       if ("经理审批".equals(task.getName())) {
           String dept = (String) task.getVariable("department");
           task.setAssignee(managerService.findDeptManager(dept));
       }
   }
   ```
   
   #### 3. 流程状态监控
   
   ```java
   // 查询进行中的采购流程
   public List<ProcessInstance> getActivePurchases() {
       return runtimeService.createProcessInstanceQuery()
           .processDefinitionKey("purchaseProcess")
           .active()
           .list();
   }
   ```
   
   #### 4. 集成 Spring 事务
   
   ```java
   @Transactional
   public void approveOrderWithInventoryUpdate(String taskId) {
       // 1. 完成任务
       taskService.complete(taskId);
       
       // 2. 更新库存系统（与工作流在同一个事务中）
       inventoryService.reduceStock(orderId);
   }
   ```
   
   ------
   
   ### **使用方式（Spring Boot）**
   
   #### 1. 基础配置（pom.xml）
   
   ```xml
   <dependencies>
       <!-- 核心引擎 -->
       <dependency>
           <groupId>org.flowable</groupId>
           <artifactId>flowable-spring-boot-starter</artifactId>
           <version>6.8.1</version>
       </dependency>
       
       <!-- 数据库（以MySQL为例） -->
       <dependency>
           <groupId>mysql</groupId>
           <artifactId>mysql-connector-java</artifactId>
       </dependency>
   </dependencies>
   ```
   
   #### 2. 配置文件（application.yml）
   
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/flowable_db
       username: flowable
       password: secret
       driver-class-name: com.mysql.cj.jdbc.Driver
   
   flowable:
     database-schema-update: true  # 自动更新表结构
     async-executor-activate: true # 启用异步执行器
     history-level: audit         # 历史记录级别
   ```
   
   #### 3. 流程部署（自动扫描）
   
   将BPMN文件放入 `src/main/resources/processes/`，启动时自动部署：
   
   ```text
   resources/
   ├── processes/
   │   ├── leave-approval.bpmn20.xml
   │   └── purchase-process.bpmn20.xml
   ```
   
   ------
   
   ### **核心配置参数说明**
   
   | **参数**                                      | **默认值**              | **作用**                                 |
   | :-------------------------------------------- | :---------------------- | :--------------------------------------- |
   | `flowable.database-schema-update`             | `false`                 | 表结构策略：true（自动更新）/create-drop |
   | `flowable.async-executor-activate`            | `false`                 | 是否启用异步任务执行器                   |
   | `flowable.history-level`                      | `none`                  | 历史记录级别：none/activity/audit/full   |
   | `flowable.process-definition-location-prefix` | `classpath:/processes/` | BPMN文件扫描路径                         |
   
   ------
   
   ### **高级用法示例**
   
   #### 1. 自定义流程事件监听
   
   ```java
   @Component
   public class FlowableEventListener {
       // 监听流程结束事件
       @EventListener
       public void onProcessCompleted(FlowableProcessEndedEvent event) {
           String processId = event.getProcessInstanceId();
           auditService.logProcessCompletion(processId);  // 自定义审计逻辑
       }
   }
   ```
   
   #### 2. 使用命令拦截器
   
   ```java
   @Configuration
   public class FlowableConfig {
       @Bean
       public CommandInterceptor customInterceptor() {
           return new AbstractCommandInterceptor() {
               @Override
               public <T> T execute(CommandConfig config, Command<T> command) {
                   log.debug("执行Flowable命令: " + command.getClass());
                   return next.execute(config, command);
               }
           };
       }
   }
   ```
   
   #### 3. 多数据源配置
   
   ```java
   @Bean
   @Primary
   @ConfigurationProperties("spring.datasource")
   public DataSource primaryDataSource() {
       return DataSourceBuilder.create().build();
   }
   
   @Bean
   @ConfigurationProperties("flowable.datasource")
   public DataSource flowableDataSource() {
       return DataSourceBuilder.create().build();
   }
   
   @Bean
   public SpringProcessEngineConfiguration processEngineConfig() {
       SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
       config.setDataSource(flowableDataSource());
       config.setTransactionManager(flowableTransactionManager());
       return config;
   }
   ```
   
   ------
   
   ### **注意事项**
   
   1. **版本兼容性**
   
      - 6.8.1 需要 JDK 8+ 和 Spring Boot 2.x
      - 生产环境建议使用最新稳定版（当前最新为 7.x）
   
   2. **性能优化**
   
      ```yaml
      flowable:
        async-executor:
          core-pool-size: 10
          max-pool-size: 50
          queue-size: 1000  # 调整异步执行器参数
      ```
   
   3. **安全建议**
   
      - 避免在前端直接暴露 `RuntimeService`/`TaskService`
      - 对流程操作添加业务层权限校验
      - 敏感数据不要存储在流程变量中
   
   4. **历史数据清理**
   
      ```java
      // 配置自动清理历史数据
      historyService.createHistoricProcessInstanceQuery()
          .finishedBefore(new Date(System.currentTimeMillis() - (30 * 24 * 3600 * 1000))) // 清理30天前的
          .delete();
      ```
   
   ------
   
   通过此核心依赖，您可以在 Spring Boot 应用中快速集成企业级工作流引擎，实现复杂的业务流程管理和自动化，同时保持与 Spring 生态系统的无缝集成。
