package com.zja.jdk.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: zhengja
 * @Date: 2025-06-13 14:20
 */
public class BigDecimalUtilsTest {

    // ===== 测试 convertSquareMetersToHectares =====

    @Test
    void testConvertSquareMetersToHectares_StringWithScale() {
        BigDecimal result = BigDecimalUtils.convertSquareMetersToHectares("12345.6789", 2);
        assertEquals(new BigDecimal("1.23"), result);
    }

    @Test
    void testConvertSquareMetersToHectares_String_DefaultScale() {
        BigDecimal result = BigDecimalUtils.convertSquareMetersToHectares("12345");
        assertEquals(new BigDecimal("1.23"), result);
    }

    @Test
    void testConvertSquareMetersToHectares_BigDecimalWithScale() {
        BigDecimal input = new BigDecimal("12345.6789");
        BigDecimal result = BigDecimalUtils.convertSquareMetersToHectares(input, 2);
        assertEquals(new BigDecimal("1.23"), result);
    }

    @Test
    void testConvertSquareMetersToHectares_BigDecimal_DefaultScale() {
        BigDecimal input = new BigDecimal("12345");
        BigDecimal result = BigDecimalUtils.convertSquareMetersToHectares(input);
        assertEquals(new BigDecimal("1.23"), result);
    }

    @Test
    void testConvertSquareMetersToHectares_DoubleWithScale() {
        BigDecimal result = BigDecimalUtils.convertSquareMetersToHectares(12345.6789, 2);
        assertEquals(new BigDecimal("1.23"), result);
    }

    @Test
    void testConvertSquareMetersToHectares_Double_DefaultScale() {
        BigDecimal result = BigDecimalUtils.convertSquareMetersToHectares(12345);
        assertEquals(new BigDecimal("1.23"), result);
    }

    @Test
    void testConvertSquareMetersToHectares_LongWithScale() {
        BigDecimal result = BigDecimalUtils.convertSquareMetersToHectares(12345L, 2);
        assertEquals(new BigDecimal("1.23"), result);
    }

    @Test
    void testConvertSquareMetersToHectares_Long_DefaultScale() {
        BigDecimal result = BigDecimalUtils.convertSquareMetersToHectares(12345L);
        assertEquals(new BigDecimal("1.23"), result);
    }

    @Test
    void testConvertSquareMetersToHectares_NullInput() {
        assertNull(BigDecimalUtils.convertSquareMetersToHectares((BigDecimal) null, 2));
    }

    @Test
    void testConvertSquareMetersToHectares_NegativeScale_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            BigDecimalUtils.convertSquareMetersToHectares(new BigDecimal("10000"), -1);
        });
    }

    // ===== 测试 convertSquareMetersToMu =====

    @Test
    void testConvertSquareMetersToMu_BigDecimalWithScale() {
        BigDecimal input = new BigDecimal("666.666667");
        BigDecimal result = BigDecimalUtils.convertSquareMetersToMu(input, 2);
        assertEquals(new BigDecimal("1.00"), result);
    }

    @Test
    void testConvertSquareMetersToMu_BigDecimal_DefaultScale() {
        BigDecimal input = new BigDecimal("666.666667");
        BigDecimal result = BigDecimalUtils.convertSquareMetersToMu(input);
        assertEquals(new BigDecimal("1.00"), result);
    }

    @Test
    void testConvertSquareMetersToMu_NullInput() {
        assertNull(BigDecimalUtils.convertSquareMetersToMu(null, 2));
    }

    @Test
    void testConvertSquareMetersToMu_NegativeScale_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            BigDecimalUtils.convertSquareMetersToMu(new BigDecimal("666.666667"), -1);
        });
    }

    // ===== 测试 formatHectares =====

    @Test
    void testFormatHectares_WithScale() {
        BigDecimal value = new BigDecimal("1.2345");
        String result = BigDecimalUtils.formatHectares(value, 2);
        assertEquals("1.23", result);
    }

    @Test
    void testFormatHectares_DefaultScale() {
        BigDecimal value = new BigDecimal("1.2345");
        String result = BigDecimalUtils.formatHectares(value);
        assertEquals("1.23", result);
    }

    @Test
    void testFormatHectares_NullInput() {
        String result = BigDecimalUtils.formatHectares(null);
        assertNull(result);
    }

    // ===== 测试 formatMu =====

    @Test
    void testFormatMu_WithScale() {
        BigDecimal value = new BigDecimal("1.2345");
        String result = BigDecimalUtils.formatMu(value, 2);
        assertEquals("1.23", result);
    }

    @Test
    void testFormatMu_DefaultScale() {
        BigDecimal value = new BigDecimal("1.2345");
        String result = BigDecimalUtils.formatMu(value);
        assertEquals("1.23", result);
    }

    @Test
    void testFormatMu_NullInput() {
        String result = BigDecimalUtils.formatMu(null);
        assertNull(result);
    }

    // ===== 测试 isNullOrZero =====

    @Test
    void testIsNullOrZero_Null() {
        assertTrue(BigDecimalUtils.isNullOrZero(null));
    }

    @Test
    void testIsNullOrZero_Zero() {
        assertTrue(BigDecimalUtils.isNullOrZero(BigDecimal.ZERO));
    }

    @Test
    void testIsNullOrZero_PositiveValue() {
        assertFalse(BigDecimalUtils.isNullOrZero(new BigDecimal("100")));
    }

    @Test
    void testIsNullOrZero_ZeroWithStringValue() {
        assertTrue(BigDecimalUtils.isNullOrZero(new BigDecimal("0")));
    }
}