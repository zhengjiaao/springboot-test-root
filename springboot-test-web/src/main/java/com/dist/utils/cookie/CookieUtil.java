package com.dist.utils.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * cookie操作
 *
 *  @author yinxp@dist.com.cn
 */
public class CookieUtil {

    // 有效时间3天
    private static final int MAXAGE = 3*24*60*60;

    /**
     * 添加cookie
     *
     * @param response
     * @param name
     * @param value
     */
    public static void addCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(MAXAGE);
//        cookie.setDomain(DOMAIN); // cookie作用域
        response.addCookie(cookie);
    }

    /**
     * 检索所有cookie 封装到map集合 以其cookie name作为key cookie value作为value
     *
     * @param request
     * @return
     */
    public static Map<String, String> readCookieMap(HttpServletRequest request) {
        Map<String, String> cookieMap = new HashMap<String, String>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookieMap;
    }

    /**
     * 通过cookie name 获取 cookie value
     *
     * @param request
     * @param name
     * @return
     */
    public static String getCookieValueByName(HttpServletRequest request, String name) {
        Map<String, String> cookieMap = readCookieMap(request);
        if (cookieMap.containsKey(name)) {
            String cookieValue = (String) cookieMap.get(name);
            return cookieValue;
        } else {
            return null;
        }
    }

    /**
     * 删除指定cookie
     * @param request
     * @param response
     * @param name
     */
    public static void deleteCookieByName(HttpServletRequest request,HttpServletResponse response,String name) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(name)){
                    cookie.setValue(null);
                    cookie.setMaxAge(0);// 立即销毁cookie
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * 更新cookie
     * @param request
     * @param response
     * @param name
     * @param value
     */
    public static void updateCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(name)){
                    cookie.setValue(value);
                    response.addCookie(cookie);
                }
            }
        }

    }


}
