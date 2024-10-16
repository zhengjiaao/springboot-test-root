package com.zja.apache.lang3;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;

/**
 * SystemUtils： 获取系统属性的工具类，如操作系统名称、Java版本等。
 *
 * @Author: zhengja
 * @Date: 2024-10-16 9:37
 */
public class SystemUtilsTest {

    @Test
    public void _test1() {
        String javaVersion = SystemUtils.JAVA_VERSION; // 获取Java运行时的版本。
        String userName = SystemUtils.USER_NAME; // 获取当前用户的用户名。
        String osName = SystemUtils.OS_NAME; // 获取当前操作系统的名称。
        System.out.println("javaVersion = " + javaVersion);
        System.out.println("userName = " + userName);
        System.out.println("osName = " + osName);
        System.out.println("SystemUtils.IS_OS_WINDOWS = " + SystemUtils.IS_OS_WINDOWS);
        System.out.println("SystemUtils.IS_OS_LINUX = " + SystemUtils.IS_OS_LINUX);
        System.out.println("SystemUtils.IS_OS_MAC = " + SystemUtils.IS_OS_MAC);
    }

}
