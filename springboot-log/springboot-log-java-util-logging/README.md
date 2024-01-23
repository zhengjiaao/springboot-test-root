# springboot-log-java-util-logging

**说明**

springboot 2.x 使用 Logback 进行日志记录的入门级，默认日志记录启动器。

## 日志关键配置

application.properties 配置文件：

```properties
# 设置日志级别
logging.level.root=info
# 控制台日志输出格式
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
# 文件日志输出配置
logging.file.name=logs/application.log
logging.file.path=
# 启用彩色控制台日志输出
spring.output.ansi.enabled=always
```

application.yml 配置文件：

```yaml
logging:
  # 设置日志级别
  level:
    root: info
  # 控制台日志输出格式
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  # 文件日志输出配置
  file:
    name: logs/application.log
    path:

# 启用彩色控制台日志输出
spring:
  output:
    ansi:
      enabled: always
```

上述配置中的关键属性包括：

1. logging.level.root：设置根日志记录器的级别，默认为info，可以根据需要修改为其他级别，如debug、trace等。
2. logging.pattern.console：设置控制台日志输出的格式。可以根据需求自定义格式，包括日期、线程、日志级别、记录器名和消息等。
3. logging.file.name：设置日志文件的名称，默认为application.log。你可以根据需要修改文件名。
4. logging.file.path：设置日志文件的路径，默认为空。如果指定了路径，则日志文件将存储在指定路径下，否则将存储在应用程序的工作目录中。
5. spring.output.ansi.enabled：配置是否启用控制台日志输出的ANSI颜色，默认为always。你可以将其设置为never来禁用颜色输出，或将其设置为detect以根据终端支持情况自动启用或禁用颜色。

## 日志自定义配置

### 特定的包或类设置不同的日志级别

properties 配置：

```properties
# 项目整体日志
logging.level.root=INFO
# 特定的包或类设置不同的日志级别
logging.level.com.example=DEBUG
```

yaml 配置：

```yaml
logging:
  # 设置日志级别
  level:
    root: info # 当前项目
    com.example: debug # 特定的包或类设置不同的日志级别
```

### 设置日志格式

properties配置：

```properties
# 控制台日志输出格式
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
# 文件日志输出配置
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
logging.file.name=logs/application.log
logging.file.path=
```

yaml配置：

```yaml
logging:
  # 日志输出格式
  pattern:
    # 控制台日志输出格式
    console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    # 文件日志输出配置
    file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/application.log
    path:
```

上述配置中的关键属性包括：

1. logging.pattern.console和logging.pattern.file：分别设置控制台日志输出和文件日志输出的格式。你可以根据需要自定义格式。
2. logging.file.name：设置日志文件的名称，默认为application.log。你可以根据需要修改文件名。
3. logging.file.path：设置日志文件的路径，默认为空。如果指定了路径，则日志文件将存储在指定路径下，否则将存储在应用程序的工作目录中。

### 设置日志保留时间和大小

properties配置：

```properties
# 设置日志文件单个大小限制为10MB
logging.file.max-size=10MB
# 设置日志文件总大小限制为10MB
logging.file.total-size-cap=10MB
# 设置日志保留时间为7天
logging.file.max-history=7
```

yaml配置：

```yaml

logging:
  file:
    max-size: 10MB        # 设置日志文件大小限制为10MB
    total-size-cap: 10MB  # 设置日志文件总大小限制为10MB
    max-history: 7        # 设置日志保留时间为7天
```

上述配置中的关键属性包括：

1. logging.file.max-size：设置日志文件的大小限制。你可以指定一个值，如10MB，表示日志文件的大小不能超过10MB。
2. logging.file.total-size-cap：设置日志文件的总大小限制，默认为无限制。你可以指定一个值，如10MB，表示日志文件的总大小不能超过10MB。
3. logging.file.max-history：设置日志文件的保留时间，默认为无限制。你可以指定一个值，如7，表示日志文件将保留最近的7个文件，旧的日志文件将被删除。

特殊说明：max-size、total-size-cap：

1.

logging.file.max-size：该属性用于设置单个日志文件的最大大小。当日志文件达到或超过指定的大小时，Logback会自动滚动（即创建一个新的日志文件），以便继续写入日志。你可以使用可识别的文件大小单位（如10MB、100KB）来指定大小。

2.

logging.file.total-size-cap：该属性用于设置日志文件的总大小限制。当所有日志文件的总大小达到或超过指定的大小时，Logback会删除最旧的日志文件，以便为新的日志文件腾出空间。同样，你可以使用可识别的文件大小单位来指定总大小。

### 设置按天保留日志文件

按天保留日志文件

```yaml
logging:
  level:
    root: info
  # 日志文件
  file:
    name: logs/application.log
    pattern:
      console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
      file: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"

    # 按天滚动日志文件
    rolling:
      pattern: logs/application-%d{yyyy-MM-dd}.log
      max-history: 7
```

在上述示例中，关键部分如下：

1. logging.file.name：指定日志文件的名称和路径，默认为 logs/application.log。你可以根据需要自定义文件名和路径。
2. logging.file.pattern.console 和 logging.file.pattern.file：分别设置控制台日志输出和文件日志输出的格式。
3. logging.file.rolling.pattern：指定滚动后的日志文件名格式，使用 %d{yyyy-MM-dd} 表示按照年月日的格式在文件名中添加日期。
4. logging.file.rolling.max-history：指定保留的日志文件数量，即保留的天数。在上述示例中，设置为 7 表示保留最近 7 天的日志文件。

通过以上配置，日志文件将按照每天生成一个新的日志文件，并保留最近 7 天的日志文件。旧的日志文件会被自动删除。
