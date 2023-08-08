package com.zja.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 项目地址 封装 工具类
 */
public abstract class ContextPathUtil {

    /**
     * 获取根节点contextPath
     *
     * @param request
     * @return http://localhost:8080/file  or http://ip:port/contextPath
     */
    public static String getRootContextPath(HttpServletRequest request) {
        String rootUrl = request.getRequestURL().toString();
        String contextPath = request.getContextPath();
        return rootUrl.substring(0, rootUrl.indexOf(contextPath) + contextPath.length());
    }
}
