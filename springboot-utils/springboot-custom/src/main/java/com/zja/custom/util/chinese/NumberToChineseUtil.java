package com.zja.custom.util.chinese;

import java.util.Stack;

/**
 * 数值转中文
 *
 * @Author: zhengja
 * @Date: 2024-11-26 10:43
 */
public class NumberToChineseUtil {

    private NumberToChineseUtil() {
    }

    /**
     * 将数字转换为中文数字字符串，例如 10 转换为 "一十"
     *
     * @param number 要转换的数字
     * @return 中文数字字符串
     */
    public static String convert(long number) {
        if (number < 0) {
            throw new IllegalArgumentException("输入必须是非负整数");
        }
        String[] convert = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] unit = {"个", "十", "百", "千", "万", "亿"};
        if (number == 0) return convert[0]; // 0 比较特殊
        // 将数字分为4个一组处理
        Stack<String> stack = new Stack<>();
        StringBuilder sb = new StringBuilder();
        int pre = -1; // 上一个数字
        int count = 0; // 记录当前处理了几个数字
        try {
            while (number > 0) {
                int digit = (int) (number % 10);
                number = number / 10;
                if (digit > 0 || (digit == 0 && pre != 0 && count > 0)) { // 避免有多个零出现
                    // 添加单位,首先要满足不是0
                    if (digit > 0 && count % 4 == 1) sb.append(unit[1]);
                    if (digit > 0 && count % 4 == 2) sb.append(unit[2]);
                    if (digit > 0 && count % 4 == 3) sb.append(unit[3]);
                    sb.append(convert[digit]);
                }
                pre = digit;
                count++;
                if (count == 4 || count == 8 || number <= 0) { // 如果处理满一组或不满一组，但已经处理完
                    String s = sb.reverse().toString();
                    if (s.startsWith("一十")) s = s.substring(1); // 特殊情况: 10 不念一十 ，念十
                    stack.push(s);
                    count = 0; // 下一组重新初始化
                    pre = -1;
                    sb.setLength(0);
                }
            }
            sb.setLength(0); // 清空
            // 对栈中的数据进行处理
            while (!stack.isEmpty()) {
                sb.append(stack.pop());
                if (stack.size() == 2) { // 为亿
                    sb.append(unit[5]);
                } else if (stack.size() == 1 && sb.charAt(sb.length() - 1) != '亿') { // 为万，特殊情况 一亿，后面不能加万了
                    sb.append(unit[4]);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("处理数字时发生错误", e);
        }
        return sb.toString();
    }

    /**
     * 将数字转换为中文数字字符串，例如 10 转换为 "一零"
     *
     * @param number 要转换的数字
     * @return 中文数字字符串
     */
    public static String convertToChineseNumber(int number) {
        String[] CHINESE_NUMBERS = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};

        if (number == 0) {
            return CHINESE_NUMBERS[0];
        }

        StringBuilder chineseNumber = new StringBuilder();
        while (number > 0) {
            int digit = number % 10;
            chineseNumber.insert(0, CHINESE_NUMBERS[digit]);
            number /= 10;
        }
        return chineseNumber.toString();
    }


    public static void main(String[] args) {
        int number = 0;
        String chineseNumber = NumberToChineseUtil.convert(number);
        System.out.println("数字 " + number + " 转换为中文字符: " + chineseNumber);

        number = 1;
        chineseNumber = NumberToChineseUtil.convert(number);
        System.out.println("数字 " + number + " 转换为中文字符: " + chineseNumber);

        number = 10;
        chineseNumber = NumberToChineseUtil.convert(number);
        System.out.println("数字 " + number + " 转换为中文字符: " + chineseNumber);

        number = 11;
        chineseNumber = NumberToChineseUtil.convert(number);
        System.out.println("数字 " + number + " 转换为中文字符: " + chineseNumber);

        number = 19;
        chineseNumber = NumberToChineseUtil.convert(number);
        System.out.println("数字 " + number + " 转换为中文字符: " + chineseNumber);

        number = 20;
        chineseNumber = NumberToChineseUtil.convert(number);
        System.out.println("数字 " + number + " 转换为中文字符: " + chineseNumber);

        number = 21;
        chineseNumber = NumberToChineseUtil.convert(number);
        System.out.println("数字 " + number + " 转换为中文字符: " + chineseNumber);

        number = 100;
        chineseNumber = NumberToChineseUtil.convert(number);
        System.out.println("数字 " + number + " 转换为中文字符: " + chineseNumber);

        number = 101;
        chineseNumber = NumberToChineseUtil.convert(number);
        System.out.println("数字 " + number + " 转换为中文字符: " + chineseNumber);

        number = 111;
        chineseNumber = NumberToChineseUtil.convert(number);
        System.out.println("数字 " + number + " 转换为中文字符: " + chineseNumber);

        // 数字 10 转换为中文字符: 十
        // 数字 11 转换为中文字符: 十一
        // 数字 19 转换为中文字符: 十九
        // 数字 20 转换为中文字符: 二十
        // 数字 21 转换为中文字符: 二十一
        // 数字 100 转换为中文字符: 一百

        String chineseNumber2 = NumberToChineseUtil.convertToChineseNumber(number);
        System.out.println("数字 " + number + " 转换为中文字符: " + chineseNumber2);
        // 数字 10 转换为中文字符: 一零
        // 数字 11 转换为中文字符: 一一
        // 数字 19 转换为中文字符: 一九
        // 数字 20 转换为中文字符: 二零
        // 数字 21 转换为中文字符: 二一
        // 数字 100 转换为中文字符: 一零零


        // 阿拉伯数字转中文数字
        System.out.println(convert(1));
        System.out.println(convert(10));
        System.out.println(convert(11));
        System.out.println(convert(15));
        System.out.println(convert(19));
        System.out.println(convert(20));
        System.out.println(convert(25));
        System.out.println(convert(100));
        System.out.println(convert(101));
        System.out.println(convert(115));
        System.out.println(convert(234));
        System.out.println(convert(999));
        System.out.println(convert(1501));
        System.out.println(convert(1006));
        System.out.println(convert(9905));
        System.out.println(convert(20394));
        System.out.println(convert(100001));
        System.out.println(convert(100000));
        System.out.println(convert(100101822));
        System.out.println(convert(123412341234L));
        System.out.println(convert(10000000000L));
    }

}
