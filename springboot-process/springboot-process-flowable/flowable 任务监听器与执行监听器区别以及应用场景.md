# flowable 任务监听器与执行监听器区别以及应用场景



这是一个非常重要且核心的Flowable概念。我们来详细解析任务监听器（Task Listener）和执行监听器（Execution Listener）的区别以及它们的应用场景。

### 核心区别一览表

| 特性             | 任务监听器 (Task Listener)                                   | 执行监听器 (Execution Listener)                              |
| :--------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| **监听目标**     | **用户任务 (User Task)**                                     | **流程执行流 (Execution)**，包括开始/结束事件、序列流、网关、所有活动节点（包括Service Task、User Task等） |
| **绑定对象**     | 特定的`<userTask>`元素                                       | `process`、`startEvent`、`endEvent`、`sequenceFlow`、所有`activity`（如 `userTask`, `serviceTask`, `callActivity`等） |
| **生命周期事件** | `create`（任务创建后） `assignment`（任务指派给人后） `complete`（任务完成后） `delete`（任务删除前） | `start`（进入一个节点时） `end`（离开一个节点时） `take`（捕获序列流时） |
| **获取上下文**   | 通过 `DelegateTask` 参数 - 任务ID、名称、办理人 - 任务相关变量 - 任务所属的执行流 | 通过 `DelegateExecution` 参数 - 流程实例ID、业务键 - **当前活动节点ID** - 流程变量 |
| **本质**         | **与人交互**相关的扩展点                                     | **与流程自动执行**相关的扩展点                               |

------

### 任务监听器 (Task Listener)

#### 应用场景

任务监听器专注于**用户任务生命周期**中的事件。它的所有操作都是围绕“任务”这个核心概念展开的。

1. **动态任务分配（`create`, `assignment`）**

   - **场景**：根据流程变量或业务规则，动态计算任务的办理人、候选人或候选组。
   - **示例**：一个审批任务，根据表单中的“部门”字段，自动分配给该部门的经理。

   ```java
   public class DynamicAssigneeListener implements TaskListener {
       @Override
       public void notify(DelegateTask task) {
           String dept = (String) task.getVariable("department");
           String manager = departmentService.getManagerByDept(dept);
           task.setAssignee(manager); // 动态设置办理人
       }
   }
   ```

   *BPMN 配置:*

   ```xml
   <userTask id="approvalTask" name="部门审批">
     <extensionElements>
       <flowable:taskListener event="create" class="com.example.DynamicAssigneeListener" />
     </extensionElements>
   </userTask>
   ```

2. **任务创建时初始化数据（`create`）**

   - **场景**：任务创建时，自动设置一些默认值、计算到期时间、生成唯一业务编号等。
   - **示例**：任务创建时，自动设置一个3天后到期的dueDate。

   ```java
   task.setDueDate(Date.from(Instant.now().plus(3, ChronoUnit.DAYS)));
   ```

3. **任务完成时触发业务逻辑（`complete`）**

   - **场景**：在任务提交后、流程继续前进前，执行一些业务操作，如记录审计日志、发送通知、调用其他系统接口。
   - **示例**：记录谁在什么时间完成了什么任务。

   ```java
   public class AuditTaskCompleteListener implements TaskListener {
       @Override
       public void notify(DelegateTask task) {
           if ("complete".equals(task.getEventName())) {
               auditService.logTaskCompleted(
                   task.getId(),
                   task.getAssignee(),
                   task.getVariable("approvalComment")
               );
           }
       }
   }
   ```

4. **任务回收与委托（`assignment`）**

   - **场景**：当任务被转派（reassign）或委托（delegate）给其他人时，发送通知或记录历史。

------

### 执行监听器 (Execution Listener)

#### 应用场景

执行监听器专注于**流程执行路径**上的事件。它监听的是流程节点（而不仅仅是用户任务）的进入和离开。

1. **流程实例的开始与结束（`start`, `end`）**

   - **场景**：在流程开始时初始化全局变量、记录流程启动日志；在流程结束时进行资源清理、更新主业务状态为“已完成”或“已终止”。
   - **示例**：流程开始时，初始化一个计数器。

   ```xml
   <startEvent id="start">
     <extensionElements>
       <flowable:executionListener event="start" class="com.example.InitializeCounterListener" />
     </extensionElements>
   </startEvent>
   ```

2. **自动活动（Service Task）的环绕通知（`start`, `end`）**

   - **场景**：虽然Service Task本身包含逻辑，但执行监听器可以为其添加通用的横切关注点逻辑，如日志、性能监控、事务管理。
   - **示例**：记录某个Service Task的执行耗时。

   ```java
   public class ServiceTaskMetricsListener implements ExecutionListener {
       @Override
       public void notify(DelegateExecution execution) {
           if ("start".equals(execution.getEventName())) {
               execution.setVariable("startTime", System.currentTimeMillis());
           } else if ("end".equals(execution.getEventName())) {
               Long startTime = (Long) execution.getVariable("startTime");
               long duration = System.currentTimeMillis() - startTime;
               metricsService.recordDuration(execution.getCurrentActivityId(), duration);
           }
       }
   }
   ```

   *BPMN 配置:*

   ```xml
   <serviceTask id="callExternalSystem" name="调用外部系统" flowable:class="...">
     <extensionElements>
       <flowable:executionListener event="start" class="com.example.ServiceTaskMetricsListener" />
       <flowable:executionListener event="end" class="com.example.ServiceTaskMetricsListener" />
     </extensionElements>
   </serviceTask>
   ```

3. **基于序列流的条件路由（`take`）**

   - **场景**：监听序列流被捕获的事件，可以用于非常复杂的、表达式无法简单描述的路由逻辑。
   - **示例**：根据当前时间、系统负载等动态因素决定流程走向。

   ```xml
   <sequenceFlow id="flowDay" sourceRef="decision" targetRef="dayTask">
     <extensionElements>
       <flowable:executionListener event="take" class="com.example.DayOrNightRouteListener" />
     </extensionElements>
   </sequenceFlow>
   ```

4. **子流程的调用与结束（`start`, `end`）**

   - **场景**：在调用子流程（Call Activity）前后，进行数据的输入映射和输出映射（如果内置映射功能不满足需求），或执行其他准备/清理工作。

------

### 如何选择？决策指南

| 你的需求                                                     | 应选择的监听器                                            |
| :----------------------------------------------------------- | :-------------------------------------------------------- |
| 需要根据表单数据动态设置任务的办理人、候选组                 | **任务监听器** (`event="create"`)                         |
| 想在任务完成时记录日志、发送通知、更新业务状态               | **任务监听器** (`event="complete"`)                       |
| 想在流程开始时进行全局初始化                                 | **执行监听器** (绑在`startEvent`上, `event="start"`)      |
| 想监控一个**自动节点**（如Service Task、Call Activity）的执行性能 | **执行监听器** (绑在对应节点上, `event="start"`和`"end"`) |
| 想实现非常复杂的、超出表达式能力的流程路由逻辑               | **执行监听器** (绑在`sequenceFlow`上, `event="take"`)     |
| 想在任何节点（无论是自动节点还是用户任务节点）进入或离开时都执行一段通用逻辑（如日志） | **执行监听器** (绑在对应节点上或全局`process`上)          |

### 重要提示

1. **`DelegateExecution` vs `DelegateTask`**：
   - 在**执行监听器**中，你通过 `DelegateExecution` 对象操作流程变量和流程控制。
   - 在**任务监听器**中，你通过 `DelegateTask` 对象操作任务属性（如办理人），也可以通过 `DelegateTask.getExecution()` 获取当前的 `DelegateExecution` 来操作流程变量。
2. **性能考虑**：监听器会同步执行。如果其中的逻辑非常耗时（如调用慢速的外部API），会阻塞流程引擎的继续执行。对于耗时操作，应考虑在监听器中触发异步消息，由另一个线程异步处理。
3. **事务边界**：监听器代码在流程引擎的同一个事务中执行。如果监听器抛出异常，整个流程操作（例如完成任务）将会回滚。

总结来说，**任务监听器是关于“人”的，执行监听器是关于“流程”的**。理解这个根本区别，你就能准确地为不同的业务场景选择合适的扩展点。