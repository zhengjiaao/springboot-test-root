package com.zja.hanbian.controller;

import com.zja.hanbian.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zhengja
 * @Date: 2024-09-20 17:39
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/user")
public class UserManagementController {

    @Autowired
    UserManagementService userManagementService;

    @PostMapping(value = "/addUser")
    public ResponseEntity<?> addUser() {
        String result = userManagementService.addUser("李四", "123456");

        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/updateUser")
    public ResponseEntity<?> updateUser() {
        String result = userManagementService.updateUser("李四", "654321");

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/validateUser")
    public ResponseEntity<?> validateUser() {
        boolean result = userManagementService.validateUser("李四", "123456");

        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/deleteUser")
    public ResponseEntity<?> deleteUser() {
        String result = userManagementService.deleteUser("李四");

        return ResponseEntity.ok(result);
    }
}
