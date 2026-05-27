# springboot-log-system-loki

**Loki 日志系统集成示例**

通过 Loki4j Logback Appender 将应用日志以 HTTP 方式直接推送到 Loki，在 Grafana 中使用 LogQL 查询日志。Loki 只索引标签（label），不索引日志内容，存储成本远低于 Elasticsearch。

## 架构流程

```
SpringBoot App → (HTTP Push) → Loki → Grafana (LogQL 查询)
```

## Loki vs Elasticsearch 对比

| 对比项 | ELK（Elasticsearch） | Loki |
|--------|---------------------|------|
| 索引方式 | 全文索引（所有字段） | 仅索引标签（label） |
| 存储成本 | 高 | 低 |
| 查询语言 | Lucene/KQL | LogQL（类似 PromQL） |
| 查询灵活性 | 高（全文搜索） | 适中（标签过滤 + 正则） |
| 适用场景 | 需要复杂搜索的场景 | 云原生、成本敏感场景 |

## 核心依赖

| 依赖 | 说明 |
|------|------|
| `spring-boot-starter-web` | Web 服务 |
| `spring-boot-starter-logging` | Logback 日志框架 |
| `loki-logback-appender 1.3.2` | Loki4j Appender，通过 HTTP 直接推送日志到 Loki |

## 快速开始

### 步骤一：部署 Loki + Grafana

使用 Docker 快速搭建：

```bash
# 1. 启动 Loki
docker run -d --name loki \
  -p 3100:3100 \
  grafana/loki:2.9.0

# 2. 启动 Grafana
docker run -d --name grafana \
  -p 3000:3000 \
  grafana/grafana:latest
```

### 步骤二：在 Grafana 中添加 Loki 数据源

1. 打开 Grafana：`http://localhost:3000`（默认账号 admin/admin）
2. 进入 Configuration → Data Sources → Add data source
3. 选择 Loki
4. URL 填写：`http://loki:3100`（Docker 网络）或 `http://localhost:3100`
5. 点击 Save & Test

### 步骤三：理解日志配置

`logback-spring.xml` 关键配置说明：

- **CONSOLE**：控制台输出，包含 `orderId` 信息
- **FILE**：本地文件输出
- **LOKI**：通过 `Loki4jAppender` 以 HTTP 方式推送日志到 Loki
  - 标签配置：`app=${APP_NAME},host=${HOSTNAME},level=%level`
  - Loki 根据标签建立索引，查询时通过标签过滤

日志按 Profile 区分：
- `dev` 环境：仅控制台
- `default`/`prod` 环境：控制台 + 文件 + Loki

### 步骤四：启动应用

```bash
mvn spring-boot:run
```

### 步骤五：测试接口

```bash
# 1. 发起支付
GET http://localhost:8080/api/loki/pay?orderId=ORD001&amount=99.9&payMethod=alipay

# 2. 发起退款
GET http://localhost:8080/api/loki/refund?paymentId=PAY001&amount=50.0

# 3. 查看 LogQL 查询示例
GET http://localhost:8080/api/loki/logql-examples
```

### 步骤六：在 Grafana 中使用 LogQL 查询

在 Grafana → Explore 页面，选择 Loki 数据源，输入 LogQL 查询：

```logql
# 查询所有日志
{app="springboot-log-system-loki"}

# 查询 ERROR 级别日志
{app="springboot-log-system-loki"} |= "ERROR"

# 查询包含"支付"关键字的日志
{app="springboot-log-system-loki"} |= "支付"

# 按日志级别过滤
{app="springboot-log-system-loki"} | logfmt | level="ERROR"

# 统计 5 分钟内的错误率
rate({app="springboot-log-system-loki"} |= "ERROR" [5m])
```

## 业务场景说明

- **PaymentService**：模拟支付/退款业务流程
- 通过 `MDC` 注入 `orderId`，在 Loki 中可按订单追踪日志
- 日志标签包含 `app`、`host`、`level`，便于 Grafana 中多维度查询

## 项目结构

```
springboot-log-system-loki/
├── pom.xml
└── src/main/
    ├── java/com/zja/log/loki/
    │   ├── LokiApplication.java          # 启动类
    │   ├── controller/
    │   │   └── LokiController.java       # 测试接口（支付、退款、LogQL 示例）
    │   └── service/
    │       └── PaymentService.java       # 支付服务（MDC orderId 链路追踪）
    └── resources/
        ├── application.yml               # 应用配置
        └── logback-spring.xml            # 日志配置（CONSOLE + FILE + LOKI）
```
