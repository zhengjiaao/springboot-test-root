package com.zja.hanbian.封装.数据结构;

/**
 * @Author: zhengja
 * @Date: 2024-09-29 10:26
 */
public class 字符串 {

    private String value;

    // 构造函数：初始化字符串
    public 字符串(String s) {
        this.value = s;
    }

    public String 值() {
        return this.value;
    }

    public static boolean 相等(Object o1, Object o2) {
        return o1.equals(o2);
    }

    public static 字符串 属于(String s) {
        return new 字符串(s);
    }

    public int 长度() {
        return this.value.length();
    }

    public 字符串 拼接(Object o) {
        return new 字符串(this.value + o);
    }

    public boolean 包含(Object o) {
        return this.value.contains(o.toString());
    }

    // 重写 toString 方法
    @Override
    public String toString() {
        return this.value;
    }

    // 重写 hashCode 方法
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    // 重写 equals 方法
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        字符串 other = (字符串) obj;
        return this.value.equals(other.value);
    }

    public static void main(String[] args) {
        字符串 s = new 字符串("Hello, World!");
        字符串 s2 = new 字符串("Hello, World!");

        System.out.println(s); // 输出 s 的字符串表示形式
        System.out.println(s.hashCode()); // 输出 s 的哈希值

        if (s.equals(s2)) {
            System.out.println("相等"); // 检查 s 和 s2 是否相等
        }
        if (s == s2) {
            System.out.println("引用相等"); // 检查 s 和 s2 是否引用相等
        }
        if (s.hashCode() == s2.hashCode()) {
            System.out.println("哈希值相等"); // 检查 s 和 s2 的哈希值是否相等
        }
    }
}
