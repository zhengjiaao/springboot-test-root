package com.zja.security.config;

import com.zja.security.model.UserInfo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 用户详情服务实现（内存存储，演示用）
 * <p>
 * 关键设计：使用不可变的UserData类存储用户数据，
 * 每次loadUserByUsername()返回新的User对象，
 * 防止Spring Security的eraseCredentials()将同一User实例的password置空。
 * （User.password非final，eraseCredentials()会将其设为null，
 *  若复用同一个User对象，后续登录会报"Empty encoded password"）
 * <p>
 * 生产环境应替换为数据库查询实现
 *
 * @author: zhengja
 * @since: 2026/05/12
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, UserData> userDataStore = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        // 初始化预置用户
        addUserInternal("admin", passwordEncoder.encode("admin123"), true, "ADMIN", "USER");
        addUserInternal("user", passwordEncoder.encode("user123"), true, "USER");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData userData = userDataStore.get(username);
        if (userData == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        // 每次返回新的User对象，避免eraseCredentials()污染存储数据
        return User.builder()
                .username(userData.username)
                .password(userData.encodedPassword)
                .roles(userData.roles)
                .disabled(!userData.enabled)
                .build();
    }

    // ===== 用户管理操作（供Controller调用）=====

    /**
     * 查询所有用户
     */
    public List<UserInfo> findAllUsers() {
        return userDataStore.values().stream()
                .map(u -> new UserInfo(u.username, Arrays.asList(u.roles), u.enabled))
                .sorted((a, b) -> a.getUsername().compareTo(b.getUsername()))
                .collect(Collectors.toList());
    }

    /**
     * 新增用户
     */
    public void addUser(String username, String rawPassword, String... roles) {
        if (userDataStore.containsKey(username)) {
            throw new IllegalArgumentException("用户已存在: " + username);
        }
        if (roles == null || roles.length == 0) {
            roles = new String[]{"USER"};
        }
        addUserInternal(username, passwordEncoder.encode(rawPassword), true, roles);
    }

    /**
     * 删除用户
     */
    public void deleteUser(String username) {
        if (!userDataStore.containsKey(username)) {
            throw new IllegalArgumentException("用户不存在: " + username);
        }
        if ("admin".equals(username)) {
            throw new IllegalArgumentException("不能删除内置管理员账号");
        }
        userDataStore.remove(username);
    }

    /**
     * 切换用户启用/禁用状态
     */
    public void toggleUserStatus(String username, boolean enabled) {
        UserData userData = userDataStore.get(username);
        if (userData == null) {
            throw new IllegalArgumentException("用户不存在: " + username);
        }
        if ("admin".equals(username) && !enabled) {
            throw new IllegalArgumentException("不能禁用内置管理员账号");
        }
        userDataStore.put(username, new UserData(
                userData.username, userData.encodedPassword, enabled, userData.roles));
    }

    /**
     * 修改密码（用户自行修改，需验证原密码）
     */
    public void changePassword(String username, String oldPassword, String newPassword) {
        UserData userData = userDataStore.get(username);
        if (userData == null) {
            throw new IllegalArgumentException("用户不存在: " + username);
        }
        if (!passwordEncoder.matches(oldPassword, userData.encodedPassword)) {
            throw new IllegalArgumentException("原密码错误");
        }
        userDataStore.put(username, new UserData(
                userData.username, passwordEncoder.encode(newPassword), userData.enabled, userData.roles));
    }

    /**
     * 重置密码（管理员操作，无需验证原密码）
     */
    public void resetPassword(String username, String newPassword) {
        UserData userData = userDataStore.get(username);
        if (userData == null) {
            throw new IllegalArgumentException("用户不存在: " + username);
        }
        userDataStore.put(username, new UserData(
                userData.username, passwordEncoder.encode(newPassword), userData.enabled, userData.roles));
    }

    /**
     * 检查用户是否存在
     */
    public boolean userExists(String username) {
        return userDataStore.containsKey(username);
    }

    // ===== 内部方法 =====

    private void addUserInternal(String username, String encodedPassword, boolean enabled, String... roles) {
        userDataStore.put(username, new UserData(username, encodedPassword, enabled, roles));
    }

    /**
     * 不可变的用户数据存储（password不会被eraseCredentials()影响）
     */
    static class UserData {
        final String username;
        final String encodedPassword;
        final boolean enabled;
        final String[] roles;

        UserData(String username, String encodedPassword, boolean enabled, String... roles) {
            this.username = username;
            this.encodedPassword = encodedPassword;
            this.enabled = enabled;
            this.roles = roles;
        }
    }
}
