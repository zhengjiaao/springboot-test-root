package com.zja.onlyoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: zhengja
 * @Date: 2025-01-22 15:31
 */
@Controller
public class HtmlController {

    // 显示转换页面 http://localhost:8080/doc/converter
    @GetMapping("/doc/converter")
    public String showConverterPage() {
        return "DocConverter"; // 返回 Thymeleaf 模板名称
    }
}
