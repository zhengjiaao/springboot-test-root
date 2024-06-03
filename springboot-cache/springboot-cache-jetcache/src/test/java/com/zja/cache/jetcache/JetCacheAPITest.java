package com.zja.cache.jetcache;

import com.zja.cache.jetcache.model.User;
import com.zja.cache.jetcache.service.JetCacheAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JetCacheAPITest {

    @Autowired
    private JetCacheAPI jetCacheAPI;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserId("111");
        user.setName("张三");

        User user2 = new User();
        user2.setUserId("222");
        user2.setName("李四");

        jetCacheAPI.putUser(user);
        jetCacheAPI.putUser(user2);
    }

    @Test
    void testPutUser() {
        User user = new User();
        user.setUserId("123");
        user.setName("婉儿");

        jetCacheAPI.putUser(user);
    }

    @Test
    void testPutUserIfAbsent() {
        String userId = "555";

        jetCacheAPI.putUserIfAbsent(userId);
    }

    @Test
    void testGetUser() {
        String userId = "111";
        User retrievedUser = jetCacheAPI.getUser(userId);
        System.out.println(retrievedUser);
    }

    @Test
    void testDeleteUser() {
        String userId = "101112";
        jetCacheAPI.deleteUser(userId);
    }

    @Test
    void testComputeIfAbsent() {
        String userId = "131415";
        jetCacheAPI.computeIfAbsent(userId);
    }

}
