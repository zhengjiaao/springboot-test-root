# springboot-monitor-prometheus

**prometheus 监控**


### 依赖引入

```xml
        <!--监控-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <!--适配prometheus-->
        <dependency>
            <groupId>io.micrometer</groupId>
            <artifactId>micrometer-registry-prometheus</artifactId>
            <scope>runtime</scope>
        </dependency>
```

### 配置`application.yml`

```yaml
spring:
  application:
    name: springboot-prometheus-example

# 开启监控并可以让prometheus拉取配置
management:
  endpoint:
    health:
      show-details: always
    metrics:
      enabled: true
    prometheus:
      enabled: true
  endpoints:
    web:
      exposure:
        include: "*"
  metrics:
    tags:
      application: ${spring.application.name}
    export:
      prometheus:
        enabled: true

```

### prometheus配置采集

- 1、访问 http://127.0.0.1:8080/actuator 路径就能看到一大堆输出的指标了，包括prometheus的
  
- 2、通过prometheus来采集应用的指标，配置 prometheus.yml 文件
