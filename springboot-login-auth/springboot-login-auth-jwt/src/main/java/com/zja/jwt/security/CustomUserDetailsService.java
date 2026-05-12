package com.zja.jwt.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 用户详情服务实现（内存存储，演示用）
 * <p>
 * 关键设计：每次 loadUserByUsername() 返回新的 User 对象，
 * 防止 Spring Security 的 eraseCredentials() 将同一 User 实例的 password 置空。
 * （User.password 非 final，eraseCredentials() 会将其设为 null，
 *  若复用同一个 User 对象，后续登录会报 "Empty encoded password"）
 * <p>
 * 生产环境应替换为数据库查询实现
 *
 * @Author: zhengja
 * @Date: 2025-07-11
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final List<UserData> userDataList;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        userDataList = Arrays.asList(
                new UserData("admin", passwordEncoder.encode("admin123"), "ADMIN", "USER"),
                new UserData("user", passwordEncoder.encode("user123"), "USER")
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 每次返回新的 User 对象，避免 eraseCredentials() 污染存储数据
        return userDataList.stream()
                .filter(u -> u.username.equals(username))
                .findFirst()
                .map(u -> User.builder()
                        .username(u.username)
                        .password(u.encodedPassword)
                        .roles(u.roles)
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
    }

    /**
     * 不可变的用户数据存储（password 不会被 eraseCredentials() 影响）
     */
    private static class UserData {
        final String username;
        final String encodedPassword;
        final String[] roles;

        UserData(String username, String encodedPassword, String... roles) {
            this.username = username;
            this.encodedPassword = encodedPassword;
            this.roles = roles;
        }
    }
}
