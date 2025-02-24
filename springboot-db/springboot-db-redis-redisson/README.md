# springboot-db-redis-redisson

## 介绍

Redisson是一个功能强大、功能丰富的基于Redis实现的分布式锁、分布式Map、分布式缓存、分布式消息队列、分布式限流器、分布式信号量、分布式计数器、分布式布隆过滤器等分布式架构解决方案。

## 功能总结

|   功能   |   	核心类/注解	    |      应用场景       |
|:------:|:-------------:|:---------------:|
|  分布式锁  |    	RLock     |  	秒杀系统、防止重复提交   |
| 分布式Map |     	RMap     |  	共享配置、分布式会话存储  |
|  缓存管理  |  	@Cacheable  |   	数据库查询结果缓存    |
|  发布订阅  |    	RTopic    |  	实时通知、事件驱动架构   |
| 分布式限流  | 	RRateLimiter | 	API限流、防止DDoS攻击 |

## 快速开始

1. 引入依赖

```xml

<dependencies>
    <dependency>
        <groupId>org.redisson</groupId>
        <artifactId>redisson-spring-boot-starter</artifactId>
        <version>3.41.0</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
</dependencies>
```

2. 配置Redis连接信息

```yaml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 123456
    database: 0
```

3. 创建RedissonClient实例

```java

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient(RedisProperties redisProps) {
        Config config = new Config();
        // 单节点模式
        config.useSingleServer()
                .setAddress("redis://" + redisProps.getHost() + ":" + redisProps.getPort())
                .setPassword(redisProps.getPassword())
                .setDatabase(redisProps.getDatabase())
                .setConnectionPoolSize(64)
                .setConnectionMinimumIdleSize(10);
        return Redisson.create(config);
    }
}
```

4. 使用RedissonClient进行操作

```java

@Service
public class LockService {

    @Autowired
    private RedissonClient redisson;

    public void doTaskWithLock() {
        RLock lock = redisson.getLock("myLock");
        try {
            // 尝试加锁，最多等待10秒，锁有效期30秒
            boolean isLocked = lock.tryLock(10, 30, TimeUnit.SECONDS);
            if (isLocked) {
                // 执行业务逻辑
                System.out.println("Lock acquired, processing...");
                Thread.sleep(5000); // 模拟业务处理
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                System.out.println("Lock released");
            }
        }
    }
}
```
