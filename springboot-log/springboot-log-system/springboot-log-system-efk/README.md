# springboot-log-system-efk

**EFK（Elasticsearch + Fluentd + Kibana）日志系统集成示例**

与 ELK 的区别在于使用 Fluentd（轻量级，C+Ruby 实现）替代 Logstash（重量级，Java 实现）进行日志采集。应用将日志以 JSON 格式输出到文件或 stdout，由 Fluentd 采集后转发到 Elasticsearch，更适合 K8s 容器化环境。

## 架构流程

```
SpringBoot App → JSON 日志文件/stdout → Fluentd(tail采集) → Elasticsearch → Kibana
```

## EFK vs ELK 对比

| 对比项 | ELK（Logstash） | EFK（Fluentd） |
|--------|----------------|---------------|
| 实现语言 | Java | C + Ruby |
| 资源占用 | 较高（JVM） | 低 |
| 数据传输 | 应用主动推送（TCP） | Fluentd 拉取（tail 日志文件） |
| K8s 适配 | 一般 | 天然适配（DaemonSet） |
| 适用场景 | 传统服务器部署 | 容器化/K8s 环境 |

## 核心依赖

| 依赖 | 说明 |
|------|------|
| `spring-boot-starter-web` | Web 服务 |
| `spring-boot-starter-logging` | Logback 日志框架 |
| `logstash-logback-encoder 6.6` | 将日志输出为 JSON 格式，供 Fluentd 采集 |

## 快速开始

### 步骤一：部署 EFK 环境

使用 Docker 快速搭建 EFK 环境：

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

# 3. 启动 Fluentd
docker run -d --name fluentd \
  -p 24224:24224 \
  -v /var/log:/var/log \
  fluent/fluentd:v1.16
```

### 步骤二：配置 Fluentd

创建 `fluent.conf` 配置文件：

```xml
<source>
  @type tail
  path /var/log/app/springboot-log-system-efk-json.log
  pos_file /var/log/td-agent/springboot.log.pos
  tag springboot.app
  <parse>
    @type json
  </parse>
</source>

<match springboot.**>
  @type elasticsearch
  host elasticsearch
  port 9200
  index_name springboot-efk
  type_name _doc
</match>
```

### 步骤三：理解日志配置

`logback-spring.xml` 关键配置说明：

- **CONSOLE**：普通格式控制台输出（开发用）
- **CONSOLE_JSON**：JSON 格式控制台输出，容器化环境下 Fluentd 从 stdout 采集
- **FILE_JSON**：JSON 格式文件输出，Fluentd 通过 tail 方式采集此文件

日志按 Profile 区分：
- `dev` 环境：普通格式控制台
- `prod` 环境：JSON 控制台 + JSON 文件（适合 K8s）
- `default` 环境：普通控制台 + JSON 文件

### 步骤四：启动应用

```bash
# 开发环境
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 生产环境（输出 JSON 格式日志）
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 步骤五：测试接口

```bash
# 1. 用户注册
GET http://localhost:8080/api/efk/register?username=zhangsan&email=zhangsan@example.com

# 2. 用户登录
GET http://localhost:8080/api/efk/login?username=zhangsan&password=password123

# 3. 批量日志生成（Fluentd 将自动采集）
GET http://localhost:8080/api/efk/batch?count=10
```

### 步骤六：在 Kibana 中查询日志

1. 打开 Kibana：`http://localhost:5601`
2. 创建 Index Pattern：`springboot-efk*`
3. 在 Discover 页面查询日志
4. 可根据 `appName`、`level` 等字段进行过滤

## 业务场景说明

- **UserService**：模拟用户注册/登录流程
- 通过 `MDC` 注入 `requestId`，实现日志链路追踪
- JSON 格式输出便于 Fluentd 直接解析，无需额外正则匹配

## K8s 部署说明

在 K8s 环境中，Fluentd 通常以 DaemonSet 方式部署，自动采集每个 Pod 的 stdout 日志：

```yaml
# Fluentd DaemonSet 核心配置
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: fluentd
spec:
  template:
    spec:
      containers:
      - name: fluentd
        image: fluent/fluentd-kubernetes-daemonset:v1.16
        volumeMounts:
        - name: varlog
          mountPath: /var/log
      volumes:
      - name: varlog
        hostPath:
          path: /var/log
```

## 项目结构

```
springboot-log-system-efk/
├── pom.xml
└── src/main/
    ├── java/com/zja/log/efk/
    │   ├── EfkApplication.java           # 启动类
    │   ├── controller/
    │   │   └── EfkController.java        # 测试接口（注册、登录、批量日志）
    │   └── service/
    │       └── UserService.java          # 用户服务（MDC requestId 链路追踪）
    └── resources/
        ├── application.yml               # 应用配置
        └── logback-spring.xml            # 日志配置（CONSOLE + CONSOLE_JSON + FILE_JSON）
```
