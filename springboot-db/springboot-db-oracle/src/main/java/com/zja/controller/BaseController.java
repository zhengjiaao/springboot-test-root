package com.zja.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @program: springbootdemo
 * @Date: 2019/1/14 10:15
 * @Author: Mr.Zheng
 * @Description:
 */
@RestController
@Slf4j
public class BaseController {

    protected HttpSession session;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    @ModelAttribute
    protected void init(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession();

        // 打印request
//        VisitLog.printRequest(request);
        // 记录访问日志
//        VisitLog.record(request);
    }

}
