/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-13 14:52
 * @Since:
 */
package com.zja.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zja.model.entity.User;
import com.zja.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * // @WebMvcTest 单元测试实例
 *
 * @author: zhengja
 * @since: 2023/10/13 14:52
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetUserById() throws Exception {
        // 1.模拟userService的返回值
        User mockUser = new User(1L, "Mock User");
        // 2.使用 Mockito.when() 方法模拟了 userService.getUserById() 方法的行为，并返回了预期的 User 对象。
        Mockito.when(userService.getUserById(1L)).thenReturn(mockUser);

        // 3.发起GET请求，验证返回结果
        mockMvc.perform(get("/api/users/{id}", 1L))
//        mockMvc.perform(get("/api/users/v2/{id}", 1L))  // 与上面一样效果
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Mock User"));

        // 4.验证userService的方法是否被调用
        Mockito.verify(userService, Mockito.times(1)).getUserById(1L);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testCreateUser() throws Exception {
        // 创建一个新用户
        User newUser = new User(2L, "New User");

        //Mockito.when() 方法模拟了 userService.createUser() 方法的行为，并返回了预期的 User 对象。
        Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(newUser);

        //方式1：输出打印结果
        //使用 mockMvc.perform() 方法执行 POST 请求
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        System.out.println("Response JSON: " + responseJson);

        //方式二：直接进行验证返回结果
        // 发起POST请求，验证返回结果
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("New User"));

        // 验证userService的方法是否被调用
        Mockito.verify(userService, Mockito.times(2)).createUser(newUser);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testCreateUser2() throws Exception {
        User user = new User(1L, "John");
        String jsonRequest = "{\"id\": 1, \"name\": \"John\"}";

        Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John"));

        // 验证userService的方法是否被调用
        Mockito.verify(userService, Mockito.times(1)).createUser(user);
        Mockito.verifyNoMoreInteractions(userService);
    }

    @Test
    public void testSearchUsers() throws Exception {
        User user1 = new User(1L, "John");
        User user2 = new User(2L, "Jane");
        List<User> userList = Arrays.asList(user1, user2);

        Mockito.when(userService.searchUsers(Mockito.anyString())).thenReturn(userList);

        mockMvc.perform(get("/api/users/search")
                        .param("query", "John"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value("John"))
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].name").value("Jane"));

        Mockito.verify(userService, Mockito.times(1)).searchUsers("John");
        Mockito.verifyNoMoreInteractions(userService);
    }
}