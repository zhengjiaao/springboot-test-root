package com.zja.mvc.exception.exception.handler;

import com.zja.mvc.exception.exception.BusinessException;
import com.zja.mvc.exception.model.response.ResponseCode;
import com.zja.mvc.exception.model.response.ResponseUtil;
import com.zja.mvc.exception.model.response.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 全局异常统一处理类
 *
 * @author: zhengja
 * @since: 2024/02/01 9:55
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 自定义异常 BusinessException
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseVO BusinessExceptionHandler(BusinessException ex) {
        // 保证异常被输出到日志文件
        log.error("业务异常：" + ex.getMsg(), ex);
        // 提取错误信息返回给前端
        return ResponseUtil.fail(ex.getCode(), ex.getMsg());
    }

    /**
     * 方法参数错误异常 BindException
     * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常，详情继续往下看代码
     *
     * @param ex
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseVO BindExceptionHandler(BindException ex) {
        // 保证异常被输出到日志文件
        log.error("BindException 方法参数错误异常", ex);
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> map = new HashMap<>();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (!CollectionUtils.isEmpty(fieldErrors)) {
            for (FieldError fieldError : fieldErrors) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            log.error("参数检验失败 {}", map.toString());
        }
        // 提取错误信息返回给前端
        return ResponseUtil.paramError(map);
    }

    /**
     * 方法参数错误异常 MethodArgumentNotValidException
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常
     *
     * @param ex
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseVO MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        // 保证异常被输出到日志文件
        log.error("MethodArgumentNotValidException 方法参数错误异常", ex);
        Map<String, String> map = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        if (!CollectionUtils.isEmpty(fieldErrors)) {
            for (FieldError fieldError : fieldErrors) {
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            log.error("参数检验失败 {}", map.toString());
        }
        // 提取错误信息返回给前端
        return ResponseUtil.paramError(map);
    }

    /**
     * 方法参数错误异常 ConstraintViolationException
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     *
     * @param ex
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseVO ConstraintViolationExceptionHandler(ConstraintViolationException ex) {
        // 保证异常被输出到日志文件
        log.error("ConstraintViolationException 方法参数错误异常", ex);
        Map<String, String> map = new HashMap<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        boolean hasError = violations != null && violations.size() > 0;
        if (hasError) {
            for (ConstraintViolation<?> violation : violations) {
                map.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            log.error("参数检验失败 {}", map.toString());
        }
        // 提取错误信息返回给前端
        return ResponseUtil.paramError(map);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseVO MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        // 保证异常被输出到日志文件
        log.error("MissingServletRequestParameterExceptionHandler 方法参数错误异常", ex);
        Map<String, String> map = new HashMap<>();
        map.put(ex.getParameterName(), "必需的参数[ " + ex.getParameterName() + " ]不存在,参数类型为 [ " + ex.getParameterType() + " ]!");
        log.error("参数检验失败 {}", map.toString());
        // 提取错误信息返回给前端
        return ResponseUtil.paramError(map);
    }

    /**
     * 运行时异常
     *
     * @param ex
     */
    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public ResponseVO runtimeExceptionHandler(RuntimeException ex) {
        // 保证异常被输出到日志文件
        log.error("RuntimeException detail：", ex);
        if (ex.getMessage() != null) {
            return ResponseUtil.fail(ex.getMessage(), exceptionDetail(ex));
        }
        // 提取错误信息返回给前端
        return ResponseUtil.fail(ResponseCode.SYS_RUN_ERROR.getMsg(), exceptionDetail(ex));
    }

    /**
     * 系统异常
     *
     * @param ex
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseVO exceptionHandler(Exception ex) {
        // 保证异常被输出到日志文件
        log.error("Exception detail：", ex);
        if (ex.getMessage() != null) {
            return ResponseUtil.error(ex.getMessage(), exceptionDetail(ex));
        }
        // 提取错误信息返回给前端
        return ResponseUtil.error(ResponseCode.ERROR.getMsg(), exceptionDetail(ex));
    }

    /**
     * 返回给前端的异常详情
     *
     * @param ex
     */
    private String exceptionDetail(Exception ex) {
        StackTraceElement stackTraceElement = ex.getStackTrace()[0];
        StringBuilder detail = new StringBuilder();
        String exclass = stackTraceElement.getClassName();
        String method = stackTraceElement.getMethodName();
        detail.append("类[");
        detail.append(exclass);
        detail.append("]调用[");
        detail.append(method);
        detail.append("]方法时在第[");
        detail.append(stackTraceElement.getLineNumber());
        detail.append("]行代码处发生[");
        detail.append(ex.getClass().getName());
        detail.append("]异常!");
        // TODO 自选是否输出堆栈信息给前端
//        detail.append(">>");
//        detail.append("异常堆栈信息：");
//        for (StackTraceElement stack : ex.getStackTrace()){
//            detail.append(stack);
//            detail.append(">>");
//        }
        return detail.toString();
    }
}
