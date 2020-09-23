package com.zja;

import com.zja.exception.APIException;
import com.zja.result.ResultUtil;
import com.zja.result.ResultVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-16 15:04
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：全局处理增强版Controller，避免Controller里返回数据每次都要用响应体来包装
 */
@RestControllerAdvice(basePackages = {"com.zja.controller"})  //扫描的包
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {

    /**
     * @param methodParameter returnType
     * @param aClass
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果接口返回的类型本身就是ResultVO那就没有必要进行额外的操作，返回false
        return !methodParameter.getGenericParameterType().equals(ResultVO.class);
    }

    /**
     * @param o                  data
     * @param methodParameter    returnType
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // String类型不能直接包装，所以要进行些特别的处理
        if (methodParameter.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在ResultVO里后，再转换为json字符串响应给前端
                return objectMapper.writeValueAsString(ResultUtil.success(o));
            } catch (JsonProcessingException e) {
                throw new APIException();
            }
        }
        // 将原本的数据包装在ResultVO里
        return ResultUtil.success(o);
    }

}
