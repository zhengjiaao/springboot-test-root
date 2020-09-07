package com.dist.utils;

import java.util.List;

/**
 * 数组工具
 */
public abstract class ArrayUtil {

    /**
     * 数组为null
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
     * 数组为空
     * @param o
     * @return
     */
    public static final boolean isEmpty(Object[] o) {
        if (o.length > 0) {
            return false;
        }
        return true;
    }

    /**
     * 数组不为null也不为空
     * @param o
     * @return
     */
    public static final boolean isNonNullAndNonEmpty(Object[] o) {
        if (isNull(o)) {
            return false;
        }
        if (isEmpty(o)) {
            return false;
        }
        return true;
    }

    /**
     * 集合不为null也不为空
     * @param o
     * @return
     */
    public static final boolean isNonNullAndNonEmpty(List o) {
        if (o != null && o.size() > 0){
            return true;
        }
        return false;
    }


    /**
     * 数组为null或为空
     * @param o
     * @return
     */
    public static final boolean isNullOrEmpty(Object[] o) {
        if (isEmpty(o)) {
            return true;
        }
        if (isNull(o)) {
            return true;
        }
        return false;
    }
}
