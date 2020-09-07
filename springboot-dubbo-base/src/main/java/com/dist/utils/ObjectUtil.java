package com.dist.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * object对象工具
 */
public abstract  class ObjectUtil {

    /**
     * 对象为空指针null
     * @param o
     * @return
     */
    public static final boolean isNull(Object o) {
        if (null == o) {
            return true;
        }
        return false;
    }

    /**
     * 对象不为空指针null
     * @param o
     * @return
     */
    public static final boolean isNonNull(Object o) {
        return !isNull(o);
    }


    /**
     * 传入的对象都不为null
     *
     * @param args
     * @return
     */
    public static boolean isNonNull(Object... args) {

        for (Object arg : args) {
            if (Objects.isNull(arg)) {
                return false;
            }
            if (arg instanceof CharSequence) {
                if (StringUtils.isBlank((CharSequence) arg)) {
                    return false;
                }
            }
            if (arg instanceof Collection) {
                if (((Collection) arg).size() == 0) {
                    return false;
                }
            }

        }

        return true;
    }

    /**
     * 有一个为空，就返回true
     *
     * @param args
     * @return
     */
    public static boolean isAnyNull(Object... args) {
        for (Object arg : args) {
            if (!isNonNull(arg)) {
                return true;
            }
        }

        return false;
    }


}
