package com.zja.jdk.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: zhengja
 * @Date: 2025-06-13 13:51
 */
public class LocalDateTimeUtilsTest {

    private final LocalDateTime sample = LocalDateTime.of(2025, 5, 28, 14, 30, 0);
    private final LocalDateTime sampleDateTime = LocalDateTime.of(2025, 6, 15, 14, 30, 45);
    private final long sampleTimestamp = LocalDateTimeUtils.toTimestamp(sampleDateTime); // 获取对应时间戳


    @Test
    void testParseDefault() {
        String input = "2025-05-28 14:30:00";
        LocalDateTime result = LocalDateTimeUtils.parseDefault(input);
        assertEquals(sample, result);
    }

    @Test
    void testFormatDefault() {
        String result = LocalDateTimeUtils.formatDefault(sample);
        assertEquals("2025-05-28 14:30:00", result);
    }

    @Test
    void testParseISO() {
        String input = "2025-05-28T14:30:00";
        LocalDateTime result = LocalDateTimeUtils.parseISO(input);
        assertEquals(sample, result);
    }

    @Test
    void testFormatISO() {
        String result = LocalDateTimeUtils.formatISO(sample);
        assertEquals("2025-05-28T14:30:00", result);
    }

    @Test
    void testParseChinese() {
        String input = "2025年05月28日 14时30分00秒";
        LocalDateTime result = LocalDateTimeUtils.parseChinese(input);
        assertEquals(sample, result);
    }

    @Test
    void testFormatChinese() {
        String result = LocalDateTimeUtils.formatChinese(sample);
        assertEquals("2025年05月28日 14时30分00秒", result);
    }

    @Test
    void testParseCompact() {
        String input = "20250528143000";
        LocalDateTime result = LocalDateTimeUtils.parseCompact(input);
        assertEquals(sample, result);
    }

    @Test
    void testFormatCompact() {
        String result = LocalDateTimeUtils.formatCompact(sample);
        assertEquals("20250528143000", result);
    }

    @Test
    void testParseWithInvalidFormatThrowsException() {
        assertThrows(DateTimeParseException.class, () -> {
            LocalDateTimeUtils.parseChinese("2025/05/28 14:30:00");
        });
    }

    @Test
    void testFormatTimeOnly() {
        assertEquals("14:30:00", LocalDateTimeUtils.formatTimeOnly(sample));
    }

    @Test
    void testFormatChineseTimeOnly() {
        assertEquals("14时30分00秒", LocalDateTimeUtils.formatChineseTimeOnly(sample));
    }

    @Test
    void testFormatChineseFull() {
        String result = LocalDateTimeUtils.formatChineseFull(sampleDateTime);
        assertEquals("2025年06月15日 星期日 14时30分45秒", result);
    }

    @Test
    void testFormatChineseSimple() {
        LocalDateTime dateTime = LocalDateTime.of(2025, 6, 15, 14, 3, 5);
        String result = LocalDateTimeUtils.formatChineseSimple(dateTime);
        assertEquals("2025年6月15日 14时3分5秒", result);
    }

    @Test
    void testFormatAmerican() {
        String result = LocalDateTimeUtils.formatAmerican(sampleDateTime);
        assertEquals("06/15/2025 14:30:45", result);
    }

    @Test
    void testParseChineseFull() {
        String input = "2025年06月15日 星期日 14时30分00秒";
        assertNotNull(LocalDateTimeUtils.parseChineseFull(input));
    }

    @Test
    void testParseChineseSimple() {
        String input = "2025年6月5日 14时30分5秒";
        assertNotNull(LocalDateTimeUtils.parseChineseSimple(input));
    }

    @Test
    void testParseAmerican() {
        String input = "06/15/2025 14:30:00";
        assertNotNull(LocalDateTimeUtils.parseAmerican(input));
    }

    // 格式转换

    @Test
    void testParseWithInvalidInputThrowsException() {
        assertThrows(DateTimeParseException.class, () -> {
            LocalDateTimeUtils.parseChineseFull("2025年06月15日");
        });
    }

    @Test
    void testParseDateOnly() {
        LocalDateTime result = LocalDateTimeUtils.parseDateOnly("2025-06-15");
        assertEquals("2025-06-15T00:00", result.toString());
    }

    @Test
    void testFormatDateOnly() {
        LocalDateTime now = LocalDateTime.now();
        String result = LocalDateTimeUtils.formatDateOnly(now);
        String expected = now.toLocalDate().toString(); // 默认格式 yyyy-MM-dd
        assertEquals(expected, result);
    }

    @Test
    void testParseChineseDateOnly() {
        LocalDateTime result = LocalDateTimeUtils.parseChineseDateOnly("2025年06月15日");
        assertEquals("2025-06-15T00:00", result.toString());
    }

    @Test
    void testFormatChineseDateOnly() {
        LocalDateTime now = LocalDateTime.of(2025, 6, 15, 14, 30);
        String result = LocalDateTimeUtils.formatChineseDateOnly(now);
        assertEquals("2025年06月15日", result);
    }

    // 日期转换

    @Test
    void testToLocalDateTime_withValidDate() {
        Date date = Date.from(sampleDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());
        LocalDateTime result = LocalDateTimeUtils.toLocalDateTime(date);
        assertNotNull(result);
        assertEquals(sampleDateTime, result);
    }

    @Test
    void testToDate_withValidLocalDateTime() {
        Date result = LocalDateTimeUtils.toDate(sampleDateTime);
        assertNotNull(result);
        LocalDateTime back = LocalDateTimeUtils.toLocalDateTime(result);
        assertEquals(sampleDateTime, back);
    }

    // 时间戳转换

    @Test
    void testFromTimestamp() {
        LocalDateTime result = LocalDateTimeUtils.fromTimestamp(sampleTimestamp);
        assertNotNull(result);
        assertEquals(sampleDateTime, result);
    }

    @Test
    void testToTimestamp() {
        long result = LocalDateTimeUtils.toTimestamp(sampleDateTime);
        assertEquals(sampleTimestamp, result);
    }

    @Test
    void testRoundTrip_Date_LocalDateTime_Date() {
        Date original = Date.from(sampleDateTime.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime ldt = LocalDateTimeUtils.toLocalDateTime(original);
        Date roundTrip = LocalDateTimeUtils.toDate(ldt);

        assertNotNull(roundTrip);
        assertEquals(original.getTime(), roundTrip.getTime());
    }

    @Test
    void testRoundTrip_Timestamp_LocalDateTime_Timestamp() {
        LocalDateTime ldt = LocalDateTimeUtils.fromTimestamp(sampleTimestamp);
        long roundTrip = LocalDateTimeUtils.toTimestamp(ldt);
        assertEquals(sampleTimestamp, roundTrip);
    }
}