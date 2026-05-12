package com.zja.mvc.rate.controller;

import com.zja.mvc.rate.model.*;
import com.zja.mvc.rate.ratelimit.RateLimit;
import com.zja.mvc.rate.ratelimit.RateLimitAlgorithm;
import com.zja.mvc.rate.ratelimit.RateLimitType;
import com.zja.mvc.rate.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理 Controller
 * <p>
 * 限流示例说明：
 * <ul>
 *   <li>queryById   - 全局限流，固定窗口，每秒最多 10 次</li>
 *   <li>pageList    - 按 IP 限流，滑动窗口，每分钟最多 60 次</li>
 *   <li>add         - 按 IP 限流，令牌桶，每秒最多 5 次，突发容量 10</li>
 *   <li>update      - 按用户限流，漏桶，每秒最多 3 次</li>
 *   <li>deleteById  - 自定义 key（从请求头取用户名），每分钟最多 10 次</li>
 * </ul>
 *
 * @author: zhengja
 * @since: 2024/03/11 15:19
 */
@Validated
@RestController
@RequestMapping("/rest/user")
@Api(tags = {"用户管理页面"})
public class UserController {

    @Autowired
    UserService service;

    /**
     * 全局限流（固定窗口）：所有人共享每秒 10 次配额
     */
    @RateLimit(
            limit = 10,
            period = 1,
            timeUnit = TimeUnit.SECONDS,
            type = RateLimitType.GLOBAL,
            algorithm = RateLimitAlgorithm.FIXED_WINDOW,
            message = "查询请求过于频繁，请稍后再试"
    )
    @GetMapping("/query/{id}")
    @ApiOperation("查询单个用户管理详情")
    public UserDTO queryById(@NotBlank @PathVariable("id") String id) {
        return service.findById(id);
    }

    /**
     * 按 IP 限流（滑动窗口）：每个 IP 每分钟最多访问 60 次，本地 IP 加入白名单
     */
    @RateLimit(
            limit = 60,
            period = 1,
            timeUnit = TimeUnit.MINUTES,
            type = RateLimitType.IP,
            algorithm = RateLimitAlgorithm.SLIDING_WINDOW,
            message = "列表查询请求过于频繁，请稍后再试",
            whitelistIps = {"127.0.0.1", "0:0:0:0:0:0:0:1"}
    )
    @GetMapping("/page/list")
    @ApiOperation("分页查询用户管理列表")
    public PageData<UserDTO> pageList(@Valid UserPageSearchRequest pageSearchRequest) {
        return service.pageList(pageSearchRequest);
    }

    /**
     * 按 IP 限流（令牌桶）：每秒补充 5 个令牌，桶容量 10（允许短时突发）
     */
    @RateLimit(
            limit = 5,
            period = 1,
            timeUnit = TimeUnit.SECONDS,
            type = RateLimitType.IP,
            algorithm = RateLimitAlgorithm.TOKEN_BUCKET,
            burstCapacity = 10,
            message = "新增请求过于频繁，请稍后再试"
    )
    @PostMapping("/add")
    @ApiOperation("添加用户管理")
    public UserDTO add(@Valid @RequestBody UserRequest request) {
        return service.add(request);
    }

    /**
     * 按用户限流（漏桶）：每个用户每秒最多 3 次，输出平滑稳定
     */
    @RateLimit(
            limit = 3,
            period = 1,
            timeUnit = TimeUnit.SECONDS,
            type = RateLimitType.USER,
            algorithm = RateLimitAlgorithm.LEAKY_BUCKET,
            message = "更新请求过于频繁，请稍后再试"
    )
    @PutMapping("/update/{id}")
    @ApiOperation("更新用户管理")
    public UserDTO update(@NotBlank @PathVariable("id") String id,
                          @Valid @RequestBody UserUpdateRequest updateRequest) {
        return service.update(id, updateRequest);
    }

    /**
     * 自定义 key 限流（滑动窗口）：按请求头 X-User-Name 每分钟最多 10 次
     * 若无该请求头，key 降级为 "null"
     */
    @RateLimit(
            limit = 10,
            period = 1,
            timeUnit = TimeUnit.MINUTES,
            type = RateLimitType.CUSTOM,
            customKey = "#request.getHeader('X-User-Name')",
            algorithm = RateLimitAlgorithm.SLIDING_WINDOW,
            message = "删除请求过于频繁，请稍后再试"
    )
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除用户管理")
    public void deleteById(@NotBlank @PathVariable("id") String id) {
        service.deleteById(id);
    }

}