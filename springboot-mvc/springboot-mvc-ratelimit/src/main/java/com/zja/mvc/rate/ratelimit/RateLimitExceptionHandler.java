package com.zja.mvc.rate.ratelimit;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 限流全局异常处理器：捕获 RateLimitException，统一返回 429 响应体
 *
 * @author: zhengja
 * @since: 2024/03/11
 */
@RestControllerAdvice
public class RateLimitExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RateLimitExceptionHandler.class);

    @ExceptionHandler(RateLimitException.class)
    public void handleRateLimitException(RateLimitException ex, HttpServletResponse response) throws IOException {
        log.warn("[RateLimit] 限流异常已捕获: {}", ex.getMessage());
        if (!response.isCommitted()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
            Map<String, Object> result = new HashMap<>();
            result.put("code", HttpStatus.TOO_MANY_REQUESTS.value());
            result.put("message", ex.getMessage());
            result.put("success", false);
            result.put("timestamp", System.currentTimeMillis());
            response.getWriter().write(JSON.toJSONString(result));
        }
    }
}
