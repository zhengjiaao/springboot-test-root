# springboot-log-system-plg

**PLG（Prometheus + Loki + Grafana）完整可观测性方案集成示例**

PLG 是云原生场景下的完整可观测性解决方案，覆盖可观测性三大支柱：**Metrics（指标）+ Logs（日志）+ Visualization（可视化）**。本模块同时集成 Prometheus 指标采集和 Loki 日志采集，通过 Grafana 统一展示。

## 架构流程

```
                    ┌→ Prometheus (指标采集) → Grafana Dashboard
SpringBoot App ─────┤
                    └→ Loki (日志采集)       → Grafana Explore
```

## 可观测性三大支柱

| 支柱 | 工具 | 说明 |
|------|------|------|
| Metrics（指标） | Prometheus | JVM 指标、HTTP 请求指标、自定义业务指标 |
| Logs（日志） | Loki | 应用日志通过 Loki4j 推送，LogQL 查询 |
| Visualization（可视化） | Grafana | 统一面板展示指标仪表盘和日志 |

## 核心依赖

| 依赖 | 说明 |
|------|------|
| `spring-boot-starter-web` | Web 服务 |
| `spring-boot-starter-actuator` | 暴露应用指标端点 |
| `micrometer-registry-prometheus` | Prometheus 指标采集 |
| `loki-logback-appender 1.3.2` | 日志推送到 Loki |

## 快速开始

### 步骤一：部署 PLG 环境

使用 Docker 快速搭建：

```bash
# 1. 启动 Prometheus
docker run -d --name prometheus \
  -p 9090:9090 \
  prom/prometheus

# 2. 启动 Loki
docker run -d --name loki \
  -p 3100:3100 \
  grafana/loki:2.9.0

# 3. 启动 Grafana
docker run -d --name grafana \
  -p 3000:3000 \
  grafana/grafana:latest
```

### 步骤二：配置 Prometheus 采集目标

在 `prometheus.yml` 中添加采集配置：

```yaml
scrape_configs:
  - job_name: 'springboot-plg'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['host.docker.internal:8080']
```

### 步骤三：配置 Grafana 数据源

1. 打开 Grafana：`http://localhost:3000`（默认 admin/admin）
2. 添加 Prometheus 数据源：`http://prometheus:9090`
3. 添加 Loki 数据源：`http://loki:3100`
4. 导入 JVM Dashboard（ID: 4701）

### 步骤四：理解应用配置

`application.yml` 关键配置：

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"         # 暴露所有 Actuator 端点
  metrics:
    tags:
      application: ${spring.application.name}  # 指标标签
    export:
      prometheus:
        enabled: true        # 启用 Prometheus 导出
```

### 步骤五：启动应用

```bash
mvn spring-boot:run
```

### 步骤六：测试接口

```bash
# 1. 查看商品详情（产生 Prometheus 指标 + Loki 日志）
GET http://localhost:8080/api/plg/product?productId=P001

# 2. 搜索商品（产生 Prometheus 指标 + Loki 日志）
GET http://localhost:8080/api/plg/search?keyword=手机

# 3. 查看可用的可观测性端点和查询示例
GET http://localhost:8080/api/plg/endpoints
```

### 步骤七：验证指标和日志

**验证 Prometheus 指标：**

```bash
# 访问 Prometheus 指标端点
curl http://localhost:8080/actuator/prometheus

# 可以看到自定义指标：
# product_view_total - 商品浏览总数
# product_search_total - 商品搜索总数
```

**验证 Loki 日志（Grafana → Explore）：**

```logql
# 查询所有日志
{app="springboot-log-system-plg"}

# 查询 ERROR 日志
{app="springboot-log-system-plg"} |= "ERROR"
```

**PromQL 查询示例（Grafana → Explore → Prometheus）：**

```promql
# 每秒请求数
rate(http_server_requests_seconds_count[5m])

# 商品浏览量
product_view_total

# 商品搜索量
product_search_total
```

## 业务场景说明

- **ProductService**：模拟商品浏览/搜索业务
- 每次请求同时产生 Prometheus 指标（Counter/Timer）和 Loki 日志
- 在 Grafana 中可同时查看指标趋势和对应时间段的日志

## 项目结构

```
springboot-log-system-plg/
├── pom.xml
└── src/main/
    ├── java/com/zja/log/plg/
    │   ├── PlgApplication.java            # 启动类
    │   ├── controller/
    │   │   └── PlgController.java         # 测试接口（商品浏览、搜索、端点查询）
    │   └── service/
    │       └── ProductService.java        # 商品服务（Counter/Timer 指标埋点）
    └── resources/
        ├── application.yml                # Actuator + Prometheus 配置
        └── logback-spring.xml             # 日志配置（CONSOLE + FILE + LOKI）
```
