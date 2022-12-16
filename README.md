# springboot-test-root

> [springboot 2.x 官方文档](https://spring.io/projects/spring-boot)

**说明**
> springboot 集成项目中常用到的一些技术，如：操作库(oracle、pg、mongodb等)、API管理(swagger)、文件预览、文件存储、流程引擎、生成二维码、定时任务、REST远程调用、REST WEB接口管理、单元测试、消息中间件(rocketmq等)、Java基本使用等

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

|                    | 官网文档 | github  | 使用版本下载  | 详细  |  推荐  |
| ----------------- | ---------- | ---------- | ---------- | ---------- | ---------- | 
| zookeeper        | [zookeeper.apache.org](http://zookeeper.apache.org/releases.html)    |  | [zookeeper-3.6.3-bin.tar.gz](https://www.apache.org/dyn/closer.lua/zookeeper/zookeeper-3.6.3/apache-zookeeper-3.6.3-bin.tar.gz)  |   |  |
| Git              | [git-scm.com](https://git-scm.com/)       |           | [git-latest](https://git-scm.com/downloads)  |   |  |
| Minio            | [minio 官网](https://min.io/)       | [minio github](https://github.com/minio/minio)        | [minio-latest](https://min.io/docs/minio/windows/index.html)  |   |  |
| Rocketmq         | [rocketmq 官方](https://rocketmq.apache.org/zh/)       | [rocketmq github](https://github.com/apache/rocketmq)        | [rocketmq-4.9.4](https://github.com/apache/rocketmq/releases/tag/rocketmq-all-4.9.4)  |   |  |
| Rabbitmq         | [Rabbitmq 官方](https://www.rabbitmq.com/)       | [Rabbitmq github](https://github.com/rabbitmq)        | [rabbitmq-server-3.11.5](https://github.com/rabbitmq/rabbitmq-server/releases/tag/v3.11.5)  |   |  |
