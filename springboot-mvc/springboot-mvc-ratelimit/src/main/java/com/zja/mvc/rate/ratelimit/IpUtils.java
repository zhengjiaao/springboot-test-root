package com.zja.mvc.rate.ratelimit;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * IP 地址工具类：支持代理穿透、IPv6 处理
 *
 * @author: zhengja
 * @since: 2024/03/11
 */
public class IpUtils {

    private static final String UNKNOWN = "unknown";
    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
    private static final String LOCALHOST_IPV4 = "127.0.0.1";

    private IpUtils() {
    }

    /**
     * 获取客户端真实 IP，支持 Nginx/CDN 多级代理透传
     *
     * @param request HttpServletRequest
     * @return 客户端 IP 地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (!isValidIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (!isValidIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (!isValidIp(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (!isValidIp(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (!isValidIp(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理多级代理（取第一个非 unknown IP）
        if (ip != null && ip.contains(",")) {
            String[] parts = ip.split(",");
            for (String part : parts) {
                String trimmed = part.trim();
                if (isValidIp(trimmed)) {
                    ip = trimmed;
                    break;
                }
            }
        }

        // IPv6 本地地址转 IPv4
        if (LOCALHOST_IPV6.equals(ip)) {
            ip = LOCALHOST_IPV4;
        }

        return ip != null ? ip.trim() : LOCALHOST_IPV4;
    }

    private static boolean isValidIp(String ip) {
        return StringUtils.hasText(ip) && !UNKNOWN.equalsIgnoreCase(ip);
    }
}
