package com.zja.onlyoffice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zhengja
 * @Date: 2025-01-16 11:09
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/service")
    public String serviceA() {
        return "Welcome to Service!";
    }
}