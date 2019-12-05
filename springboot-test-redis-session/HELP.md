# springboot redis session配置

[toc]

### 1、前提springboot配置redis：

```xml
        <!--redis-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        
```

yml配置

```yml
# 主配置文件主要用来存放公共设置，不受环境配置影响
server:
  port: 8080
  servlet:
    context-path: /springboot-test-redis
spring:
  redis:
    #host: 129.204.207.127
    database: 2 # Redis数据库索引（默认为0）,如果设置为1，那么存入的key-value都存放在select 1中
    host: 127.0.0.1
    port: 6379
    max-wait: 30000    # 连接池最大阻塞等待时间（使用负值表示没有限制）
    max-active: 100   # 连接池最大连接数（使用负值表示没有限制）
    max-idle: 20     # 连接池中的最大空闲连接
    min-idle: 0     # 连接池中的最小空闲连接
    timeout: 5000   # 连接超时
    #password: 123456 # 密码,默认密码为空
    #cluster:       # 集群配置
      #nodes: 127.0.0.1:6381,127.0.0.1:6382,127.0.0.1:6383,127.0.0.1:6384,127.0.0.1:6385,127.0.0.1:6386
      #max-redirects: 2  # 最大重定向次数
```

RedisConfig.java

```java
package com.dist.config;

import com.dist.constants.RedisConstants;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * redis配置类
 * @program: springbootdemo
 * @Date: 2019/1/25 15:20
 * @Author: Mr.Zheng
 * @Description:
 */
@Configuration
@EnableCaching //开启注解
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * retemplate相关配置
     * 使redis支持插入对象
     * @param factory
     * @return 方法缓存 Methods the cache
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);

        // 值采用json序列化
        template.setValueSerializer(jacksonSeial);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 对hash类型的数据操作: 针对map类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作： 简单K-V操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作: 针对list类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作： set类型数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作 ：zset类型数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    /**
     * 在redis.windows.conf  添加 notify-keyspace-events Egx
     * redis操作  --解决报异常 Error creating bean with name 'enableRedisKeyspaceNotificationsInitializer'
     * @return
     */
    /*@Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }*/


    /**************** 注解缓存失效时间配置 *******************/
    /**
     * springboot2.x 设置redis缓存失效时间(注解)：
     *      @CacheConfig(cacheNames = "SsoCache")
     *      @Cacheable(value = "BasicDataCache",keyGenerator = "wiselyKeyGenerator")
     * @param redisConnectionFactory
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                //半小时(秒)
                this.getRedisCacheConfigurationWithTtl(30*60), // 默认策略，未配置的 key 会使用这个
                this.getRedisCacheConfigurationMap() // 指定 key 策略
        );
    }

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        //SsoCache和BasicDataCache进行过期时间配置(秒)
        redisCacheConfigurationMap.put(RedisConstants.EntityCacheKey.OPINION, this.getRedisCacheConfigurationWithTtl(600));
        //redisCacheConfigurationMap.put("BasicDataCache", this.getRedisCacheConfigurationWithTtl(30*60));
        return redisCacheConfigurationMap;
    }

    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));

        return redisCacheConfiguration;
    }

/*    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append("." + method.getName());
                if (params == null || params.length == 0 || params[0] == null) {
                    return null;
                }
                String join = String.join("&", Arrays.stream(params).map(Object::toString).collect(Collectors.toList()));
                String format = String.format("%s{%s}", sb.toString(), join);
                //log.info("缓存key：" + format);
                return format;
            }
        };
    }*/

}

```

RedisUtil.java

```java
/**
 * redisTemplate封装
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key,long time){
        try {
            if(time>0){
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
        	/*============================String=============================*/
    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key){
        return key==null?null:redisTemplate.opsForValue().get(key);
    }
    
    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key,Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key,Object value,long time){
        try {
            if(time>0){
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            }else{
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
```



### 2、集成redis session

```xml
        <!--redisSession-->
        <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
        </dependency>

```


**两种方式：推荐两者只选其一(同时配置，只有方式2注解配置生效)**

- 方式1：yml配置
- 方式2：注解配置



##### 方式1：yml配置

>  session:
>  store-type: redis #是否将session存到redis  none/redis
>  timeout: 1000 # 配置session在redis中的过期时间

```yml
spring:
  redis:
    #host: 129.204.207.127
    database: 2 # Redis数据库索引（默认为0）,如果设置为1，那么存入的key-value都存放在select 1中
    host: 127.0.0.1
    port: 6379
    max-wait: 30000    # 连接池最大阻塞等待时间（使用负值表示没有限制）
    max-active: 100   # 连接池最大连接数（使用负值表示没有限制）
    max-idle: 20     # 连接池中的最大空闲连接
    min-idle: 0     # 连接池中的最小空闲连接
    timeout: 5000   # 连接超时
    #password: 123456 # 密码,默认密码为空
    #cluster:       # 集群配置
      #nodes: 127.0.0.1:6381,127.0.0.1:6382,127.0.0.1:6383,127.0.0.1:6384,127.0.0.1:6385,127.0.0.1:6386
      #max-redirects: 2  # 最大重定向次数
  # 方式1：配置redis存储session，方式2：@EnableRedisHttpSession注解，开启session存到redis中，并也可设置时间
  # 说明：如果方式1和方式2 同时配置，只生效方式2注解的配置，方式1的session过期时间不生效
  session:
    store-type: redis #是否将session存到redis  none/redis
    timeout: 1000 # 配置session在redis中的过期时间
```



##### 方式2：注解配置

@EnableRedisHttpSession

```java
/**
 * Date: 2019-12-05 14:53
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：配置redis存储session方式2 ，86400 session在redis中的过期时间
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 86400)
public class HttpSessionConfig {

}

```

> 注：如果`方式1yml配置`和`方式2注解配置` 若同时配置，只生效`方式2注解的配置`，方式1yml配置的session在redis中的过期时间不生效



