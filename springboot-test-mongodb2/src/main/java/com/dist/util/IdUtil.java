package com.dist.util;

import java.util.UUID;

/**
 * uuid工具
 */
public class IdUtil {

    /**
     * 36位uuid
     * @return
     */
    public static String getGlobalId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 32位uuid
     * @return
     */
    public static String uuid32() {
        return getGlobalId().replace("-","");
    }

    /**
     * 随机产生6位数字
     * @return
     */
    public static String uuid6() {
        int randNum = 1 + (int)(Math.random() * ((999999 - 1) + 1));
        return String.valueOf(randNum);
    }
}
