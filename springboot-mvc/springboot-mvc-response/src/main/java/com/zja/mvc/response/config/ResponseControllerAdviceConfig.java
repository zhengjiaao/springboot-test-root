package com.zja.mvc.response.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zja.mvc.response.model.response.ResponseUtil;
import com.zja.mvc.response.model.response.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author: zhengja
 * @since: 2024/03/11 15:45
 */
@Slf4j
@RestControllerAdvice(basePackages = {"com.zja.mvc.controller"})  // 扫描的包
public class ResponseControllerAdviceConfig implements ResponseBodyAdvice<Object> {

    // 校验 controller 中方法返回值类型
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        // return false;  // 默认，不封装，也就是不执行下面 beforeBodyWrite 方法

        // 若接口返回的类型是ResponseVO，直接返回false，不再包装
        return !methodParameter.getGenericParameterType().equals(ResponseVO.class);
    }

    // 封装方法返回数据
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // return null; // 默认，不封装，此方法不会被调用

        // 将返回的String数据包装在ResponseVO对象中
        if (methodParameter.getGenericParameterType().equals(String.class)) {
            return stringToObject(body, methodParameter);
        }
        // 将返回的数据包装在ResponseVO对象中
        return ResponseUtil.success(body);
    }

    // String类型不能直接包装，所以要进行些特别的处理
    private String stringToObject(Object body, MethodParameter methodParameter) {
        if (methodParameter.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在ResponseVO里后，再转换为json字符串响应给前端
                return objectMapper.writeValueAsString(ResponseUtil.success(body));
            } catch (JsonProcessingException e) {
                log.error("stringToObject 异常:", e);
            }
        }

        return null;
    }

}
