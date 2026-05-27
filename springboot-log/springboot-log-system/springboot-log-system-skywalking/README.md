# springboot-log-system-skywalking

**Apache SkyWalking 分布式链路追踪集成示例**

通过 SkyWalking Java Agent 实现无侵入链路追踪，结合 Toolkit SDK（`@Trace`、`@Tag`、`TraceContext`、`ActiveSpan`）进行手动埋点，日志通过 gRPC 上报到 SkyWalking OAP Server，在 SkyWalking UI 中查看服务拓扑、链路详情和日志关联。

## 架构流程

```
SpringBoot App + SkyWalking Agent
    ├→ (gRPC) → SkyWalking OAP Server → 存储层(ES/H2/MySQL)
    └→ SkyWalking UI (Dashboard/Topology/Trace/Log)
```

## SkyWalking 核心组件

| 组件 | 说明 |
|------|------|
| SkyWalking Agent | Java Agent，无侵入自动埋点 |
| SkyWalking OAP Server | 数据收集和分析后端 |
| SkyWalking UI | 可视化界面（拓扑图、链路、日志） |
| 存储层 | ES / H2 / MySQL / BanyanDB |

## 两种使用方式

| 方式 | 说明 | 推荐度 |
|------|------|--------|
| Java Agent（无侵入） | 启动时挂载 Agent，自动追踪 HTTP/RPC/DB 等 | 推荐 |
| Toolkit SDK（手动埋点） | `@Trace`/`@Tag` 注解，适合自定义业务追踪 | 配合 Agent 使用 |

## 核心依赖

| 依赖 | 说明 |
|------|------|
| `spring-boot-starter-web` | Web 服务 |
| `apm-toolkit-logback-1.x` | SkyWalking Logback 集成，日志中自动注入 traceId |
| `apm-toolkit-trace` | 提供 `@Trace` 注解和 `TraceContext` API |

## 快速开始

### 步骤一：部署 SkyWalking 环境

```bash
# 1. 启动 Elasticsearch（作为存储后端）
docker run -d --name elasticsearch \
  -p 9200:9200 \
  -e "discovery.type=single-node" \
  elasticsearch:7.17.0

# 2. 启动 SkyWalking OAP Server
docker run -d --name skywalking-oap \
  -p 11800:11800 -p 12800:12800 \
  -e SW_STORAGE=elasticsearch \
  -e SW_STORAGE_ES_CLUSTER_NODES=elasticsearch:9200 \
  apache/skywalking-oap-server:9.5.0

# 3. 启动 SkyWalking UI
docker run -d --name skywalking-ui \
  -p 8088:8080 \
  -e SW_OAP_ADDRESS=http://skywalking-oap:12800 \
  apache/skywalking-ui:9.5.0
```

### 步骤二：下载 SkyWalking Agent

```bash
# 下载 Agent 包
wget https://archive.apache.org/dist/skywalking/java-agent/8.16.0/apache-skywalking-java-agent-8.16.0.tgz
tar -xzf apache-skywalking-java-agent-8.16.0.tgz
```

### 步骤三：理解日志配置

`logback-spring.xml` 关键配置：

- **CONSOLE**：使用 `TraceIdPatternLogbackLayout`，日志中 `%tid` 自动替换为 SkyWalking traceId
- **FILE**：文件输出，同样包含 traceId
- **SW_GRPC_LOG**：通过 `GRPCLogClientAppender` 将日志通过 gRPC 上报到 OAP Server

### 步骤四：启动应用（挂载 Agent）

```bash
# 方式一：Java Agent 启动（推荐）
java -javaagent:/path/to/skywalking-agent/skywalking-agent.jar \
     -Dskywalking.agent.service_name=springboot-log-system-skywalking \
     -Dskywalking.collector.backend_service=127.0.0.1:11800 \
     -jar target/springboot-log-system-skywalking.jar

# 方式二：不挂载 Agent 直接运行（@Trace 注解和 TraceContext 可编译运行，但 traceId 为空）
mvn spring-boot:run
```

### 步骤五：测试接口

```bash
# 1. 发送邮件通知（@Trace + @Tag 手动埋点）
GET http://localhost:8080/api/skywalking/notify/email?target=test@example.com&content=你好

# 2. 获取当前 traceId
GET http://localhost:8080/api/skywalking/traceId

# 3. 模拟完整业务调用链（多次服务调用）
GET http://localhost:8080/api/skywalking/full-chain?userId=U001
```

### 步骤六：在 SkyWalking UI 中查看

1. 打开 SkyWalking UI：`http://localhost:8088`
2. **Dashboard**：查看服务概览和关键指标
3. **Topology**：查看服务拓扑图和调用关系
4. **Trace**：通过 traceId 查看完整调用链详情
5. **Log**：查看与链路关联的日志

## Toolkit SDK 使用说明

```java
// @Trace 注解：将方法加入链路追踪
@Trace
public void sendEmail(String target, String content) { ... }

// @Tag 注解：记录方法参数到 Span
@Trace
@Tag(key = "userId", value = "arg[0]")
public void processUser(String userId) { ... }

// TraceContext：获取当前 traceId
String traceId = TraceContext.traceId();

// ActiveSpan：添加自定义标签和错误标记
ActiveSpan.tag("orderId", orderId);
ActiveSpan.error(exception);
```

## 业务场景说明

- **NotificationService**：模拟通知发送服务（邮件/短信/推送）
- 使用 `@Trace` + `@Tag` 手动埋点，记录通知类型和目标
- 通过 `TraceContext.traceId()` 获取 traceId 并返回给调用方
- 日志通过 gRPC 上报，在 SkyWalking UI 中可查看日志与链路的关联

## 项目结构

```
springboot-log-system-skywalking/
├── pom.xml
└── src/main/
    ├── java/com/zja/log/skywalking/
    │   ├── SkyWalkingApplication.java      # 启动类
    │   ├── controller/
    │   │   └── SkyWalkingController.java   # 测试接口（通知发送、traceId、完整调用链）
    │   └── service/
    │       └── NotificationService.java    # 通知服务（@Trace/@Tag 手动埋点）
    └── resources/
        ├── application.yml                 # 应用配置
        └── logback-spring.xml              # 日志配置（TraceId + gRPC 上报）
```
