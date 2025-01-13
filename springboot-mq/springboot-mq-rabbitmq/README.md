# springboot-mq-rabbitmq

**MQ消息中间件: rabbitmq**

- [rabbitmq 官网](https://www.rabbitmq.com/)
- [rabbitmq github](https://github.com/rabbitmq)
- [rabbitmq 参考](https://www.jianshu.com/p/a205606182e7)

## 依赖引入

```xml
        <!--支持 RabbitMQ server 3.x-->
        <dependency>
            <groupId>com.rabbitmq</groupId>
            <artifactId>amqp-client</artifactId>
            <version>5.16.0</version>
        </dependency>
```

## 适配的中间件版本

> 以下是你可能会用到的中间件

|                    | 官网文档 | github  | 使用版本下载  | 详细  |  推荐  |
| ----------------- | ---------- | ---------- | ---------- | ---------- | ---------- | 
| Rabbitmq         | [Rabbitmq 官方](https://www.rabbitmq.com/)       | [Rabbitmq github](https://github.com/rabbitmq)        | [rabbitmq-server-3.11.5](https://github.com/rabbitmq/rabbitmq-server/releases/tag/v3.11.5)  |   |  |
