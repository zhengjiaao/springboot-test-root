/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-13 16:28
 * @Since:
 */
package com.zja.controller;

import com.zja.model.entity.User;
import com.zja.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * TestRestTemplate 单元测试实例
 *
 * @author: zhengja
 * @since: 2023/10/13 16:28
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 启动一个嵌入式的随机端口的服务器，并加载应用程序上下文
public class UserControllerTest2 {

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * 要启动项目，可以通过 UnittestJunitApplication.java 类启动
     */
    @Test
    public void testGetUserById() {
        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/{id}", User.class, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        User user = response.getBody();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("John Doe");
    }

    //restTemplate 搭配 MockMvc、@MockBean、Mockito 等，来模拟依赖、验证方法调用和设置方法的行为。
    @MockBean
    private UserService userService;

    /**
     * 不需要启动项目
     */
    @Test
    public void testGetUserByIdV2() {
        // 模拟 userService.getUserById() 方法的行为
        User user = new User(1L, "John");
        Mockito.when(userService.getUserById(1L)).thenReturn(user);

        // 发送 GET 请求
        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/{id}", User.class, 1L);

        // 验证响应
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        User responseBody = response.getBody();
        assertThat(responseBody.getId()).isEqualTo(1L);
        assertThat(responseBody.getName()).isEqualTo("John");

        // 验证 userService.getUserById() 方法是否被调用
        Mockito.verify(userService, Mockito.times(1)).getUserById(1L);
        Mockito.verifyNoMoreInteractions(userService);
    }


    @Test
    public void testCreateUser() {
        // 创建要发送的请求体
        User user = new User(1L, "John");
        Mockito.when(userService.createUser(user)).thenReturn(user);

        // 发送 POST 请求
        ResponseEntity<User> response = restTemplate.postForEntity("/api/users", user, User.class);

        // 验证响应
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        User responseBody = response.getBody();
        assertThat(responseBody.getId()).isNotNull();
        assertThat(responseBody.getName()).isEqualTo("John");
    }

}
