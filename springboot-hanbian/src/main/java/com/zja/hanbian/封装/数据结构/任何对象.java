package com.zja.hanbian.封装.数据结构;

/**
 * @Author: zhengja
 * @Date: 2024-09-29 13:03
 */
public class 任何对象 {

    private Object value;

    public 任何对象(Object value) {
        this.value = value;
    }

    public static <T> 任何对象 属于(T value) {
        return new 任何对象(value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    // 重写 hashCode 方法
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof 任何对象) {
            return this.value.equals(((任何对象) obj).getValue());
        }
        return this.value.equals(obj);
    }
}
