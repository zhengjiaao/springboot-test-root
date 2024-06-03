package com.zja.cache.jetcache.service;

import com.zja.cache.jetcache.model.User;
import com.zja.cache.jetcache.model.UserRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-05-31 10:12
 */
@Service
public class UserServiceImpl implements UserService {

    // 模拟：从数据库加载用户
    private User loadUserFromDataBase(String userId) {
        return new User(userId, "李四" + userId, getRandomNum());
    }

    // 生成0～100的随机数
    private int getRandomNum() {
        return (int) (Math.random() * 100);
    }

    @Override
    public User getUserById(String userId) {
        System.out.println("查询用户：" + userId);
        return loadUserFromDataBase(userId);
    }

    @Override
    public void updateUser(User user) {
        System.out.println("更新用户：" + user);
    }

    @Override
    public void deleteUser(String userId) {
        System.out.println("删除用户：" + userId);
    }

    @Override
    public BigDecimal summaryOfToday(String categoryId) {
        System.out.println("查询今日销售额：" + categoryId);
        BigDecimal bigDecimal = randomBigDecimal();
        System.out.println(bigDecimal);
        return bigDecimal;
    }

    @Override
    public List<User> getAll() {
        System.out.println("查询所有用户");

        return Arrays.asList(loadUserFromDataBase("1"), loadUserFromDataBase("2"));

        // 返回null1情况
        // return Collections.emptyList();
    }

    @Override
    public Map<String, User> getMap() {
        return Collections.emptyMap();
    }

    @Override
    public User getUser(UserRequest request) {
        return null;
    }

    @Override
    public User getUserById(List<String> ids) {
        return null;
    }

    @Override
    public User getUserById(Map<String, Object> map) {
        return null;
    }

    // 生成随机数
    private BigDecimal randomBigDecimal() {
        return BigDecimal.valueOf(Math.random() * 100);
    }
}
