package com.zja.placeholder.service;

import org.springframework.stereotype.Service;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Map;

/**
 * JSON 模板占位符替换服务
 * <p>
 * 用于替换 JSON 模板中的占位符，支持自定义占位符格式
 * 默认使用 %占位符% 格式
 * </p>
 *
 * @Author: zhengja
 * @Date: 2025-09-17 14:42
 */
@Service
public class PlaceholderService {

    private final PropertyPlaceholderHelper helper;

    /**
     * 默认构造函数
     * <p>使用 % 作为占位符的前缀和后缀</p>
     * <p>示例:
     * <pre>
     * {@code
     * JsonPlaceholderService service = new JsonPlaceholderService();
     * String template = "Hello %name%, you are %age% years old";
     * Map<String, String> params = Map.of("name", "Tom", "age", "25");
     * String result = service.replacePlaceholders(template, params);
     * // 结果: "Hello Tom, you are 25 years old"
     * }
     * </pre>
     * </p>
     */
    public PlaceholderService() {
        this.helper = new PropertyPlaceholderHelper("%", "%");
        // this.helper = new PropertyPlaceholderHelper("${", "}");
    }

    /**
     * 自定义占位符构造函数
     * <p>允许自定义占位符的前缀和后缀</p>
     * <p>示例:
     * <pre>
     * {@code
     * JsonPlaceholderService service = new JsonPlaceholderService("${", "}");
     * String template = "Hello ${name}, you are ${age} years old";
     * Map<String, String> params = Map.of("name", "Tom", "age", "25");
     * String result = service.replacePlaceholders(template, params);
     * // 结果: "Hello Tom, you are 25 years old"
     * }
     * </pre>
     * </p>
     *
     * @param placeholderPrefix 占位符前缀，如 "${"
     * @param placeholderSuffix 占位符后缀，如 "}"
     */
    public PlaceholderService(String placeholderPrefix, String placeholderSuffix) {
        this.helper = new PropertyPlaceholderHelper(placeholderPrefix, placeholderSuffix);
    }

    /**
     * 根据传入的参数替换 JSON 模板中的占位符
     * <p>使用已配置的占位符格式替换模板中的占位符</p>
     * <p>示例:
     * <pre>
     * {@code
     * // 使用默认 % 占位符
     * JsonPlaceholderService service = new JsonPlaceholderService();
     * String template = "User: %username%, Email: %email%";
     * Map<String, String> params = Map.of(
     *     "username", "john_doe",
     *     "email", "john@example.com"
     * );
     * String result = service.replacePlaceholders(template, params);
     * // 结果: "User: john_doe, Email: john@example.com"
     * }
     * </pre>
     * </p>
     *
     * @param jsonTemplate 原始 JSON 模板字符串，包含占位符
     *                     示例: {"name": "%name%", "age": %age%}
     * @param params       参数映射，key为占位符名称，value为替换值
     *                     示例: {"name": "Tom", "age": "25"}
     * @return 替换占位符后的新字符串
     * 示例: {"name": "Tom", "age": 25}
     */
    public String replacePlaceholders(String jsonTemplate, Map<String, String> params) {
        return helper.replacePlaceholders(jsonTemplate, params::get);
    }
}
