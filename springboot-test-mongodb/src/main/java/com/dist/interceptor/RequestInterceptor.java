package com.dist.interceptor;

import com.dist.common.LocalFileService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 图片加载拦截器
 * @author yinxp@dist.com.cn
 * @date 2018/9/26
 */
public class RequestInterceptor implements HandlerInterceptor {

    @Resource
    private LocalFileService localFileService;

    /**
     * 在访问图片之前，先将数据从mongo中写入tomcat目录
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 此处servletPath的值形如
         * /rest/resource/2c90906b65a9ea9e0165a9ecba910000.jpg
         */
        String servletPath = request.getServletPath();
        String filePath = servletPath.substring(servletPath.lastIndexOf("/")+1, servletPath.length());
        localFileService.transfer(filePath,request);
        return true;
    }
}
