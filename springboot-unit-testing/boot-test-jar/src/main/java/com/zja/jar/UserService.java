package com.zja.jar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: zhengja
 * @Date: 2024-09-20 14:35
 */
@Service
public class UserService {

    @Value("${name}")
    private String name;

    public String getUserName(){
        return name;
    }
}
