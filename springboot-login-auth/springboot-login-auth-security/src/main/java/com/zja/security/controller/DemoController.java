package com.zja.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 演示接口 - 角色权限控制
 * <p>
 * 展示基于@PreAuthorize的方法级权限控制
 *
 * @author: zhengja
 * @since: 2026/05/12
 */
@RestController
@RequestMapping("/api/demo")
@Api(tags = {"权限演示接口"})
public class DemoController {

    @GetMapping("/public")
    @ApiOperation("公开接口（无需认证）")
    public ResponseEntity<Map<String, Object>> publicApi() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "这是公开接口，无需登录即可访问");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user")
    @ApiOperation("用户接口（需登录）")
    public ResponseEntity<Map<String, Object>> userApi(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "你好, " + userDetails.getUsername() + "！这是用户接口");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/admin")
    @ApiOperation("管理员接口（需ADMIN角色）")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> adminApi(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "你好, " + userDetails.getUsername() + "！这是管理员专属接口");
        return ResponseEntity.ok(result);
    }

    @GetMapping("/profile")
    @ApiOperation("获取当前用户详细信息")
    public ResponseEntity<Map<String, Object>> profile(@AuthenticationPrincipal UserDetails userDetails) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", userDetails.getUsername());
        data.put("authorities", userDetails.getAuthorities());
        data.put("accountNonExpired", userDetails.isAccountNonExpired());
        data.put("accountNonLocked", userDetails.isAccountNonLocked());
        data.put("credentialsNonExpired", userDetails.isCredentialsNonExpired());
        data.put("enabled", userDetails.isEnabled());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "获取成功");
        result.put("data", data);
        return ResponseEntity.ok(result);
    }
}
