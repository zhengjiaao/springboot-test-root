package com.zja.init.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: zhengja
 * @Date: 2025-09-18 16:06
 */
@Service
public class UserService {

    @Value("${userInfo.name}")
    private String userName;

    @Value("${userInfo.age}")
    private Integer userAge;

    public String getUserInfo() {
        return "用户名：" + userName + "，年龄：" + userAge;
    }
}
