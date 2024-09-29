package com.zja.hanbian.controller;

import com.zja.hanbian.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhengja
 * @Date: 2024-09-20 17:39
 */
@RestController
@RequestMapping
public class UserManagementController {

    @Autowired
    UserManagementService userManagementService;

    @PostMapping(value = "/addUser")
    public void addUser() {
        userManagementService.addUser("李四", "123456");
    }

    @PutMapping(value = "/updateUser")
    public void updateUser() {
        userManagementService.updateUser("李四", "654321");
    }

    @GetMapping(value = "/validateUser")
    public void validateUser() {
        userManagementService.validateUser("李四", "123456");
    }

    @DeleteMapping(value = "/deleteUser")
    public void deleteUser() {
        userManagementService.deleteUser("李四");
    }
}
