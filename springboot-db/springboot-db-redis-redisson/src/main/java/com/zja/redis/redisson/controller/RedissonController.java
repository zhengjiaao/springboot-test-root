package com.zja.redis.redisson.controller;

import com.zja.redis.redisson.model.User;
import com.zja.redis.redisson.service.*;
import com.zja.redis.redisson.service.LockService;
import com.zja.redis.redisson.service.pub.MessagePublisher;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Redisson 接口层（一般与页面、功能对应）
 *
 * @author: zhengja
 * @since: 2025/02/24 10:45
 */
@Validated
@RestController
@RequestMapping("/rest/redisson")
@Api(tags = {"Redisson管理页面"})
public class RedissonController {

    @Autowired
    private LockService lockService;

    @Autowired
    private MapService mapService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MessagePublisher messagePublisher;

    @Autowired
    private RateLimitService rateLimitService;

    @Autowired
    private LockOrderService lockOrderService;

    @Autowired
    private RedisUserService redisUserService;

    @Autowired
    private MyService myService;

    @GetMapping("/testAll")
    @ApiOperation("Redisson-测试")
    public void queryById() {
        // 测试分布式锁
        lockService.doTaskWithLock();

        // 测试分布式Map
        mapService.operateDistributedMap();

        // 测试缓存
        userService.getUserById("1"); // 第一次查询数据库
        userService.getUserById("1"); // 从缓存获取

        // 测试消息发布
        messagePublisher.publishMessage("myChannel", "Hello Redisson!");

        // 测试限流器
        for (int i = 0; i < 10; i++) {
            System.out.println("Access allowed: " + rateLimitService.tryAccess("api1"));
        }
    }

    @GetMapping("/lock")
    @ApiOperation("测试分布式锁")
    public void lock() {
        // 测试分布式锁
        lockService.doTaskWithLock();
    }

    @GetMapping("/lockOrder")
    @ApiOperation("测试分布式锁")
    public void lockOrder() {
        // 创建订单
        String orderId = "order-123";
        lockOrderService.createOrder(orderId);
    }

    @GetMapping("/map")
    @ApiOperation("测试分布式Map")
    public void map() {
        // 测试分布式Map
        mapService.operateDistributedMap();
    }

    @GetMapping("/user")
    @ApiOperation("测试缓存")
    public void user() {
        // 测试缓存、
        String id = "111";
        userService.getUserById(id); // 第一次查询数据库
        userService.getUserById(id); // 从缓存获取
    }

    @GetMapping("/user/add")
    @ApiOperation("测试缓存添加")
    public void userAdd() {
        // 测试缓存
        String id = "111";
        String username = "zhengja";
        userService.saveUser(new User(id, username));
    }

    @GetMapping("/user/del")
    @ApiOperation("测试缓存删除")
    public void userDel() {
        // 测试缓存删除
        String id = "111";
        userService.deleteUser(id);
    }

    @GetMapping("/redis")
    @ApiOperation("测试redis")
    public void redis() {
        // 测试redis
        String key = "testKey";
        String value = "testValue";
        redisService.setValue(key, value);
        String result = redisService.getValue(key);
        System.out.println("Value from Redis: " + result);
    }

    @GetMapping("/pub")
    @ApiOperation("测试消息发布")
    public void pub() {
        // 测试消息发布
        messagePublisher.publishMessage("myChannel", "Hello Redisson!");
    }

    @GetMapping("/rateLimit")
    @ApiOperation("测试限流器")
    public void rateLimit() {
        for (int i = 0; i < 10; i++) {
            System.out.println("Access allowed: " + rateLimitService.tryAccess("api1"));
        }
    }

    @GetMapping("/redisUser")
    @ApiOperation("测试redisUser")
    public void redisUser() {
        // 创建订单
        String username = "zhengja";
        String password = "123456";
        redisUserService.registerUser(username, password);
        User user = redisUserService.loadUserByUsername(username);
        System.out.println(user);
    }

    @GetMapping("/performTaskWithLock")
    @ApiOperation("测试performTaskWithLock")
    public void performTaskWithLock() {
        String taskId = "testId-123";
        myService.performTaskWithLock(taskId);
    }


}