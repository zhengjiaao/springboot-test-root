// package com.zja.hanbian.封装.数据结构;
//
// import java.util.stream.IntStream;
//
// /**
//  * @Author: zhengja
//  * @Date: 2024-09-29 10:05
//  */
// public class MyString implements java.io.Serializable, Comparable<String>, CharSequence {
//     private String data;
//
//     public MyString() {
//         this.data = "";
//     }
//
//     // 构造函数：初始化字符串
//     public MyString(String s) {
//         this.data = s;
//     }
//
//     public MyString(char value[]) {
//         this.data = new String(value);
//     }
//
//     public String toString() {
//         return data;
//     }
//
//     public boolean equals(Object obj) {
//         return data.equals(obj);
//     }
//
//     @Override
//     public int compareTo(String o) {
//         return data.compareTo(o);
//     }
//
//     @Override
//     public int length() {
//         return data.length();
//     }
//
//     @Override
//     public char charAt(int index) {
//         return data.charAt(index);
//     }
//
//     @Override
//     public CharSequence subSequence(int start, int end) {
//         return data.subSequence(start, end);
//     }
//
//     @Override
//     public IntStream chars() {
//         return data.chars();
//     }
//
//     @Override
//     public IntStream codePoints() {
//         return data.codePoints();
//     }
//
//     // 主函数，用于测试
//     public static void main(String[] args) {
//         MyString myStr = new MyString("Hello");
//         System.out.println("Length: " + myStr.length()); // 输出长度
//         System.out.println("Char at 1: " + myStr.charAt(1)); // 输出第2个字符
//         // MyString newStr = myStr.concat(" World");
//         // System.out.println("Concatenated: " + newStr.toString()); // 输出拼接后的字符串
//
//         // 测试 equals 和 hashCode
//         MyString myStr2 = new MyString("Hello");
//         System.out.println("Equals: " + myStr.equals(myStr2)); // 应该输出 true
//         System.out.println("Hash codes match: " + (myStr.hashCode() == myStr2.hashCode())); // 应该输出 true
//     }
//
//     public static 字符串 创建字符串(String s) {
//         return new 字符串(s);
//     }
//
//
// }
