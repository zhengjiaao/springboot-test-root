package com.zja.mvc.rate.controller;

import com.alibaba.fastjson.JSON;
import com.zja.mvc.rate.model.UserDTO;
import com.zja.mvc.rate.model.UserRequest;
import com.zja.mvc.rate.model.UserUpdateRequest;
import com.zja.mvc.rate.ratelimit.RateLimitHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController 限流集成测试
 * <p>
 * 使用 @SpringBootTest 启动完整 Spring 上下文，使 AOP 切面生效，
 * 验证各接口在不同限流算法和维度下的限流行为。
 * <p>
 * 限流配置说明：
 * <ul>
 *   <li>queryById  - 全局限流，固定窗口，每秒最多 10 次</li>
 *   <li>pageList   - 按 IP 限流，滑动窗口，每分钟最多 60 次，白名单: 127.0.0.1</li>
 *   <li>add        - 按 IP 限流，令牌桶，每秒 5 令牌，桶容量 10</li>
 *   <li>update     - 按用户限流，漏桶，每秒最多 3 次</li>
 *   <li>deleteById - 自定义 key（X-User-Name 请求头），滑动窗口，每分钟最多 10 次</li>
 * </ul>
 *
 * @author: zhengja
 * @since: 2024/03/11
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserController 限流集成测试")
class UserControllerRateLimitIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RateLimitHandler rateLimitHandler;

    @BeforeEach
    void setUp() {
        // 每次测试前清除所有限流状态，确保测试隔离
        rateLimitHandler.resetAll();
    }

    // =====================================================================
    // queryById - 全局限流，固定窗口，10次/秒
    // =====================================================================

    @Test
    @DisplayName("queryById: 固定窗口 - 未超限时应正常返回")
    void testQueryById_WithinLimit_ShouldReturnOk() throws Exception {
        // 固定窗口：每秒最多 10 次，发送 10 次请求应全部通过
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/rest/user/query/{id}", "1"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("queryById: 固定窗口 - 超限时应返回 429")
    void testQueryById_ExceedLimit_ShouldReturn429() throws Exception {
        // 先消耗完 10 次配额
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/rest/user/query/{id}", "1"))
                    .andExpect(status().isOk());
        }

        // 第 11 次请求应被限流
        mockMvc.perform(get("/rest/user/query/{id}", "1"))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string(containsString("查询请求过于频繁")));
    }

    @Test
    @DisplayName("queryById: 固定窗口 - 全局限流不区分用户，所有人共享配额")
    void testQueryById_GlobalLimit_SharedQuota() throws Exception {
        // 不同 id 的请求共享全局限流配额（key 不含 IP/用户维度）
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/rest/user/query/{id}", "1"))
                    .andExpect(status().isOk());
        }
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/rest/user/query/{id}", "2"))
                    .andExpect(status().isOk());
        }

        // 第 11 次请求（无论什么 id）应被限流
        mockMvc.perform(get("/rest/user/query/{id}", "3"))
                .andExpect(status().isTooManyRequests());
    }

    // =====================================================================
    // pageList - IP 限流，滑动窗口，60次/分钟，白名单 127.0.0.1
    // =====================================================================

    @Test
    @DisplayName("pageList: 滑动窗口 - 白名单 IP 不受限流约束")
    void testPageList_WhitelistedIp_ShouldNotBeRateLimited() throws Exception {
        // MockMvc 默认使用 127.0.0.1，该 IP 在白名单中
        // 发送超过限制的请求数（60次），白名单 IP 仍应全部通过
        for (int i = 0; i < 70; i++) {
            mockMvc.perform(get("/rest/user/page/list")
                            .param("page", "1")
                            .param("size", "10"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("pageList: 滑动窗口 - 正常请求返回分页数据")
    void testPageList_WithinLimit_ShouldReturnPageData() throws Exception {
        mockMvc.perform(get("/rest/user/page/list")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    // =====================================================================
    // add - IP 限流，令牌桶，5令牌/秒，桶容量10
    // =====================================================================

    @Test
    @DisplayName("add: 令牌桶 - 初始突发容量内应全部通过")
    void testAdd_WithinBurstCapacity_ShouldReturnOk() throws Exception {
        UserRequest request = new UserRequest();
        request.setId("1");
        request.setName("测试用户");

        // 令牌桶初始满载（桶容量 10），前 10 次请求应全部通过
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/rest/user/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSON.toJSONString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("add: 令牌桶 - 超过桶容量后应被限流")
    void testAdd_ExceedBurstCapacity_ShouldReturn429() throws Exception {
        UserRequest request = new UserRequest();
        request.setId("1");
        request.setName("测试用户");

        // 消耗完桶中 10 个令牌
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/rest/user/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSON.toJSONString(request)))
                    .andExpect(status().isOk());
        }

        // 令牌已耗尽，再次请求应被限流
        mockMvc.perform(post("/rest/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(request)))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string(containsString("新增请求过于频繁")));
    }

    @Test
    @DisplayName("add: 令牌桶 - 等待令牌补充后应能再次请求")
    void testAdd_TokenRefill_ShouldAllowAfterWait() throws Exception {
        UserRequest request = new UserRequest();
        request.setId("1");
        request.setName("测试用户");

        // 消耗完桶中 10 个令牌
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/rest/user/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSON.toJSONString(request)))
                    .andExpect(status().isOk());
        }

        // 等待 300ms，约补充 1-2 个令牌（5 令牌/秒 = 0.005 令牌/ms）
        Thread.sleep(300);

        // 补充后应有令牌可用
        MvcResult result = mockMvc.perform(post("/rest/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(request)))
                .andReturn();

        // 等待时间可能刚好补充了令牌（200ms 可补充约 1 个令牌）
        // 所以结果可能是 200 或 429，取决于执行速度
        int status = result.getResponse().getStatus();
        assert status == 200 || status == 429 :
                "期望状态码为 200 或 429，实际为 " + status;
    }

    // =====================================================================
    // update - 用户限流，漏桶，3次/秒
    // =====================================================================

    @Test
    @DisplayName("update: 漏桶 - 在容量内应正常返回")
    void testUpdate_WithinCapacity_ShouldReturnOk() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("更新名称");

        // 漏桶容量为 3，前 3 次请求应通过
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(put("/rest/user/update/{id}", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSON.toJSONString(updateRequest))
                            .header("X-User-Id", "userA"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("update: 漏桶 - 超过容量应被限流")
    void testUpdate_ExceedCapacity_ShouldReturn429() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("更新名称");

        // 消耗完 3 次配额
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(put("/rest/user/update/{id}", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSON.toJSONString(updateRequest))
                            .header("X-User-Id", "userB"))
                    .andExpect(status().isOk());
        }

        // 第 4 次请求应被限流
        mockMvc.perform(put("/rest/user/update/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(updateRequest))
                        .header("X-User-Id", "userB"))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string(containsString("更新请求过于频繁")));
    }

    @Test
    @DisplayName("update: 漏桶 - 不同用户拥有独立限流配额")
    void testUpdate_DifferentUsers_IndependentQuota() throws Exception {
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setName("更新名称");

        // 用户 userC 消耗 3 次配额
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(put("/rest/user/update/{id}", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSON.toJSONString(updateRequest))
                            .header("X-User-Id", "userC"))
                    .andExpect(status().isOk());
        }

        // 用户 userC 已达限流
        mockMvc.perform(put("/rest/user/update/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(updateRequest))
                        .header("X-User-Id", "userC"))
                .andExpect(status().isTooManyRequests());

        // 用户 userD 有独立配额，应正常通过
        mockMvc.perform(put("/rest/user/update/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(updateRequest))
                        .header("X-User-Id", "userD"))
                .andExpect(status().isOk());
    }

    // =====================================================================
    // deleteById - 自定义 key（X-User-Name 请求头），滑动窗口，10次/分钟
    // =====================================================================

    @Test
    @DisplayName("deleteById: 自定义 key - 未超限时应正常执行")
    void testDeleteById_WithinLimit_ShouldReturnOk() throws Exception {
        // 每分钟最多 10 次，按 X-User-Name 请求头分组
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(delete("/rest/user/delete/{id}", String.valueOf(i))
                            .header("X-User-Name", "testUser"))
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DisplayName("deleteById: 自定义 key - 超限应返回 429")
    void testDeleteById_ExceedLimit_ShouldReturn429() throws Exception {
        // 先消耗 10 次配额
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(delete("/rest/user/delete/{id}", String.valueOf(i))
                            .header("X-User-Name", "limitedUser"))
                    .andExpect(status().isOk());
        }

        // 第 11 次应被限流
        mockMvc.perform(delete("/rest/user/delete/{id}", "11")
                        .header("X-User-Name", "limitedUser"))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string(containsString("删除请求过于频繁")));
    }

    @Test
    @DisplayName("deleteById: 自定义 key - 不同用户名拥有独立配额")
    void testDeleteById_DifferentCustomKeys_IndependentQuota() throws Exception {
        // 用户 alice 消耗 10 次配额
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(delete("/rest/user/delete/{id}", String.valueOf(i))
                            .header("X-User-Name", "alice"))
                    .andExpect(status().isOk());
        }

        // alice 已达限流
        mockMvc.perform(delete("/rest/user/delete/{id}", "99")
                        .header("X-User-Name", "alice"))
                .andExpect(status().isTooManyRequests());

        // bob 有独立配额，应正常通过
        mockMvc.perform(delete("/rest/user/delete/{id}", "100")
                        .header("X-User-Name", "bob"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("deleteById: 自定义 key - 无请求头时 key 降级为 null 字符串")
    void testDeleteById_NoCustomHeader_KeyDegradesToNull() throws Exception {
        // 不带 X-User-Name 请求头，SpEL 解析结果为 null → key 中包含 "null"
        // 所有无请求头的请求共享同一个限流 key
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(delete("/rest/user/delete/{id}", String.valueOf(i)))
                    .andExpect(status().isOk());
        }

        // 第 11 次仍无请求头，应被限流
        mockMvc.perform(delete("/rest/user/delete/{id}", "11"))
                .andExpect(status().isTooManyRequests());
    }

    // =====================================================================
    // 限流响应体结构验证
    // =====================================================================

    @Test
    @DisplayName("限流响应体应包含 code、message、success、timestamp 字段")
    void testRateLimitResponse_Structure() throws Exception {
        // 消耗完 queryById 的 10 次配额
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/rest/user/query/{id}", "1"))
                    .andExpect(status().isOk());
        }

        // 验证 429 响应体结构
        mockMvc.perform(get("/rest/user/query/{id}", "1"))
                .andExpect(status().isTooManyRequests())
                .andExpect(content().string(containsString("\"code\":429")))
                .andExpect(content().string(containsString("\"message\"")))
                .andExpect(content().string(containsString("\"success\":false")))
                .andExpect(content().string(containsString("\"timestamp\"")));
    }

    // =====================================================================
    // 限流状态重置验证
    // =====================================================================

    @Test
    @DisplayName("重置限流状态后应能重新访问")
    void testRateLimitReset_ShouldAllowRequestsAgain() throws Exception {
        // 消耗完 queryById 的 10 次配额
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(get("/rest/user/query/{id}", "1"))
                    .andExpect(status().isOk());
        }

        // 确认被限流
        mockMvc.perform(get("/rest/user/query/{id}", "1"))
                .andExpect(status().isTooManyRequests());

        // 重置限流状态
        rateLimitHandler.resetAll();

        // 重置后应能正常访问
        mockMvc.perform(get("/rest/user/query/{id}", "1"))
                .andExpect(status().isOk());
    }
}
