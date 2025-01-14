# springboot-mq-rocketmq-starter

**MQ消息中间件：rocketmq**

- [rocketmq官方文档](https://rocketmq.apache.org/zh/)
- [rocketmq](https://github.com/apache/rocketmq)
- [rocketmq-dashboard](https://github.com/apache/rocketmq-dashboard)
- [rocketmq-spring](https://github.com/apache/rocketmq-spring)
- [rocketmq 消息发送几种机制的理解](https://www.51cto.com/article/801310.html)

## 依赖引入

- SDK参考：[rocketmq-grpc-client-java-sdk](https://rocketmq.apache.org/zh/download#rocketmq-grpc-client-java-sdk)
- 示例参考：[rocketmq-v5-client-spring-boot-samples](https://github.com/apache/rocketmq-spring/tree/master/rocketmq-v5-client-spring-boot-samples)

```xml

<dependency>
    <!--2.3.1 内置 rocketmq-client-5.3.0-->
    <groupId>org.apache.rocketmq</groupId>
    <artifactId>rocketmq-spring-boot-starter</artifactId>
    <version>2.3.1</version>
</dependency>
```

## 安装 RocketMQ

### Docker RocketMQ 安装

```shell
# 拉取镜像
docker pull apache/rocketmq:5.3.1
# 创建容器共享网络
docker network create rocketmq

# 启动 NameServer
docker run -d --name rmqnamesrv -p 9876:9876 --network rocketmq apache/rocketmq:5.3.1 sh mqnamesrv
# 验证 NameServer 是否启动成功
docker logs -f rmqnamesrv
```

### Docker RocketMQ Broker 安装

```shell
# 创建Broker目录
mkdir -p ~/dockerdata/rocketmq/broker
cd ~/dockerdata/rocketmq/broker

# 配置 Broker 的IP地址
# echo "brokerIP1=127.0.0.1" > broker.conf
echo "brokerIP1=192.168.159.148" > broker.conf

# 启动 Broker 和 Proxy
docker run -d \
--name rmqbroker \
--network rocketmq \
-p 10912:10912 -p 10911:10911 -p 10909:10909 \
-p 8080:8080 -p 8081:8081 \
-e "NAMESRV_ADDR=rmqnamesrv:9876" \
-v ./broker.conf:/home/rocketmq/rocketmq-5.3.1/conf/broker.conf \
apache/rocketmq:5.3.1 sh mqbroker --enable-proxy \
-c /home/rocketmq/rocketmq-5.3.1/conf/broker.conf

# 验证 Broker 是否启动成功
docker exec -it rmqbroker bash -c "tail -n 10 /home/rocketmq/logs/rocketmqlogs/proxy.log"

# 删除容器
docker rm -f rmqbroker
```

### Docker RocketMQ Dashboard 安装

```shell    
# 拉取镜像
$ docker pull apacherocketmq/rocketmq-dashboard:latest

# 正确应使用具体ip: 127.0.0.1:9876 or IP:9876
$ docker run -d --name rocketmq-dashboard -e "JAVA_OPTS=-Drocketmq.namesrv.addr=192.168.159.148:9876" -p 18080:8080 -t apacherocketmq/rocketmq-dashboard:latest

# 删除容器
docker rm -f rocketmq-dashboard
```

## 配置 RocketMQ

```yaml
# rocketmq-spring-boot-starter 2.2.0及其以上版本：
rocketmq:
  #  name-server: localhost:9876  # NameServer 地址，集群使用';'隔开
  name-server: 192.168.159.148:9876  # NameServer 地址，集群使用';'隔开
  producer:
    group: springboot-producer-group  # 生产者组名称
    send-message-timeout: 3000
    retry-times-when-send-failed: 2   # 发送失败重试次数
    retry-next-server: true   # 发送失败是否重试到下一个服务器
  #    access-key: RocketMQ    # 若启用了 ACL 功能
  #    secret-key: 12345678    # 若启用了 ACL 功能
  consumer:
    group: springboot-consumer-group  # 消费者组名称
    topic: test-topic         # 订阅的主题
#    access-key: RocketMQ    # 若启用了 ACL 功能
#    secret-key: 12345678    # 若启用了 ACL 功能
```

## SDK RocketMQ 应用

RocketMQ 配置，主要是消费者组配置，消费者监听器配置。

```java

@Configurable
@Import(RocketMQAutoConfiguration.class) // 让 RocketMQ 消费者监听器生效,无法收到消息。
public class RocketMQConfig {
}
```

消息发送者

```java

@Service
public class MessageProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送单向消息：Producer 仅负责发送消息，不等待、不处理 MQ 的 ACK。该发送方式时 MQ 也不返回 ACK。该方式的消息发送效率最高，但消息可靠性较差。
     */
    public void sendOneWayMessage(String topic, String message) {
        rocketMQTemplate.sendOneWay(topic, message);
        System.out.println("One way message sent: " + message);
    }

}
```

消息消费者

```java
/**
 * 消费者：使用 @RocketMQMessageListener 注解来订阅主题并监听消息的到达，处理消息的消费逻辑。
 */
@Service
@RocketMQMessageListener(consumerGroup = "springboot-consumer-group", topic = "test-topic")
public class MessageConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("MessageConsumer receive message：" + message);
    }
}
```

注意，会自动注册消费者组，所以不需要手动配置消费者组。

## 启用 ACL 功能权限控制

- 参考：[rocketmq-ACL 权限控制](https://rocketmq.apache.org/zh/docs/bestPractice/03access/)

## 适配的中间件版本

> 以下是你可能会用到的中间件

|          | 官网文档                                           | github                                                | 使用版本下载                                                    | 详细 | 推荐 |
|----------|------------------------------------------------|-------------------------------------------------------|-----------------------------------------------------------|----|----| 
| Rocketmq | [Rocketmq 官方](https://rocketmq.apache.org/zh/) | [rocketmq github](https://github.com/apache/rocketmq) | [rocketmq-5.3.1](https://rocketmq.apache.org/zh/download) |    |    |
