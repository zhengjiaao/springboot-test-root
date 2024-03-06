package com.zja.log.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author: zhengja
 * @since: 2024/01/23 15:38
 */
@Slf4j
@Service
public class UserService {

    // 测试显示级别
    public void add() {
        log.trace("log=trace");

        log.debug("log=debug");

        log.info("log=info");

        log.warn("log=warn");

        log.error("log=error");
    }

    // 测试异常日志输出：默认异常是不会输出到日志文件中
    public void exceptionLog() {
        try {
            throw new TestRuntimeException();
        } catch (Exception e) {
            // 等价于6:e.fillInStackTrace()
            log.error("异常输出日志1：", e);  // 输出全部异常详情
            // 等价于5：e.getLocalizedMessage()
            log.error("异常输出日志2：{}", e.getMessage()); // 输出异常信息

            log.error("异常输出日志3：{}", e.toString()); // 无太多意义，内容：com.zja.webexception.exception.TestRuntimeException: 测试：继承 RuntimeException 抛出异常
            log.error("异常输出日志4：{}", Arrays.toString(e.getStackTrace())); // 无太多意义
            log.error("异常输出日志5：{}", e.getLocalizedMessage());
            log.error("异常输出日志6：", e.fillInStackTrace());
        }
    }

    // 异常
    static class TestRuntimeException extends RuntimeException {

        private static final String EXCEPTION_MSG = "测试：继承 RuntimeException 抛出异常";

        public TestRuntimeException() {
            super(EXCEPTION_MSG);
        }

        public TestRuntimeException(String msg) {
            super(msg);
        }
    }
}
