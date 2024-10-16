package com.zja.apache.lang3;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhengja
 * @Date: 2024-10-16 9:52
 */
public class ExceptionUtilsTest {
    public static void main(String[] args) {
        // 获取异常信息
        String message = "This is a test exception";
        Exception exception = new Exception(message);
        String exceptionMessage = ExceptionUtils.getMessage(exception);
        System.out.println("Exception message: " + exceptionMessage);

        // 获取异常堆栈信息
        String stackTrace = ExceptionUtils.getStackTrace(exception);
        System.out.println("Stack trace: " + stackTrace);

        // 获取异常 cause
        Exception causeException = new Exception("Cause exception");
        exception.initCause(causeException);
    }

    // String getStackTrace(final Throwable throwable): 以字符串形式从可丢弃对象获取堆栈跟踪信息
    @Test
    public void testGetStackTrace() {
        try {
            System.out.println(1 / 0);
        } catch (Exception e) {
            // e.printStackTrace();
            String stackTrace = ExceptionUtils.getStackTrace(e);
            System.out.println(stackTrace);
        }
        try {
            int[] ints = {1, 2, 3};
            System.out.println(ints[8]);
        } catch (Exception e) {
            // e.getMessage();
            String stackTrace = ExceptionUtils.getStackTrace(e);
            System.out.println(stackTrace);
        }
    }
}
