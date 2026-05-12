package com.zja.mvc.rate.controller;

import com.alibaba.fastjson.JSON;
import com.zja.mvc.rate.model.*;
import com.zja.mvc.rate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController 单元测试
 * <p>
 * 使用 MockMvc standalone 模式，不启动 Spring 容器，Mock UserService 依赖。
 * 限流相关逻辑（AOP 切面）在 Controller 单元测试中不生效，
 * 限流的集成测试应在 Spring 上下文环境下进行。
 *
 * @author: zhengja
 * @since: 2024/03/11
 */
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    // =====================================================================
    // queryById 测试
    // =====================================================================

    @Test
    void testQueryById_Success() throws Exception {
        // 准备测试数据
        UserDTO userDTO = UserDTO.builder().id("1").name("李四1").age(21).build();
        when(userService.findById("1")).thenReturn(userDTO);

        // 执行测试 & 验证结果
        mockMvc.perform(get("/rest/user/query/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("李四1"))
                .andExpect(jsonPath("$.age").value(21));

        verify(userService, times(1)).findById("1");
    }

    @Test
    void testQueryById_NotFound() throws Exception {
        // 准备测试数据
        when(userService.findById("999")).thenReturn(null);

        // 执行测试 & 验证结果
        mockMvc.perform(get("/rest/user/query/{id}", "999"))
                .andExpect(status().isOk());

        verify(userService, times(1)).findById("999");
    }

    @Test
    void testQueryById_BlankId() throws Exception {
        // 空白 id 应触发 @NotBlank 校验失败
        mockMvc.perform(get("/rest/user/query/{id}", ""))
                .andExpect(status().isBadRequest());

        verify(userService, never()).findById(anyString());
    }

    // =====================================================================
    // pageList 测试
    // =====================================================================

    @Test
    void testPageList_Success() throws Exception {
        // 准备测试数据
        List<UserDTO> dtoList = new ArrayList<>();
        dtoList.add(UserDTO.builder().id("1").name("李四1").age(21).build());
        dtoList.add(UserDTO.builder().id("2").name("李四2").age(22).build());

        PageData<UserDTO> pageData = PageData.of(dtoList, 0, 10, 2);
        when(userService.pageList(any(UserPageSearchRequest.class))).thenReturn(pageData);

        // 执行测试 & 验证结果
        mockMvc.perform(get("/rest/user/page/list")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.index").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true));

        verify(userService, times(1)).pageList(any(UserPageSearchRequest.class));
    }

    @Test
    void testPageList_WithSearchParam() throws Exception {
        // 准备测试数据
        List<UserDTO> dtoList = new ArrayList<>();
        dtoList.add(UserDTO.builder().id("1").name("李四1").age(21).build());

        PageData<UserDTO> pageData = PageData.of(dtoList, 0, 10, 1);
        when(userService.pageList(any(UserPageSearchRequest.class))).thenReturn(pageData);

        // 执行测试 & 验证结果（带搜索参数）
        mockMvc.perform(get("/rest/user/page/list")
                        .param("page", "1")
                        .param("size", "10")
                        .param("name", "李四1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.count").value(1));

        verify(userService, times(1)).pageList(any(UserPageSearchRequest.class));
    }

    @Test
    void testPageList_DefaultPaging() throws Exception {
        // 准备测试数据
        List<UserDTO> dtoList = new ArrayList<>();
        PageData<UserDTO> pageData = PageData.of(dtoList, 0, 10, 0);
        when(userService.pageList(any(UserPageSearchRequest.class))).thenReturn(pageData);

        // 执行测试（不传分页参数，使用默认值 page=1, size=10）
        mockMvc.perform(get("/rest/user/page/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        verify(userService, times(1)).pageList(any(UserPageSearchRequest.class));
    }

    // =====================================================================
    // add 测试
    // =====================================================================

    @Test
    void testAdd_Success() throws Exception {
        // 准备测试数据
        UserRequest request = new UserRequest();
        request.setId("10");
        request.setName("新用户");

        UserDTO userDTO = UserDTO.builder().id("10").name("新用户").build();
        when(userService.add(any(UserRequest.class))).thenReturn(userDTO);

        // 执行测试 & 验证结果
        mockMvc.perform(post("/rest/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("10"))
                .andExpect(jsonPath("$.name").value("新用户"));

        verify(userService, times(1)).add(any(UserRequest.class));
    }

    @Test
    void testAdd_NullRequest() throws Exception {
        // 空请求体应触发校验失败
        mockMvc.perform(post("/rest/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk());

        verify(userService, times(1)).add(any(UserRequest.class));
    }

    // =====================================================================
    // update 测试
    // =====================================================================

    @Test
    void testUpdate_Success() throws Exception {
        // 准备测试数据
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("更新名称");

        UserDTO updatedDTO = UserDTO.builder().id("1").name("更新名称").age(21).build();
        when(userService.update(eq("1"), any(UserUpdateRequest.class))).thenReturn(updatedDTO);

        // 执行测试 & 验证结果
        mockMvc.perform(put("/rest/user/update/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("更新名称"));

        verify(userService, times(1)).update(eq("1"), any(UserUpdateRequest.class));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        // 准备测试数据
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("更新名称");

        when(userService.update(eq("999"), any(UserUpdateRequest.class))).thenReturn(null);

        // 执行测试 & 验证结果
        mockMvc.perform(put("/rest/user/update/{id}", "999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(updateRequest)))
                .andExpect(status().isOk());

        verify(userService, times(1)).update(eq("999"), any(UserUpdateRequest.class));
    }

    @Test
    void testUpdate_BlankId() throws Exception {
        // 空白 id 应触发 @NotBlank 校验失败
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("更新名称");

        mockMvc.perform(put("/rest/user/update/{id}", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(updateRequest)))
                .andExpect(status().isMethodNotAllowed());

        verify(userService, never()).update(anyString(), any(UserUpdateRequest.class));
    }

    // =====================================================================
    // deleteById 测试
    // =====================================================================

    @Test
    void testDeleteById_Success() throws Exception {
        // 准备测试数据
        doNothing().when(userService).deleteById("1");

        // 执行测试 & 验证结果
        mockMvc.perform(delete("/rest/user/delete/{id}", "1"))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteById("1");
    }

    @Test
    void testDeleteById_BlankId() throws Exception {
        // 空白 id 应触发 @NotBlank 校验失败
        mockMvc.perform(delete("/rest/user/delete/{id}", ""))
                .andExpect(status().isMethodNotAllowed());

        verify(userService, never()).deleteById(anyString());
    }

    @Test
    void testDeleteById_VerifyServiceCalled() throws Exception {
        // 准备测试数据
        doNothing().when(userService).deleteById(anyString());

        // 执行测试
        mockMvc.perform(delete("/rest/user/delete/{id}", "5"))
                .andExpect(status().isOk());

        // 验证 deleteById 被调用了一次
        verify(userService, times(1)).deleteById("5");
        verify(userService, never()).findById(anyString());
    }
}
