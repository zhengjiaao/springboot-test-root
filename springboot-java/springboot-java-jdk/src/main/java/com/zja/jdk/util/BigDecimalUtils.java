package com.zja.jdk.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 面积单位转换工具类
 * <p>
 * 提供将平方米（square meters）转换为公顷（hectares）、亩等常见面积单位的方法，
 * 所有计算均基于 BigDecimal 实现，保证高精度运算。
 * 支持多种输入类型（String、BigDecimal、double、long），并提供格式化输出功能。
 *
 * @Author: zhengja
 * @Date: 2025-03-24 16:59
 */
public class BigDecimalUtils {

    /**
     * 默认保留的小数位数
     */
    private static final int DEFAULT_SCALE = 2;

    /**
     * 1 公顷 = 10000 平方米
     */
    private static final BigDecimal SQUARE_METERS_PER_HECTARE = new BigDecimal("10000");

    /**
     * 1 亩 ≈ 666.666667 平方米
     */
    private static final BigDecimal SQUARE_METERS_PER_MU = new BigDecimal("666.666667");

    /**
     * 判断 BigDecimal 是否为 null 或等于零
     * <p>
     * 示例：
     * isNullOrZero(null) → true
     * isNullOrZero(BigDecimal.ZERO) → true
     * isNullOrZero(new BigDecimal("0")) → true
     * isNullOrZero(new BigDecimal("100")) → false
     *
     * @param value 要判断的 BigDecimal 值
     * @return 如果为 null 或等于零则返回 true，否则返回 false
     */
    public static boolean isNullOrZero(BigDecimal value) {
        return value == null || BigDecimal.ZERO.compareTo(value) == 0;
    }

    // ===== 平方米 → 公顷 =====

    /**
     * 将字符串表示的平方米值转换为公顷，保留指定小数位数
     * <p>
     * 示例：
     * convertSquareMetersToHectares("12345.6789", 2) → 1.23 公顷
     *
     * @param squareMeters 平方米值（字符串形式）
     * @param scale        保留的小数位数
     * @return 转换后的公顷值（BigDecimal），若输入为空抛出异常
     * @throws IllegalArgumentException 若输入为空或无效数字
     */
    public static BigDecimal convertSquareMetersToHectares(String squareMeters, int scale) {
        if (squareMeters == null || squareMeters.isEmpty()) {
            throw new IllegalArgumentException("平方米值不能为空");
        }
        return convertSquareMetersToHectares(new BigDecimal(squareMeters), scale);
    }

    /**
     * 使用默认精度（2位）将字符串表示的平方米值转换为公顷
     * <p>
     * 示例：
     * convertSquareMetersToHectares("12345") → 1.23 公顷
     *
     * @param squareMeters 平方米值（字符串形式）
     * @return 转换后的公顷值（BigDecimal），若输入为空抛出异常
     * @throws IllegalArgumentException 若输入为空或无效数字
     */
    public static BigDecimal convertSquareMetersToHectares(String squareMeters) {
        return convertSquareMetersToHectares(squareMeters, DEFAULT_SCALE);
    }

    /**
     * 将 BigDecimal 表示的平方米值转换为公顷，保留指定小数位数
     * <p>
     * 示例：
     * convertSquareMetersToHectares(new BigDecimal("12345.6789"), 2) → 1.23 公顷
     *
     * @param squareMeters 平方米值（BigDecimal 形式）
     * @param scale        保留的小数位数
     * @return 转换后的公顷值（BigDecimal），若输入为 null 返回 null
     * @throws IllegalArgumentException 若 scale < 0
     */
    public static BigDecimal convertSquareMetersToHectares(BigDecimal squareMeters, int scale) {
        if (squareMeters == null) {
            return null;
        }
        if (scale < 0) {
            throw new IllegalArgumentException("小数位数不能为负数");
        }

        return squareMeters.divide(SQUARE_METERS_PER_HECTARE, scale, RoundingMode.HALF_UP);
    }

    /**
     * 使用默认精度（2位）将 BigDecimal 表示的平方米值转换为公顷
     * <p>
     * 示例：
     * convertSquareMetersToHectares(new BigDecimal("12345")) → 1.23 公顷
     *
     * @param squareMeters 平方米值（BigDecimal 形式）
     * @return 转换后的公顷值（BigDecimal），若输入为 null 返回 null
     */
    public static BigDecimal convertSquareMetersToHectares(BigDecimal squareMeters) {
        return convertSquareMetersToHectares(squareMeters, DEFAULT_SCALE);
    }

    /**
     * 将 double 表示的平方米值转换为公顷，保留指定小数位数
     * <p>
     * 示例：
     * convertSquareMetersToHectares(12345.6789, 2) → 1.23 公顷
     *
     * @param squareMeters 平方米值（double 形式）
     * @param scale        保留的小数位数
     * @return 转换后的公顷值（BigDecimal）
     * @throws IllegalArgumentException 若 scale < 0
     */
    public static BigDecimal convertSquareMetersToHectares(double squareMeters, int scale) {
        return convertSquareMetersToHectares(BigDecimal.valueOf(squareMeters), scale);
    }

    /**
     * 使用默认精度（2位）将 double 表示的平方米值转换为公顷
     * <p>
     * 示例：
     * convertSquareMetersToHectares(12345) → 1.23 公顷
     *
     * @param squareMeters 平方米值（double 形式）
     * @return 转换后的公顷值（BigDecimal）
     */
    public static BigDecimal convertSquareMetersToHectares(double squareMeters) {
        return convertSquareMetersToHectares(BigDecimal.valueOf(squareMeters), DEFAULT_SCALE);
    }

    /**
     * 将 long 表示的平方米值转换为公顷，保留指定小数位数
     * <p>
     * 示例：
     * convertSquareMetersToHectares(12345, 2) → 1.23 公顷
     *
     * @param squareMeters 平方米值（long 形式）
     * @param scale        保留的小数位数
     * @return 转换后的公顷值（BigDecimal）
     * @throws IllegalArgumentException 若 scale < 0
     */
    public static BigDecimal convertSquareMetersToHectares(long squareMeters, int scale) {
        return convertSquareMetersToHectares(BigDecimal.valueOf(squareMeters), scale);
    }

    /**
     * 使用默认精度（2位）将 long 表示的平方米值转换为公顷
     * <p>
     * 示例：
     * convertSquareMetersToHectares(12345) → 1.23 公顷
     *
     * @param squareMeters 平方米值（long 形式）
     * @return 转换后的公顷值（BigDecimal）
     */
    public static BigDecimal convertSquareMetersToHectares(long squareMeters) {
        return convertSquareMetersToHectares(BigDecimal.valueOf(squareMeters), DEFAULT_SCALE);
    }

    // ===== 平方米 → 亩 =====

    /**
     * 将 BigDecimal 表示的平方米值转换为亩，保留指定小数位数
     * <p>
     * 示例：
     * convertSquareMetersToMu(new BigDecimal("666.666667"), 2) → 1.00 亩
     *
     * @param squareMeters 平方米值（BigDecimal 形式）
     * @param scale        保留的小数位数
     * @return 转换后的亩值（BigDecimal），若输入为 null 返回 null
     * @throws IllegalArgumentException 若 scale < 0
     */
    public static BigDecimal convertSquareMetersToMu(BigDecimal squareMeters, int scale) {
        if (squareMeters == null) {
            return null;
        }
        if (scale < 0) {
            throw new IllegalArgumentException("小数位数不能为负数");
        }

        return squareMeters.divide(SQUARE_METERS_PER_MU, scale, RoundingMode.HALF_UP);
    }

    /**
     * 使用默认精度（2位）将 BigDecimal 表示的平方米值转换为亩
     * <p>
     * 示例：
     * convertSquareMetersToMu(new BigDecimal("666.666667")) → 1.00 亩
     *
     * @param squareMeters 平方米值（BigDecimal 形式）
     * @return 转换后的亩值（BigDecimal），若输入为 null 返回 null
     */
    public static BigDecimal convertSquareMetersToMu(BigDecimal squareMeters) {
        return convertSquareMetersToMu(squareMeters, DEFAULT_SCALE);
    }

    // ===== 格式化输出 =====

    /**
     * 将公顷值格式化为字符串并保留指定小数位数
     * <p>
     * 示例：
     * formatHectares(new BigDecimal("1.2345"), 2) → "1.23"
     *
     * @param hectares 公顷值（BigDecimal 形式）
     * @param scale    保留的小数位数
     * @return 格式化后的字符串结果，若输入为 null 返回 null
     * @throws IllegalArgumentException 若 scale < 0
     */
    public static String formatHectares(BigDecimal hectares, int scale) {
        if (hectares == null) {
            return null;
        }
        return hectares.setScale(scale, RoundingMode.HALF_UP).toPlainString();
    }

    /**
     * 使用默认精度（2位）将公顷值格式化为字符串
     * <p>
     * 示例：
     * formatHectares(new BigDecimal("1.2345")) → "1.23"
     *
     * @param hectares 公顷值（BigDecimal 形式）
     * @return 格式化后的字符串结果，若输入为 null 返回 null
     */
    public static String formatHectares(BigDecimal hectares) {
        return formatHectares(hectares, DEFAULT_SCALE);
    }

    /**
     * 将亩值格式化为字符串并保留指定小数位数
     * <p>
     * 示例：
     * formatMu(new BigDecimal("1.2345"), 2) → "1.23"
     *
     * @param mu    亩值（BigDecimal 形式）
     * @param scale 保留的小数位数
     * @return 格式化后的字符串结果，若输入为 null 返回 null
     * @throws IllegalArgumentException 若 scale < 0
     */
    public static String formatMu(BigDecimal mu, int scale) {
        if (mu == null) {
            return null;
        }
        return mu.setScale(scale, RoundingMode.HALF_UP).toPlainString();
    }

    /**
     * 使用默认精度（2位）将亩值格式化为字符串
     * <p>
     * 示例：
     * formatMu(new BigDecimal("1.2345")) → "1.23"
     *
     * @param mu 亩值（BigDecimal 形式）
     * @return 格式化后的字符串结果，若输入为 null 返回 null
     */
    public static String formatMu(BigDecimal mu) {
        return formatMu(mu, DEFAULT_SCALE);
    }
}
