package com.zja.jdk.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author: zhengja
 * @Date: 2025-03-24 16:59
 */
public class BigDecimalUtils {

    /**
     * 将平方米转换为公顷，并保留指定的小数位数
     *
     * @param squareMeters 平方米值（字符串形式，避免精度丢失）
     * @param scale        保留的小数位数
     * @return 转换后的公顷值（BigDecimal）
     */
    public static BigDecimal convertSquareMetersToHectares(String squareMeters, int scale) {
        if (squareMeters == null || squareMeters.isEmpty()) {
            throw new IllegalArgumentException("平方米值不能为空");
        }

        BigDecimal number = new BigDecimal(squareMeters);
        return convertSquareMetersToHectares(number, scale);
    }

    /**
     * 将平方米转换为公顷，并保留指定的小数位数
     *
     * @param squareMeters 平方米值（BigDecimal形式，避免精度丢失）
     * @param scale        保留的小数位数
     * @return 转换后的公顷值（BigDecimal）
     */
    public static BigDecimal convertSquareMetersToHectares(BigDecimal squareMeters, int scale) {
        if (squareMeters == null) {
            return null;
        }
        if (scale < 0) {
            throw new IllegalArgumentException("小数位数不能为负数");
        }

        BigDecimal hectares = squareMeters.divide(new BigDecimal("10000"), scale, RoundingMode.HALF_UP);
        return hectares;
    }
}
