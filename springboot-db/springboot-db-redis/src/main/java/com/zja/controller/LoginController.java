package com.zja.controller;

import com.zja.redis.login.LoginService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Author: zhengja
 * @Date: 2024-09-11 14:59
 */
@RestController
@RequestMapping(value = "/rest/v1")
@Api(tags = {"LoginController"})
public class LoginController {

    @Autowired
    LoginService loginService;

    @GetMapping("login")
    public boolean login(String username, String password) {
        return loginService.login(username, password);
    }

}
