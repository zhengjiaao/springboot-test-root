# springboot-test-root

> [springboot 2.x 官方文档](https://spring.io/projects/spring-boot)

**说明**

> springboot 集成项目中常用到的一些技术

- AI：chatgpt
- api：API管理(swagger2 or 3)、knife4j
- captcha：图形验证码
- db：关系型数据库(h2、mysql、oracle、postgresql等)、SQL版本管理(flywaydb、liquibase)、分库分表(shardingSphere)、分布式事务管理(jta)
- db：非关系型数据库(mongodb、redis、elasticsearch、neo4j图数据库)
- encrypt：加解密(rsa、sm等)
- file：文件操作(解压缩、csv、excel、yml等)、文件预览、文档存储
- java：java基础、设计模式、juc并发编程
- mapping：对象映射(dozer、mapstruct)
- monitor：系统监控(prometheus)
- mq：消息中间件(rabbitmq、rocketmq、kafka等)
- nlp：自然语言分析(hanlp)
- process：流程引擎(flowable、liteflow、tangram等)
- qrcode：二维码(qrgen、zxing等)
- remote：远程调用(httpclient、openfeign、restTemplate等)
- task：定时任务(quartz、springTask、xxl-job等)
- unittest：单元测试(junit)
- utils：自定义工具类(utils)
- web-19000：接口服务提供者(rest api 接口服务)

## spring-boot 集成(组件)示例

> 以下是已经完成的示例模块

- [springboot-api API管理](./springboot-api)
    - [springboot-api-swagger2](./springboot-api/springboot-api-swagger2)
    - [springboot-api-swagger2-knife4j](./springboot-api/springboot-api-swagger2-knife4j)
    - [springboot-api-swagger3](./springboot-api/springboot-api-swagger3)
    - [springboot-api-swagger3-knife4j](./springboot-api/springboot-api-swagger3-knife4j)
- [springboot-captcha 验证码](./springboot-captcha)
- [springboot-db 数据库](./springboot-db)
    - [springboot-db-elasticsearch](./springboot-db/springboot-db-elasticsearch)
    - [springboot-db-flywaydb](./springboot-db/springboot-db-flywaydb)
    - [springboot-db-h2](./springboot-db/springboot-db-h2)
    - [springboot-db-liquibase](./springboot-db/springboot-db-liquibase)
    - [springboot-db-mongodb](./springboot-db/springboot-db-mongodb)
    - [springboot-db-mongodb2](./springboot-db/springboot-db-mongodb2)
    - [springboot-db-mysql](./springboot-db/springboot-db-mysql)
    - [springboot-db-oracle](./springboot-db/springboot-db-oracle)
    - [springboot-db-oracle2](./springboot-db/springboot-db-oracle2)
    - [springboot-db-redis](./springboot-db/springboot-db-redis)
    - [springboot-db-redis2](./springboot-db/springboot-db-redis2)
- [springboot-encrypt 加解密](./springboot-encrypt)
    - [springboot-encrypt-base64](./springboot-encrypt/springboot-encrypt-base64)
    - [springboot-encrypt-rsa](./springboot-encrypt/springboot-encrypt-rsa)
    - [springboot-encrypt-sm](./springboot-encrypt/springboot-encrypt-sm)
    - [springboot-encrypt-util](./springboot-encrypt/springboot-encrypt-util)
- [springboot-file 文件相关操作](./springboot-file)
    - [springboot-file-compress-ant](./springboot-file/springboot-file-compress-ant)
    - [springboot-file-compress-jdk](./springboot-file/springboot-file-compress-jdk)
    - [springboot-file-compress-zip4j](./springboot-file/springboot-file-compress-zip4j)
    - [springboot-file-csv](./springboot-file/springboot-file-csv)
    - [springboot-file-excel](./springboot-file/springboot-file-excel)
    - [springboot-file-ftp](./springboot-file/springboot-file-ftp)
    - [springboot-file-minio](./springboot-file/springboot-file-minio)
    - [springboot-file-yml](./springboot-file/springboot-file-yml)
- [springboot-java JAVA相关操作](./springboot-java)
    - [springboot-java-juc](./springboot-java/springboot-java-juc)
- [springboot-mapping 对象属性映射](./springboot-mapping)
    - [springboot-mapping-dozer](./springboot-mapping/springboot-mapping-dozer)
    - [springboot-mapping-mapstruct](./springboot-mapping/springboot-mapping-mapstruct)
- [springboot-mq MQ消息中间件](./springboot-mq)
    - [springboot-mq-rabbitmq-spring](./springboot-mq/springboot-mq-rabbitmq-spring)
    - [springboot-mq-rabbitmq-starter](./springboot-mq/springboot-mq-rabbitmq-starter)
    - [springboot-mq-rocketmq](./springboot-mq/springboot-mq-rocketmq)
    - [springboot-mq-rocketmq-starter](./springboot-mq/springboot-mq-rocketmq-starter)
- [springboot-process 流程管理](./springboot-process)
    - [springboot-process-liteflow](./springboot-process/springboot-process-liteflow)
    - [springboot-process-tangram](./springboot-process/springboot-process-tangram)
- [springboot-qrcode 二维码](./springboot-qrcode)
    - [springboot-qrcode-qrgen](./springboot-qrcode/springboot-qrcode-qrgen)
    - [springboot-qrcode-zxing](./springboot-qrcode/springboot-qrcode-zxing)
- [springboot-remote 远程调用](./springboot-remote)
    - [springboot-remote-consumer-openfeign](./springboot-remote/springboot-remote-consumer-openfeign)
    - [springboot-remote-consumer-restTemplate](./springboot-remote/springboot-remote-consumer-restTemplate)
- [springboot-task 定时任务](./springboot-task)
    - [springboot-task-quartz](./springboot-task/springboot-task-quartz)
    - [springboot-task-springtask](./springboot-task/springboot-task-springtask)
    - [springboot-task-xxl-job](./springboot-task/springboot-task-xxl-job)
- [springboot-unittest 单元测试](./springboot-unittest)
    - [springboot-unittest-junit](./springboot-unittest/springboot-unittest-junit)
- [springboot-web-19000 REST接口提供者](./springboot-web-19000)

## 适配的中间件版本

> 以下是你可能会用到的中间件

|           | 官网文档                                                              | github                                                | 使用版本下载                                                                                                                          | 详细 | 推荐 |
|-----------|-------------------------------------------------------------------|-------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|----|----| 
| zookeeper | [zookeeper.apache.org](http://zookeeper.apache.org/releases.html) |                                                       | [zookeeper-3.6.3-bin.tar.gz](https://www.apache.org/dyn/closer.lua/zookeeper/zookeeper-3.6.3/apache-zookeeper-3.6.3-bin.tar.gz) |    |    |
| Git       | [git-scm.com](https://git-scm.com/)                               |                                                       | [git-latest](https://git-scm.com/downloads)                                                                                     |    |    |
| Minio     | [minio 官网](https://min.io/)                                       | [minio github](https://github.com/minio/minio)        | [minio-latest](https://min.io/docs/minio/windows/index.html)                                                                    |    |    |
| Rocketmq  | [rocketmq 官方](https://rocketmq.apache.org/zh/)                    | [rocketmq github](https://github.com/apache/rocketmq) | [rocketmq-4.9.4](https://github.com/apache/rocketmq/releases/tag/rocketmq-all-4.9.4)                                            |    |    |
| Rabbitmq  | [Rabbitmq 官方](https://www.rabbitmq.com/)                          | [Rabbitmq github](https://github.com/rabbitmq)        | [rabbitmq-server-3.11.5](https://github.com/rabbitmq/rabbitmq-server/releases/tag/v3.11.5)                                      |    |    |

## 本地运行环境搭建

> 以下是你必须要安装的基础软件,可以使项目正常打包及运行.

|       | 官网文档                                                                              | github | 使用版本下载                                                                       | 详细 | 是否必须安装 |
|-------|-----------------------------------------------------------------------------------|--------|------------------------------------------------------------------------------|----|--------| 
| java  | [www.oracle.com/java8](https://www.oracle.com/java/technologies/downloads/#java8) |        | [java8 downloads](https://www.oracle.com/java/technologies/downloads/#java8) |    | **必须** |
| maven | [maven.apache.org](https://maven.apache.org/)                                     |        | [maven3.6.2 downloads](https://maven.apache.org/download.cgi)                |    | **必须** |

## 后续计划

> 以下是后续计划预研的技术

|              | 说明                            | 是否完成 | 
|--------------|-------------------------------|------|
| cloud-stream | 预研消息中间件kafka、rabbit、rocketmq等 | 计划中  |

## 你还可以学习其他项目

> 以下是你可能需要学习的其他项目及技术

|                                          | 资源地址                                                                                         | 说明                                                                                           |  |
|------------------------------------------|----------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|--|
| github/zhengjiaao                        | [github.com/zhengjiaao](https://github.com/zhengjiaao)                                       | 主页面，展示一些比较重要技术预研项目                                                                           |  |
| zhengjiaao/springcloud-test-root         | [springcloud-test-root](https://github.com/zhengjiaao/springcloud-test-root)                 | springcloud 全家桶(组件) 技术预研框架,内容较多，较基础，偏向于技术的应用，适合初学者快速掌握某项技术，欢迎Star，推荐学习                       |  |
| zhengjiaao/spring-boot-starter-test-root | [spring-boot-starter-test-root](https://github.com/zhengjiaao/spring-boot-starter-test-root) | spring-boot-starter 2.x 全家桶(组件) 技术预研框架,内容较多，较基础，偏向于技术的应用，适合初学者快速掌握某项技术，欢迎Star，推荐学习           |  |
| zhengjiaao/springboot-test-root          | [springboot-test-root](https://github.com/zhengjiaao/springboot-test-root)                   | springboot 2.x 技术预研框架,内容较多，较基础，偏向于技术的应用，适合初学者快速掌握某项技术，欢迎Star，推荐学习                            |  |
| zhengjiaao/spring5x                      | [spring5x](https://github.com/zhengjiaao/spring5x)                                           | spring 5.x 技术预研框架                                                                            |  |
| zhengjiaao/springboot-test-redis         | [springboot-test-redis](https://github.com/zhengjiaao/springboot-test-redis)                 | springboot 2.x + redis 项目实战-实例,很早之前学习redis写的，可以学习redis工具类、数据缓存、消息发布和订阅等                      |  |
| zhengjiaao/springboot-test-mybatis-root  | [springboot-test-mybatis-root](https://github.com/zhengjiaao/springboot-test-mybatis-root)   | springboot 2.x 集成 mybatis、mybatis-plus、分页插件 pagehelper。 使用 mybatis 实现简单的CRUD操作，动态插入、批量插入等操作。 |  |

## springboot 版本对应选型

- 参考地址：
    - https://start.spring.io/actuator/info
    - https://spring.io/projects/spring-cloud#overview
    - https://docs.spring.io/spring-boot/docs/{springboot-verion}/reference/htmlsingle/

| Spring Boot   | Spring Framework | Spring Cloud | spring-cloud-alibaba | Java        | Maven          | Gradle                     | Tomcat                   |
|---------------|------------------|--------------|----------------------|-------------|----------------|----------------------------|--------------------------|
| 3.2.x         | 6.0.x            | 2022.0.x     | 2021.x               | Java 17     | 3.6.3 or later | 7.x (7.5 or later) and 8.x | Tomcat 10.x              |
| 2.7.x         | 5.3.x            | 2021.0.x     | 2021.x               | Java 8 or 9 | 3.5+           | 6.8+                       | Tomcat 9.x               |
| 2.5.x         | 5.3.x            | 2020.0.x     | 2020.x               | Java 8 or 9 | 3.5+           | 6.8+                       | Tomcat 8.x or Tomcat 9.x |
| 2.3.x.RELEASE | 5.2.x.RELEASE    | Hoxton       | 2.2.x                | Java 8 or 9 | 3.3+           | 4.4+                       | Tomcat 8.x or Tomcat 9.x |
| 1.5.x.RELEASE | 4.x.x.RELEASE    | Edgware      | 1.5.x                | Java 7 or 8 | 3.2+           | 2.9+                       | Tomcat 7.x or Tomcat 8.x |
