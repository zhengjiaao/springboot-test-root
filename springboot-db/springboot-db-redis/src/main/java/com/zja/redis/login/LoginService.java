package com.zja.redis.login;

import com.zja.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @Author: zhengja
 * @Date: 2024-09-11 15:15
 */
@Service
public class LoginService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 假设这是从数据库或其他来源获取用户信息的方法
    private User getUserByUsername(String username) {
        // 实现获取用户信息的逻辑
        return new User("1", "zhengja", "123456", "zhengja@163.com");
    }

    private static final int LOCK_TIME_MINUTES = 30; // 锁定时间，单位为分钟
    private static final int MAX_FAIL_COUNT = 5; // 最大失败次数
    private static final long WINDOW_TIME_MS = 60 * 1000; // 窗口时间，单位为毫秒

    public boolean login(String username, String password) {
        String keyPrefix = "loginFail:" + username;
        String lockKey = "loginLock:" + username;

        // 获取当前时间戳
        long currentTime = System.currentTimeMillis();

        // 清理过期的登录记录
        deleteExpiredKeys(keyPrefix, currentTime);

        // 检查是否已被锁定
        if (redisTemplate.opsForValue().get(lockKey) != null) {
            throw new RuntimeException("账号已被锁定，请稍后再试！");
        }

        // 验证用户名密码
        User user = getUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            // 记录失败登录
            recordLoginFailure(lockKey, keyPrefix, currentTime);
            throw new RuntimeException("用户名或密码错误");
        } else {
            // 登录成功，清除失败记录
            deleteExpiredKeys(keyPrefix, currentTime);
            redisTemplate.delete(lockKey); // 确保没有遗留的锁定记录
            return true;
        }
    }

    private void deleteExpiredKeys(String keyPrefix, long currentTime) {
        Set<String> keys = redisTemplate.keys(keyPrefix + ":*");
        if (keys == null) {
            throw new NullPointerException("Redis keys cannot be null.");
        }

        List<String> toDelete = new ArrayList<>();
        for (String key : keys) {
            long timestamp = Long.parseLong(key.substring(key.lastIndexOf(":") + 1));
            if (currentTime - timestamp > WINDOW_TIME_MS) {
                toDelete.add(key);
            }
        }

        if (!toDelete.isEmpty()) {
            redisTemplate.delete(toDelete);
        }
    }

    private void recordLoginFailure(String lockKey, String keyPrefix, long currentTime) {
        String key = keyPrefix + ":" + currentTime;
        redisTemplate.opsForValue().set(key, "1", WINDOW_TIME_MS, TimeUnit.MILLISECONDS);

        // 检查是否达到最大失败次数
        int failCount = getFailCount(keyPrefix, currentTime);
        if (failCount >= MAX_FAIL_COUNT) {
            // 锁定账户30分钟
            redisTemplate.opsForValue().set(lockKey, "locked", LOCK_TIME_MINUTES, TimeUnit.MINUTES);
        }
    }

    private int getFailCount(String keyPrefix, long currentTime) {
        int failCount = 0;
        Set<String> keys = redisTemplate.keys(keyPrefix + ":*");
        if (keys == null) {
            throw new NullPointerException("Redis keys cannot be null.");
        }

        for (String key : keys) {
            long timestamp = Long.parseLong(key.substring(key.lastIndexOf(":") + 1));
            if (currentTime - timestamp <= WINDOW_TIME_MS) {
                String countStr = redisTemplate.opsForValue().get(key);
                if (countStr != null) {
                    failCount += Integer.parseInt(countStr);
                }
            }
        }
        return failCount;
    }
}
