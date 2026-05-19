package com.zja.security.controller;

import com.zja.security.config.CustomUserDetailsService;
import com.zja.security.model.AddUserRequest;
import com.zja.security.model.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理 接口层（仅ADMIN角色可访问）
 * <p>
 * 权限说明：
 * - GET    /api/users              — 查询所有用户（ADMIN）
 * - POST   /api/users              — 新增用户（ADMIN）
 * - PUT    /api/users/{username}/status  — 启用/禁用用户（ADMIN）
 * - PUT    /api/users/{username}/reset-password — 重置密码（ADMIN）
 * - DELETE /api/users/{username}   — 删除用户（ADMIN）
 *
 * @author: zhengja
 * @since: 2026/05/12
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/users")
@Api(tags = {"用户管理"})
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final CustomUserDetailsService userDetailsService;

    public UserController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    @ApiOperation("查询所有用户")
    public ResponseEntity<List<UserInfo>> list() {
        return ResponseEntity.ok(userDetailsService.findAllUsers());
    }

    @PostMapping
    @ApiOperation("新增用户")
    public ResponseEntity<Map<String, Object>> add(@Valid @RequestBody AddUserRequest request) {
        List<String> roles = request.getRoles();
        String[] roleArray;
        if (roles == null || roles.isEmpty()) {
            roleArray = new String[]{"USER"};
        } else {
            roleArray = roles.toArray(new String[0]);
        }
        userDetailsService.addUser(request.getUsername(), request.getPassword(), roleArray);
        log.info("新增用户成功: {}", request.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "用户创建成功");
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{username}/status")
    @ApiOperation("启用/禁用用户")
    public ResponseEntity<Map<String, Object>> toggleStatus(
            @PathVariable String username,
            @RequestParam boolean enabled) {
        userDetailsService.toggleUserStatus(username, enabled);
        log.info("用户状态变更: {} -> {}", username, enabled ? "启用" : "禁用");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", enabled ? "用户已启用" : "用户已禁用");
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{username}/reset-password")
    @ApiOperation("重置用户密码")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @PathVariable String username,
            @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("新密码不能为空");
        }
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("密码长度至少6个字符");
        }
        userDetailsService.resetPassword(username, newPassword);
        log.info("管理员重置用户密码: {}", username);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "密码重置成功");
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{username}")
    @ApiOperation("删除用户")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable String username) {
        userDetailsService.deleteUser(username);
        log.info("删除用户成功: {}", username);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "用户删除成功");
        return ResponseEntity.ok(result);
    }
}
