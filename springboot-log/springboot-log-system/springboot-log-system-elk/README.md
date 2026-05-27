# springboot-log-system-elk

**ELK（Elasticsearch + Logstash + Kibana）日志系统集成示例**

通过 Logback 的 `LogstashTcpSocketAppender` 将应用日志以 JSON 格式通过 TCP 发送到 Logstash，再由 Logstash 写入 Elasticsearch，最后通过 Kibana 进行日志查询和可视化分析。

## 架构流程

```
SpringBoot App → (TCP/JSON) → Logstash → Elasticsearch → Kibana
```

## 核心依赖

| 依赖 | 说明 |
|------|------|
| `spring-boot-starter-web` | Web 服务 |
| `spring-boot-starter-logging` | Logback 日志框架 |
| `logstash-logback-encoder 6.6` | Logstash Logback 编码器，将日志以 JSON 格式通过 TCP 发送到 Logstash |

## 快速开始

### 步骤一：部署 ELK 环境

使用 Docker 快速搭建 ELK 环境：

```bash
# 1. 启动 Elasticsearch
docker run -d --name elasticsearch \
  -p 9200:9200 \
  -e "discovery.type=single-node" \
  elasticsearch:7.17.0

# 2. 启动 Kibana
docker run -d --name kibana \
  -p 5601:5601 \
  --link elasticsearch \
  kibana:7.17.0

# 3. 启动 Logstash
docker run -d --name logstash \
  -p 5044:5044 \
  --link elasticsearch \
  logstash:7.17.0
```

### 步骤二：配置 Logstash

创建 `logstash.conf` 配置文件：

```conf
input {
  tcp {
    port => 5044
    codec => json_lines
  }
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    index => "springboot-log-%{+YYYY.MM.dd}"
  }
}
```

### 步骤三：理解日志配置

`logback-spring.xml` 关键配置说明：

- **CONSOLE**：控制台输出，包含 `traceId` 信息
- **FILE**：本地文件输出，支持按大小和时间滚动
- **LOGSTASH**：通过 `LogstashTcpSocketAppender` 以 JSON 格式发送到 Logstash（默认 `127.0.0.1:5044`）
- **ASYNC_LOGSTASH**：异步发送到 Logstash，避免阻塞业务线程

日志按 Profile 区分：
- `dev` 环境：仅控制台 + 文件
- `prod`/`default` 环境：控制台 + 文件 + Logstash

### 步骤四：启动应用

```bash
# 开发环境（不发送到 Logstash）
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 生产环境（发送到 Logstash）
mvn spring-boot:run
```

### 步骤五：测试接口

```bash
# 1. 创建订单（正常流程，包含 MDC traceId 链路追踪）
GET http://localhost:8080/api/elk/order?userId=U001&product=iPhone15

# 2. 测试不同级别日志输出（TRACE/DEBUG/INFO/WARN/ERROR）
GET http://localhost:8080/api/elk/levels

# 3. 模拟异常日志（堆栈信息将被 Logstash 收集）
GET http://localhost:8080/api/elk/error
```

### 步骤六：在 Kibana 中查询日志

1. 打开 Kibana：`http://localhost:5601`
2. 创建 Index Pattern：`springboot-log-*`
3. 在 Discover 页面查询日志
4. 可根据 `traceId` 字段过滤完整调用链

## 业务场景说明

- **OrderService**：模拟订单创建流程（参数校验 → 库存检查 → 订单入库）
- 通过 `MDC` 注入 `traceId`，实现日志链路追踪
- 在 Kibana 中可根据 `traceId` 查询一次请求的所有相关日志

## 项目结构

```
springboot-log-system-elk/
├── pom.xml
└── src/main/
    ├── java/com/zja/log/elk/
    │   ├── ElkApplication.java           # 启动类
    │   ├── controller/
    │   │   └── ElkController.java        # 测试接口（创建订单、日志级别、异常模拟）
    │   └── service/
    │       └── OrderService.java         # 订单服务（MDC traceId 链路追踪）
    └── resources/
        ├── application.yml               # 应用配置
        └── logback-spring.xml            # 日志配置（CONSOLE + FILE + LOGSTASH）
```
