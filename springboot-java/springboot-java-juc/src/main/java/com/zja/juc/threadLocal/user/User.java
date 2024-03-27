package com.zja.juc.threadLocal.user;

/**
 * @author: zhengja
 * @since: 2024/03/27 16:47
 */
public class User {
    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}