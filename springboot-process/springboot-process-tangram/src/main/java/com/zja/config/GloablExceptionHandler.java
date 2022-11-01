/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-20 17:12
 * @Since:
 */
package com.zja.config;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class GloablExceptionHandler {

    @ExceptionHandler({FeignException.class})
    @ResponseBody
    public ResponseData feignExceptionHandler(FeignException ex) {
        log.error("FeignException detail：", ex);
        if (ex instanceof FeignException.FeignClientException) {
            return ResponseUtil.fail("FeignClientException 客户端异常: " + ex.getMessage(), exceptionDetail(ex));
        }
        if (ex instanceof FeignException.FeignServerException) {
            return ResponseUtil.fail("FeignServerException 服务端异常: " + ex.getMessage(), exceptionDetail(ex));
        }
        return ResponseUtil.fail("FeignException 调用异常: " + ex.getMessage(), exceptionDetail(ex));
    }

    /**
     * 运行时异常
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler({RuntimeException.class})
    @ResponseBody
    public ResponseData runtimeExceptionHandler(RuntimeException ex) {
        log.error("RuntimeException detail：", ex);
        if (ex.getMessage() != null) {
            return ResponseUtil.error(ex.getMessage(), exceptionDetail(ex));
        }
        return ResponseUtil.error("系统运行错误,请求失败", exceptionDetail(ex));
    }


    /**
     * 系统异常
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseData exceptionHandler(Exception ex) {
        log.error("Exception detail：", ex);
        if (ex.getMessage() != null) {
            return ResponseUtil.error(ex.getMessage(), exceptionDetail(ex));
        }
        return ResponseUtil.error("系统内部错误,请求失败", exceptionDetail(ex));
    }

    /**
     * 异常详情
     * @param e
     * @return
     */
    private String exceptionDetail(Exception e) {
        StackTraceElement stackTraceElement = e.getStackTrace()[0];
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
        detail.append(e.getClass().getName());
        detail.append("]异常!");
        // TODO 自选是否输出堆栈信息给前端
//        detail.append(">>");
//        detail.append("异常堆栈信息：");
//        for (StackTraceElement stack : e.getStackTrace()){
//            detail.append(stack);
//            detail.append(">>");
//        }
        return detail.toString();
    }

}
