package com.zja.webexception.service;

import com.zja.webexception.exception.BusinessException;
import com.zja.webexception.exception.DataException;
import com.zja.webexception.exception.TestException;
import com.zja.webexception.exception.TestRuntimeException;
import com.zja.webexception.model.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;

import static com.zja.webexception.model.response.ResponseCode.USER_NOT_LOGIN;

/**
 * 异常服务：：异常抛出模拟
 *
 * @author: zhengja
 * @since: 2024/02/01 9:51
 */
@Slf4j
@Service
public class ExceptionService {

    // 校验数据
    public String getUserName(String name) {
        if (name.length() < 2) {
            throw new DataException("name length < 2.");
        }
        return name;
    }

    // 登录验证
    public void loginVerify() {
        throw new BusinessException(USER_NOT_LOGIN);
    }


    // 测试：继承 Exception 抛出异常，需要手动抛出异常
    public void testException() throws TestException {
        throw new TestException();
    }

    // 测试：继承 RuntimeException 抛出异常，不需要手动抛出异常
    public void testRuntimeException() {
        throw new TestRuntimeException();
    }

    // 异常日志记录：默认异常是不会输出到日志文件中
    public void testRuntimeExceptionLog() {
        try {
            throw new TestRuntimeException();
        } catch (Exception e) {
            log.error("异常输出日志1：", e); // 等价于6:e.fillInStackTrace()
            log.error("异常输出日志2：{}", e.getMessage()); // 等价于5：e.getLocalizedMessage()
            log.error("异常输出日志3：{}", e.toString()); // 无太多意义，内容：com.zja.webexception.exception.TestRuntimeException: 测试：继承 RuntimeException 抛出异常
            log.error("异常输出日志4：{}", Arrays.toString(e.getStackTrace())); //无太多意义
            log.error("异常输出日志5：{}", e.getLocalizedMessage());
            log.error("异常输出日志6：", e.fillInStackTrace());
        }
    }

}
