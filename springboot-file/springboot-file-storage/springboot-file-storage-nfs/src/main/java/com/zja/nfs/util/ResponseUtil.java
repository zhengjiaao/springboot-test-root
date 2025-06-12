package com.zja.nfs.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2025-06-12 14:59
 */
public class ResponseUtil {

    /**
     * 构建成功响应
     */
    public static Map<String, Object> success() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }

    /**
     * 构建带消息的成功响应
     */
    public static Map<String, Object> success(String message) {
        Map<String, Object> response = success();
        response.put("message", message);
        return response;
    }

    /**
     * 构建带数据的成功响应（默认 key 为 data）
     */
    public static Map<String, Object> successData(Object data) {
        Map<String, Object> response = success();
        response.put("data", data);
        return response;
    }

    /**
     * 构建带自定义 key 的数据响应
     */
    public static Map<String, Object> successData(String key, Object value) {
        Map<String, Object> response = success();
        response.put(key, value);
        return response;
    }

    /**
     * 构建失败响应
     */
    public static Map<String, Object> error(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}

