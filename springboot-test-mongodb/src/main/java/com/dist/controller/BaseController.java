package com.dist.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 提供统一的Controller服务
 *
 *  @author yinxp@dist.com.cn
 */
@Slf4j
@RestController
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
