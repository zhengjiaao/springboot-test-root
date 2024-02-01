package com.zja.webexception.model.response.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zja.webexception.exception.BusinessException;
import com.zja.webexception.model.response.ResponseUtil;
import com.zja.webexception.model.response.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author: zhengja
 * @since: 2024/02/01 10:19
 * Desc：统一返回数据格式：Controller返回数据统一包装为 ResponseVO
 */
@Slf4j
@RestControllerAdvice(basePackages = {"com.zja.webexception.controller"})
public class ResponseBodyAdviceHandler implements ResponseBodyAdvice<Object> {

    /**
     * 判断controller中方法返回值类型
     *
     * @param returnType    returnType方法返回值类型
     * @param converterType 转换器类型
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果接口返回的类型本身就是ResultVO那就没有必要进行额外的操作，返回false
        return !returnType.getGenericParameterType().equals(ResponseVO.class);
    }

    /**
     * 封装方法返回数据
     *
     * @param body                  data 返回数据
     * @param returnType            returnType方法返回值类型
     * @param selectedContentType   媒体类型
     * @param selectedConverterType 选择的转换器类型
     * @param request               服务器请求
     * @param response              服务器响应
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // String类型不能直接包装，所以要进行些特别的处理
        if (returnType.getGenericParameterType().equals(String.class)) {
            return stringToJson(body, response);
        }
        // 将数据包装在ResponseVO里,返回给前端
        return ResponseUtil.success(body);
    }

    private Object stringToJson(Object body, ServerHttpResponse serverHttpResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 把string数据封装到vo类中，并转为json格式，同时，修改Response的Content-Type为application/json
            String value = objectMapper.writeValueAsString(ResponseUtil.success(body));
            serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON); // String默认返回类型：content-type: text/plain;charset=UTF-8
            return value;
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException", e);
            throw new BusinessException();
        }
    }

}
