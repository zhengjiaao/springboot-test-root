package com.dist.utils;

import java.text.DecimalFormat;

/**
 * @desc 一些用于数字计算的工具类
 * @author dingchw
 * @date 2019/3/6.
 */
public class MathUtil {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat( "###############0.################# ");
    /**
     * 科学计数法转换成正常显示的数值，此处就直接保留字符串，
     * 再转回去又变成科学记数法了
     * @param inputData
     * @return
     */
    public static String scientificNotation2NormalData(Double inputData){

        return DECIMAL_FORMAT.format(inputData);
    }

    /**
     * 除法
     * @param a
     * @param b
     * @return
     */
    public static String divide(Double a, Double b){
        return DECIMAL_FORMAT.format(a/b);
    }

    public static String devide(Integer a, Integer b){
        return DECIMAL_FORMAT.format((double)a/b);
    }

    public static String devide(Long a, Long b){
        return DECIMAL_FORMAT.format((double)a/b);
    }
}
