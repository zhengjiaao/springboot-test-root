/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-16 13:18
 * @Since:
 */
package com.zja.service;

import com.zja.dao.UserRepository;
import com.zja.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author: zhengja
 * @since: 2023/10/16 13:18
 */
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this); //初始化 Mockito 注解，确保 @Mock 和 @InjectMocks 注解生效
    }

    @Test
    public void testGetUserById() {
        // 模拟 UserRepository 的行为
        User user = new User(1L,  "John Doe");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // 调用 UserService 的方法
        User result = userService.getUserById(1L);

        // 验证结果是否符合预期
        assertEquals(user, result);

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testCreateUser() {
        // 模拟 UserRepository 的行为
        User user = new User(1L, "John Doe");
        when(userRepository.save(user)).thenReturn(user);

        // 调用 UserService 的方法
        User result = userService.createUser(user);

        // 验证结果是否符合预期
        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        // 调用 UserService 的方法
        userService.deleteUser(1L);

        // 验证 UserRepository 的方法是否被调用
        verify(userRepository, times(1)).deleteById(1L);
    }
}
