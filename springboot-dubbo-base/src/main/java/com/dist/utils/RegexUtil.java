package com.dist.utils;

import java.util.regex.Pattern;

/**
 * @author zhangjp
 * @date 2019/3/8 11:29
 */
public class RegexUtil {

    /**
     * 验证字符串是否为数字
     * @param str
     * @return
     */
    public static Boolean isNumber(String str){
        Pattern pattern = Pattern.compile("^(\\-)?(((([1-9]\\d*)|0){1}(\\.(\\d+))?)|" +
                "([1-9]\\d*)){1}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 验证字符串是否为年份
     * @param str
     * @return
     */
    public static Boolean isYear(String str){
        Pattern pattern = Pattern.compile("^[1-9]{1}[0-9]{0,3}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 验证字符串是否为区间（包含无穷大）
     * @param str
     * @return
     */
    public static Boolean isInterval(String str){
        Pattern pattern = Pattern.compile("^(\\[|\\(){1}(\\-)?(((([1-9]\\d*)|0){1}(\\.(\\d+))?)|([1-9]\\d*)|(\\∞))" +
                "{1}(\\,){1}((\\-)?(((([1-9]\\d*)|0){1}(\\.(\\d+))?)|([1-9]\\d*))|(\\+\\∞)){1}(\\]|\\)){1}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 验证字符串是否为比例，字符串1:2,或者1:2:3，比例中只支持正整数的比例
     * @param str
     * @return
     */
    public static Boolean isProportion(String str){
        Pattern pattern = Pattern.compile("^[1-9]{1}(\\d*)(\\:){1}[1-9]{1}(\\d*)$|" +
                "^[1-9]{1}(\\d*)(\\:){1}[1-9]{1}(\\d*)(\\:){1}[1-9]{1}(\\d*)$");
        return pattern.matcher(str).matches();
    }

    /**
     * 同时验证是否为数字、区间和比例
     * @param str
     * @return
     */
    public static Boolean isNumberOrIntervalOrProportion(String str){
        Pattern pattern = Pattern.compile("(^(\\[|\\(){1}(\\-)?(((([1-9]\\d*)|0){1}(\\.(\\d+))?)|([1-9]\\d*)|" +
                "(\\∞)){1}(\\,){1}((\\-)?(((([1-9]\\d*)|" +
                "0){1}(\\.(\\d+))?)|([1-9]\\d*))|(\\+\\∞)){1}(\\]|\\)){1}$)|(^(\\-)?(((([1-9]\\d*)|" +
                "0){1}(\\.(\\d+))?)|([1-9]\\d*)){1}$)|(^[1-9]{1}(\\d*)(\\:){1}[1-9]{1}(\\d*)$)|" +
                "(^[1-9]{1}(\\d*)(\\:){1}[1-9]{1}(\\d*)(\\:){1}[1-9]{1}(\\d*)$)");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否是字母和数字的字符串
     * @param rawString
     * @return
     */
    public static Boolean isAlphabeticString(String rawString){
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]*");
        return pattern.matcher(rawString).matches();
    }

}