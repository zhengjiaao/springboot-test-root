package com.zja.hanbian.封装.数据结构;

/**
 * @Author: zhengja
 * @Date: 2024-09-29 11:13
 */
public class 布尔值 {
    private boolean value;

    public static 布尔值 属于(boolean value) {
        return new 布尔值(value);
    }

    public static 布尔值 属于(String value) {
        return new 布尔值(value.equals("真") ? true : false);
    }

    public 布尔值(boolean value) {
        this.value = value;
    }

    public boolean 值() {
        return value;
    }

    public 布尔值 逻辑与(布尔值 other) {
        return new 布尔值(this.value && other.value);
    }

    // 重写 toString 方法
    @Override
    public String toString() {
        return Boolean.toString(this.value);
    }

}