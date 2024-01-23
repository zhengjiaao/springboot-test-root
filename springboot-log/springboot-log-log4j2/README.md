# springboot-log-log4j2

**说明**

## 引入log4j2依赖

> 注意，springboot默认使用Logback，需要先排除掉Logback，才会生效Log4j2依赖。

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <!--排除 Logback 依赖-->
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-logging</artifactId>
            </exclusion>
        </exclusions>
    </dependency>

    <!--使用 Log4j2 依赖-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-log4j2</artifactId>
    </dependency>
</dependencies>
```

## 配置 log4j2

```yaml
logging:
  level:
    root: info
    com.zja.log.user: debug
  file:
    name: logs/application.log
  config: classpath:log4j2.xml # 可选地，自定义日志配置
```

在上述示例中：

1. logging.level 配置了不同包或类的日志级别。root 表示根日志级别，com.example.mypackage 表示特定包的日志级别。你可以根据需要设置不同的日志级别。
2. logging.file.name 指定了日志文件的名称和路径。在示例中，日志文件被命名为 application.log 并位于 logs
   目录下。你可以根据需要自定义文件名和路径。
3. logging.config 指定了 Log4j2 配置文件的位置。在示例中，Log4j2 配置文件 log4j2.xml
   位于类路径下。你可以使用其他命名的配置文件，或者使用绝对/相对路径指定配置文件的位置。

## 创建 Log4j2 配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="info">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
        </Console>
        <RollingFile name="File" fileName="logs/application.log"
                     filePattern="logs/application-%d{yyyy-MM-dd}.log">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
            <Policies>
                <TimeBasedTriggeringPolicy/>
            </Policies>
        </RollingFile>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
            <AppenderRef ref="File"/>
        </Root>
    </Loggers>
</Configuration>
```
