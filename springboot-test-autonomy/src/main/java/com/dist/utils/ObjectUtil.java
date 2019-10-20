package com.dist.utils;

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



}
