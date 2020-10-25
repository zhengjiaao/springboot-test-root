package com.zja.exception.global;//package com.dist.xdata.onemap.application.global;
//
//import com.dist.xdata.onemap.api.utils.ListUtil;
//import com.dist.xdata.starter.common.defines.ResponseData;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.BindException;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.MissingServletRequestParameterException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import javax.validation.ConstraintViolationException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Company: 上海数慧系统技术有限公司
// * Department: 数据中心
// * Date: 2020-09-16 15:03
// * Author: zhengja
// * Email: zhengja@dist.com.cn
// * Desc：全局异常统一处理类
// */
//@RestControllerAdvice
//@Slf4j
//public class GlobalExceptionHandler {
//
//
//    /**
//     * 方法参数错误异常 BindException
//     * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常，详情继续往下看代码
//     *
//     * @param ex
//     */
//    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseData methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
//        log.error("ValidException 参数异常", ex);
//        Map<String, String> map = new HashMap<>();
//        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
//        if (ListUtil.isNotNull(fieldErrors)) {
//            for (FieldError fieldError : fieldErrors) {
//                map.put(fieldError.getField(), fieldError.getDefaultMessage());
//            }
//            log.error("参数检验失败 {}", map.toString());
//        }
//
//        return ResponseData.fail(new ValidateException(map.toString()));
//    }
//
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ResponseData MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
//        log.error("RequestException 请求异常");
//        Map<String, String> map = new HashMap<>();
//        map.put(ex.getParameterName(), "必需的参数[ " + ex.getParameterName() + " ]不存在,参数类型为 [ " + ex.getParameterType() + " ]!");
//        log.error("参数检验失败 {}", map.toString());
//        return ResponseData.fail(new ValidateException(map.toString()));
//    }
//
//}
