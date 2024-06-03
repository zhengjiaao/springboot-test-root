#             

- [jetcache github](https://github.com/alibaba/jetcache)
- [jetcache docs](https://github.com/alibaba/jetcache/blob/master/docs/EN/Readme.md)

  Alibaba JetCache 是一个高性能的Java缓存解决方案，支持本地缓存、远程缓存、双级缓存。

## JetCache 的完整功能

1. 通过统一的Cache API来操作缓存。
2. 使用带有 TTL（生存时间）和两级缓存支持的注释进行声明式方法缓存
3. Cache使用缓存管理器创建并配置实例
4. 自动收集Cache实例和方法级缓存的访问统计信息
5. 可以自定义key的生成和value的序列化策略
6. 支持的缓存键转换器：fastjson// fastjson2；支持的jackson值转换器：java//kryokryo5
7. 分布式缓存自动刷新和分布式锁。（2.2+）
8. 使用 Cache API 进行异步访问（2.2+，带有 redis lettuce 客户端）
9. 更新后使本地缓存（在所有 JVM 进程中）无效（2.7+）
10. Spring Boot 支持

## 引入依赖及配置

Redis 支持（使用以下任一方式）:

1. [使用 Jedis 客户端](https://github.com/alibaba/jetcache/blob/master/docs/EN/RedisWithJedis.md)
2. [使用 lettuce 客户端](https://github.com/alibaba/jetcache/blob/master/docs/EN/RedisWithLettuce.md)
3. [使用 redisson 客户端](https://github.com/alibaba/jetcache/blob/master/docs/EN/RedisWithRedisson.md)

Spring data redis（需要文档）

```xml

<dependencies>
    <!--redis-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <!--redis Jedis 支持内存/远程redis/双级缓存(内存+远程redis)-->
    <dependency>
        <groupId>com.alicp.jetcache</groupId>
        <artifactId>jetcache-starter-redis</artifactId>
        <version>${jetcache.latest.version}</version>
    </dependency>
</dependencies>
```

```xml

```

配置

```yaml
jetcache:
  statIntervalMinutes: 15
  areaInCacheName: false
  local:
    default:
      type: linkedhashmap     # other choose：caffeine
      keyConvertor: fastjson2 # other choose：fastjson/jackson
      limit: 100
  remote:
    default:
      type: redis
      keyConvertor: fastjson2 # other choose：fastjson/jackson
      broadcastChannel: projectA
      valueEncoder: java # other choose：kryo/kryo5
      valueDecoder: java # other choose：kryo/kryo5
      poolConfig:
        minIdle: 5
        maxIdle: 20
        maxTotal: 50
      host: ${redis.host}
      port: ${redis.port}
```

# 遇到的坑

### jetcache 与 spring-boot-starter-data-redis 集成问题

* spring-boot-starter-data-redis 2.3.2.RELEASE 版本 使用的是 lettuce
* jetcache-starter-redis 2.7 版本 使用的是 jedis

若使用 jedis 则应该引入依赖：

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
        <version>2.3.12.RELEASE</version>
        <exclusions>
            <exclusion>
                <groupId>io.lettuce</groupId>
                <artifactId>lettuce-core</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>com.alicp.jetcache</groupId>
        <artifactId>jetcache-starter-redis</artifactId>
        <version>2.7.5</version>
    </dependency>
</dependencies>
```

jedis 配置文件(不带本地配置)：

```yaml
jetcache:
  areaInCacheName: false
  remote:
    default:
      type: redis
      keyConvertor: fastjson2
      broadcastChannel: projectA
      poolConfig:
        minIdle: 5
        maxIdle: 20
        maxTotal: 50
      host: ${redis.host}
      port: ${redis.port}
      #password:***
      #sentinels: 127.0.0.1:26379 , 127.0.0.1:26380, 127.0.0.1:26381
      #masterName: mymaster
```

若使用 lettuce 则应该引入依赖：

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
        <version>2.3.12.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>com.alicp.jetcache</groupId>
        <artifactId>jetcache-starter-redis-lettuce</artifactId>
        <version>2.7.5</version>
    </dependency>
</dependencies>
```

lettuce 配置文件(不带本地配置)：

```yaml
jetcache:
  areaInCacheName: false
  remote:
    default:
      type: redis.lettuce
      keyConvertor: fastjson2
      broadcastChannel: projectA
      uri: redis://127.0.0.1:6379/  # uri格式：redis://密码@ip:端口/库?timeout=5s  # 注意，若密码是带非法字符的，需要URL编码
      #uri: redis-sentinel://127.0.0.1:26379,127.0.0.1:26380,127.0.0.1:26381/?sentinelMasterId=mymaster
      #readFrom: slavePreferred
```