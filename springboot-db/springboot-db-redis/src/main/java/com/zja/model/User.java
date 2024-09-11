package com.zja.model;

import java.io.Serializable;

/**
 *
 * @Author: zhengja
 * @Date: 2024-09-11 15:01
 */
public class User implements Serializable {

    private String id;
    private String name;
    private String password;
    private String email;

    public User() {
    }

    public User(String id, String name, String password, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
