package com.zja.juc.threadLocal.user;

/**
 * @author: zhengja
 * @since: 2024/03/27 16:47
 */
public class UserContext {
    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    public static User getUser() {
        return userThreadLocal.get();
    }

    public static void clear() {
        userThreadLocal.remove();
    }
}


