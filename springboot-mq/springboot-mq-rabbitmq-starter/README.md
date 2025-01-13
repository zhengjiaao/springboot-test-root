# springboot-mq-rabbitmq-starter

**MQ消息中间件: rabbitmq**

- [rabbitmq 官网](https://www.rabbitmq.com/)
- [rabbitmq github](https://github.com/rabbitmq)
- [rabbitmq 参考](https://www.jianshu.com/p/a205606182e7)

## 依赖引入

此 amqp-client 5.x.x 客户端版本独立于 RabbitMQ 服务器版本，可与 RabbitMQ 服务器一起使用3.x。它们需要 Java 8 或更高版本。

```xml
        <!--MQ rabbitmq-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
            <!--内置 amqp-client 5.7.3-->
        </dependency>
```

## 适配的中间件版本

> 以下是你可能会用到的中间件

|                    | 官网文档 | github  | 使用版本下载  | 详细  |  推荐  |
| ----------------- | ---------- | ---------- | ---------- | ---------- | ---------- | 
| Rabbitmq         | [Rabbitmq 官方](https://www.rabbitmq.com/)       | [Rabbitmq github](https://github.com/rabbitmq)        | [rabbitmq-server-3.11.5](https://github.com/rabbitmq/rabbitmq-server/releases/tag/v3.11.5)  |   |  |
