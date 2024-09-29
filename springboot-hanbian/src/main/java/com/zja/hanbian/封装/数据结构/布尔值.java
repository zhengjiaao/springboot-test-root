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

    public 布尔值() {
        this.value = false;
    }

    public 布尔值(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}