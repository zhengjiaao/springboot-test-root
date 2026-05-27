# springboot-log-system-trace

**Spring Cloud Sleuth + Zipkin 分布式链路追踪集成示例**

通过 Spring Cloud Sleuth 自动在日志中注入 `traceId/spanId`，并通过 HTTP Header（B3 格式）跨服务传播链路信息。结合 Zipkin 收集链路数据，提供可视化 UI 展示完整调用链和服务依赖关系。

## 架构流程

```
服务A (Sleuth自动埋点) → RestTemplate/Feign (B3 Header传播) → 服务B (Sleuth自动埋点)
        ↓                                                            ↓
    Zipkin Server ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ← ←
        ↓
    Zipkin UI (链路详情 + 依赖图)
```

## 核心概念

| 概念 | 说明 |
|------|------|
| **Trace** | 一次完整请求的追踪，由唯一 traceId 标识 |
| **Span** | Trace 中的一个工作单元，由 spanId 标识 |
| **Annotation** | 关键事件标记（cs/cr/ss/sr） |
| **B3 Propagation** | 通过 HTTP Header 传播追踪信息 |

B3 HTTP Headers：

| Header | 说明 |
|--------|------|
| `X-B3-TraceId` | 全局唯一追踪 ID |
| `X-B3-SpanId` | 当前 Span ID |
| `X-B3-ParentSpanId` | 父 Span ID |
| `X-B3-Sampled` | 是否采样 |

## 核心依赖

| 依赖 | 说明 |
|------|------|
| `spring-boot-starter-web` | Web 服务 |
| `spring-cloud-starter-sleuth` | 自动在日志中添加 traceId/spanId |
| `spring-cloud-starter-zipkin` | 将链路数据发送到 Zipkin Server |
| `spring-cloud-dependencies Hoxton.SR12` | Spring Cloud 版本管理 |

## Sleuth 自动追踪范围

Sleuth 自动追踪以下组件，无需额外代码：

- HTTP 请求（RestTemplate、WebClient、Feign）
- 消息队列（RabbitMQ、Kafka）
- 定时任务（@Scheduled）
- 异步方法（@Async）
- 数据库调用

## 快速开始

### 步骤一：部署 Zipkin

```bash
docker run -d --name zipkin \
  -p 9411:9411 \
  openzipkin/zipkin
```

### 步骤二：理解应用配置

`application.yml` 关键配置：

```yaml
spring:
  sleuth:
    sampler:
      probability: 1.0       # 采样率：1.0 = 100%（生产环境建议 0.1 即 10%）
    propagation:
      type: B3                # 传播方式：B3 格式
  zipkin:
    base-url: http://127.0.0.1:9411   # Zipkin Server 地址
    sender:
      type: web               # 发送方式：HTTP
```

日志格式自动包含：`[appName, traceId, spanId, exportable]`

```
2024-01-23 16:00:00.000 [http-nio-8080-exec-1] [abc123,def456] INFO  c.z.l.t.controller.TraceController - 检查库存
```

### 步骤三：启动应用

```bash
mvn spring-boot:run
```

### 步骤四：测试接口

```bash
# 1. 检查库存（自动追踪 + 手动 Span）
GET http://localhost:8080/api/trace/stock?productId=P001

# 2. 扣减库存
GET http://localhost:8080/api/trace/deduct?productId=P001&quantity=5

# 3. 跨服务调用（RestTemplate 自动传播 traceId）
GET http://localhost:8080/api/trace/cross-service

# 4. 查看当前链路信息
GET http://localhost:8080/api/trace/info
```

### 步骤五：在 Zipkin 中查看链路

1. 打开 Zipkin UI：`http://localhost:9411`
2. 点击 **Find Traces** 查看所有链路
3. 选择一个 Trace 查看调用链详情
4. 查看 **Dependencies** 页面了解服务依赖关系

### 步骤六：手动 Span（进阶用法）

本模块使用 Brave `Tracer` 创建手动 Span，用于追踪自定义业务逻辑：

```java
@Autowired
private Tracer tracer;

// 获取当前 traceId
String traceId = tracer.currentSpan().context().traceIdString();

// InventoryService 中使用手动 Span
brave.Span span = tracer.nextSpan().name("checkStock").start();
try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
    // 业务逻辑
} finally {
    span.finish();
}
```

## SkyWalking vs Sleuth+Zipkin 对比

| 对比项 | Sleuth + Zipkin | SkyWalking |
|--------|----------------|------------|
| 埋点方式 | Sleuth 自动 + Brave 手动 | Agent 自动 + Toolkit 手动 |
| 传播协议 | B3 HTTP Header | gRPC |
| 侵入性 | 需引入依赖 | Agent 完全无侵入 |
| 功能 | 链路追踪为主 | 链路 + 指标 + 日志 + 拓扑 |
| 生态 | Spring Cloud 原生 | Apache 顶级项目 |

## 业务场景说明

- **InventoryService**：模拟库存查询/扣减业务
- 使用 Brave `Tracer` 创建手动 Span，追踪关键业务步骤
- `RestTemplate` 跨服务调用时自动在 Header 中注入 B3 traceId
- `RestTemplateConfig` 配置了 `RestTemplate` Bean（Sleuth 自动增强）

## 项目结构

```
springboot-log-system-trace/
├── pom.xml
└── src/main/
    ├── java/com/zja/log/trace/
    │   ├── TraceApplication.java          # 启动类
    │   ├── config/
    │   │   └── RestTemplateConfig.java    # RestTemplate Bean 配置
    │   ├── controller/
    │   │   └── TraceController.java       # 测试接口（库存查询、扣减、跨服务调用）
    │   └── service/
    │       └── InventoryService.java      # 库存服务（手动 Span 追踪）
    └── resources/
        └── application.yml                # Sleuth + Zipkin 配置
```
