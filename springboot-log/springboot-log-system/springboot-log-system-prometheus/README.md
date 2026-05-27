# springboot-log-system-prometheus

**Prometheus 指标监控集成示例**

通过 Spring Boot Actuator + Micrometer 将应用指标以 Prometheus 格式暴露，演示 Counter、Gauge、Timer、DistributionSummary 四种核心指标类型的使用方式，并提供 PromQL 查询示例。

## 架构流程

```
SpringBoot App (Actuator /prometheus端点) ← Prometheus (定时拉取) → Grafana (可视化)
```

## 四种指标类型

| 类型 | 名称 | 说明 | 示例 |
|------|------|------|------|
| Counter | 计数器 | 单调递增，适合统计总量 | API 请求总数、错误总数 |
| Gauge | 仪表盘 | 可增可减，适合当前值 | 在线用户数、队列大小 |
| Timer | 计时器 | 统计耗时分布 | API 响应时间 |
| DistributionSummary | 分布摘要 | 统计数值分布 | 请求体大小分布 |

## 核心依赖

| 依赖 | 说明 |
|------|------|
| `spring-boot-starter-web` | Web 服务 |
| `spring-boot-starter-actuator` | 暴露应用监控端点 |
| `micrometer-registry-prometheus` | 将指标以 Prometheus 格式暴露 |

## 快速开始

### 步骤一：部署 Prometheus + Grafana

```bash
# 1. 启动 Prometheus
docker run -d --name prometheus \
  -p 9090:9090 \
  -v prometheus.yml:/etc/prometheus/prometheus.yml \
  prom/prometheus

# 2. 启动 Grafana
docker run -d --name grafana \
  -p 3000:3000 \
  grafana/grafana:latest
```

### 步骤二：配置 Prometheus 采集目标

创建 `prometheus.yml`：

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'springboot-app'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['host.docker.internal:8080']
```

### 步骤三：理解应用配置

`application.yml` 关键配置：

```yaml
management:
  endpoint:
    prometheus:
      enabled: true              # 启用 Prometheus 端点
  endpoints:
    web:
      exposure:
        include: "*"             # 暴露所有端点
  metrics:
    tags:
      application: ${spring.application.name}  # 给所有指标添加应用名标签
```

### 步骤四：启动应用

```bash
mvn spring-boot:run
```

### 步骤五：验证指标端点

```bash
# 查看所有 Prometheus 指标
curl http://localhost:8080/actuator/prometheus

# 查看健康状态
curl http://localhost:8080/actuator/health

# 查看所有可用端点
curl http://localhost:8080/actuator
```

### 步骤六：测试接口（触发自定义指标）

```bash
# 1. 模拟 API 调用（Counter + Timer 指标）
GET http://localhost:8080/api/prometheus/call?api=/user/list

# 2. 模拟错误（Counter 指标）
GET http://localhost:8080/api/prometheus/error

# 3. 模拟用户上线（Gauge 指标增加）
GET http://localhost:8080/api/prometheus/online

# 4. 模拟用户下线（Gauge 指标减少）
GET http://localhost:8080/api/prometheus/offline

# 5. 查看 PromQL 查询示例
GET http://localhost:8080/api/prometheus/promql-examples
```

### 步骤七：在 Grafana 中配置

1. 打开 Grafana：`http://localhost:3000`（默认 admin/admin）
2. 添加 Prometheus 数据源：`http://prometheus:9090`
3. 导入 JVM Dashboard（推荐 ID: 4701 或 12271）
4. 创建自定义业务指标面板

### 步骤八：PromQL 查询示例

```promql
# 请求总数
api_requests_total

# 每秒请求数（QPS）
rate(api_requests_total[5m])

# 错误率
rate(api_errors_total[5m]) / rate(api_requests_total[5m])

# 在线用户数
online_users

# P99 响应时间
api_response_time_seconds{quantile="0.99"}

# JVM 堆内存使用
jvm_memory_used_bytes{area="heap"}

# JVM GC 频率
rate(jvm_gc_pause_seconds_count[5m])

# HTTP 请求 P95 响应时间
http_server_requests_seconds{quantile="0.95"}
```

## 自定义指标说明

| 指标名 | 类型 | 说明 |
|--------|------|------|
| `api_requests_total` | Counter | API 请求总数（按接口路径分类） |
| `api_errors_total` | Counter | API 错误总数 |
| `online_users` | Gauge | 当前在线用户数 |
| `api_response_time_seconds` | Timer | API 响应时间分布 |
| `api_request_size` | DistributionSummary | 请求大小分布 |

## 业务场景说明

- **MetricsService**：演示四种 Micrometer 指标类型的注册和使用
- 通过 `MeterRegistry` 注册自定义指标
- 指标自动暴露在 `/actuator/prometheus` 端点

## 项目结构

```
springboot-log-system-prometheus/
├── pom.xml
└── src/main/
    ├── java/com/zja/log/prometheus/
    │   ├── PrometheusApplication.java     # 启动类
    │   ├── controller/
    │   │   └── PrometheusController.java  # 测试接口（API调用、错误、上下线、PromQL示例）
    │   └── service/
    │       └── MetricsService.java        # 指标服务（Counter/Gauge/Timer/Summary）
    └── resources/
        └── application.yml                # Actuator + Prometheus 配置
```
