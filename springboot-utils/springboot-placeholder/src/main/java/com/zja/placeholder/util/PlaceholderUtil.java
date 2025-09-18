package com.zja.placeholder.util;

import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Map;

/**
 * 占位符替换工具类
 * <p>
 * <p>
 * <p>
 * 用于替换文本模板中的占位符，支持自定义占位符格式
 * 默认使用 %占位符% 格式
 * </p>
 *
 * @Author: zhengja
 * @Date: 2025-09-17 14:51
 */
public final class PlaceholderUtil {

    /**
     * 私有构造函数，防止实例化
     */
    private PlaceholderUtil() {
    }

    /**
     * 使用默认占位符 % 替换模板中的占位符
     *
     * @param template 原始模板字符串，包含占位符
     *                 示例: "Hello %name%, you are %age% years old"
     * @param params   参数映射，key为占位符名称，value为替换值
     *                 示例: {"name": "Tom", "age": "25"}
     * @return 替换占位符后的新字符串
     */
    public static String replacePlaceholders(String template, Map<String, String> params) {
        return replacePlaceholders(template, params, "%", "%");
    }

    /**
     * 使用自定义占位符替换模板中的占位符
     *
     * @param template 原始模板字符串，包含占位符
     *                 示例: "Hello ${name}, you are ${age} years old"
     * @param params   参数映射，key为占位符名称，value为替换值
     *                 示例: {"name": "Tom", "age": "25"}
     * @param prefix   占位符前缀，如 "${" 或 "%"
     * @param suffix   占位符后缀，如 "}" 或 "%"
     * @return 替换占位符后的新字符串
     */
    public static String replacePlaceholders(String template, Map<String, String> params,
                                             String prefix, String suffix) {
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(prefix, suffix);
        return helper.replacePlaceholders(template, params::get);
    }
}
