package com.zja.jdk.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: zhengja
 * @Date: 2025-06-13 11:18
 */
public class DateUtilsTest {

    // 测试通用 format 方法
    @Test
    void testFormat() {
        Date now = new Date();
        String formatted = DateUtils.format(now, "yyyy-MM-dd HH:mm:ss");
        assertNotNull(formatted);
        assertTrue(formatted.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }

    // 测试通用 parse 方法
    @Test
    void testParse() throws Exception {
        String dateStr = "2025-05-28 14:30:00";
        Date parsed = DateUtils.parse(dateStr, "yyyy-MM-dd HH:mm:ss");
        assertNotNull(parsed);
        assertEquals("2025-05-28 14:30:00", DateUtils.format(parsed, "yyyy-MM-dd HH:mm:ss"));
    }

    // 测试 null 输入返回 null
    @Test
    void testFormatWithNullDate() {
        String result = DateUtils.format(null, "yyyy-MM-dd");
        assertNull(result);
    }

    // 测试空字符串输入返回 null
    @Test
    void testParseWithEmptyString() {
        Date result = DateUtils.parse("", "yyyy-MM-dd");
        assertNull(result);
    }

    // 测试错误格式抛出异常
    @Test
    void testParseWithInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtils.parse("2025/01/01", "yyyy-MM-dd");
        });
    }

    // 测试默认日期格式输出
    @Test
    void testDateToDefaultDateString() {
        Date now = new Date();
        String formatted = DateUtils.dateToDefaultDateString(now);
        assertEquals(DateUtils.format(now, DateUtils.DEFAULT_DATE_FORMAT), formatted);
    }

    // 测试默认日期时间格式输出
    @Test
    void testDateToDefaultDateTimeString() {
        Date now = new Date();
        String formatted = DateUtils.dateToDefaultDateTimeString(now);
        assertEquals(DateUtils.format(now, DateUtils.DEFAULT_DATETIME_FORMAT), formatted);
    }

    // 测试默认日期字符串转 Date
    @Test
    void testDefaultDateStringToDate() throws Exception {
        String dateStr = "2025-05-28";
        Date parsed = DateUtils.defaultDateStringToDate(dateStr);
        assertNotNull(parsed);
        assertEquals(dateStr, DateUtils.dateToDefaultDateString(parsed));
    }

    // 测试默认日期时间字符串转 Date
    @Test
    void testDefaultDateTimeStringToDate() throws Exception {
        String dateStr = "2025-05-28 14:30:00";
        Date parsed = DateUtils.defaultDateTimeStringToDate(dateStr);
        assertNotNull(parsed);
        assertEquals(dateStr, DateUtils.dateToDefaultDateTimeString(parsed));
    }

    // 测试紧凑日期格式转换
    @Test
    void testCompactDateStringToDate() throws Exception {
        String dateStr = "20250528";
        Date parsed = DateUtils.compactDateStringToDate(dateStr);
        assertNotNull(parsed);
        assertEquals(dateStr, DateUtils.dateToCompactDateString(parsed));
    }

    // 测试紧凑日期时间格式转换
    @Test
    void testCompactDateTimeStringToDate() throws Exception {
        String dateStr = "20250528143000";
        Date parsed = DateUtils.compactDateTimeStringToDate(dateStr);
        assertNotNull(parsed);
        assertEquals(dateStr, DateUtils.dateToCompactDateTimeString(parsed));
    }

    // 测试自定义格式转换
    @Test
    void testCustomFormatAndParse() throws Exception {
        String customPattern = "yyyy/MM/dd HH:mm";
        Date now = new Date();
        String formatted = DateUtils.format(now, customPattern);
        Date parsed = DateUtils.parse(formatted, customPattern);
        assertEquals(DateUtils.format(parsed, customPattern), formatted);
    }

    // 测试斜杠格式转换
    @Test
    void testSlashFormat() throws Exception {
        Date now = new Date();
        String formatted = DateUtils.dateToSlashString(now);
        Date parsed = DateUtils.slashStringToDate(formatted);
        assertEquals(formatted, DateUtils.dateToSlashString(parsed));
    }

    // 测试 dateTimeToSlashString 和 slashDateTimeToDate
    @Test
    void testDateTimeToSlashFormat() {
        String expected = "2025/06/15 14:30:00";
        Date parsed = DateUtils.slashDateTimeToDate(expected);
        String formatted = DateUtils.dateTimeToSlashString(parsed);

        assertEquals(expected, formatted);
        assertEquals(expected, DateUtils.dateTimeToSlashString(parsed));
    }

    @Test
    void testSlashDateTimeToDate_withInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtils.slashDateTimeToDate("2025-06-15 14:30:00"); // 错误分隔符
        });
    }

    // 测试 dateToMillisecondString 和 millisecondStringToDate
    @Test
    void testDateToMillisecondFormat() throws Exception {
        String expected = "2025_06_15_14_30_00_123";
        Date parsed = DateUtils.millisecondStringToDate(expected);
        String formatted = DateUtils.dateToMillisecondString(parsed);

        assertEquals(expected, formatted);
    }

    @Test
    void testMillisecondStringToDate_withInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtils.millisecondStringToDate("2025-06-15 14:30:00"); // 错误格式
        });
    }

    // 测试时间戳一致性（格式化与解析是否保持一致）
    @Test
    void testDateTimeToSlashString_roundTrip() {
        Date now = new Date();
        String formatted = DateUtils.dateTimeToSlashString(now);
        Date parsed = DateUtils.slashDateTimeToDate(formatted);

        assertEquals(
                now.getTime(),
                parsed.getTime(),
                1000 // 允许最多1秒误差（因格式不包含毫秒）
        );
    }

    @Test
    void testDateToMillisecondString_roundTrip() {
        Date now = new Date();
        String formatted = DateUtils.dateToMillisecondString(now);
        Date parsed = DateUtils.millisecondStringToDate(formatted);

        assertEquals(
                now.getTime(),
                parsed.getTime(),
                1 // 毫秒级精确，允许1ms误差
        );
    }

    // 测试 ISO 格式转换
    @Test
    void testIsoFormat() throws Exception {
        Date now = new Date();
        String formatted = DateUtils.dateToIsoString(now);
        Date parsed = DateUtils.isoStringToDate(formatted);
        assertEquals(formatted, DateUtils.dateToIsoString(parsed));
    }

    // 测试 美式日期格式转换
    @Test
    void testAmericanDateFormat() throws Exception {
        String dateStr = "06/15/2025";
        Date parsed = DateUtils.americanDateStringToDate(dateStr);
        assertEquals(dateStr, DateUtils.dateToAmericanDateString(parsed));
    }

    // 测试 中文日期格式转换

    @Test
    void testChineseDateFormat() throws Exception {
        String expected = "2025年06月15日";
        Date parsed = DateUtils.chineseDateStringToDate(expected);
        String formatted = DateUtils.dateToChineseDateString(parsed);

        assertEquals(expected, formatted);
        assertEquals(expected, DateUtils.dateToChineseDateString(parsed));
    }

    @Test
    void testChineseDateTimeFormat() throws Exception {
        String expected = "2025年06月15日 14时30分00秒";
        Date parsed = DateUtils.chineseDateTimeStringToDate(expected);
        String formatted = DateUtils.dateToChineseDateTimeString(parsed);

        assertEquals(expected, formatted);
    }

    @Test
    void testChineseShortDateTimeFormat() throws Exception {
        String expected = "2025年06月15日 14:30:00";
        Date parsed = DateUtils.chineseShortDateTimeStringToDate(expected);
        String formatted = DateUtils.dateToChineseShortDateTimeString(parsed);

        assertEquals(expected, formatted);
    }

    @Test
    void testChineseDateTime_withNullInput() {
        assertNull(DateUtils.chineseDateStringToDate(null));
        assertNull(DateUtils.chineseDateTimeStringToDate(null));
    }

    @Test
    void testChineseDateTime_withInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtils.chineseDateStringToDate("2025-06-15");
        });
    }

    @Test
    void testChineseFullDateTimeFormat() throws Exception {
        String expected = "2025年06月15日 14时30分00秒 星期日";
        Date parsed = DateUtils.chineseFullDateTimeStringToDate(expected);
        String formatted = DateUtils.dateToChineseFullDateTimeString(parsed);
        assertEquals(expected, formatted);
    }

    @Test
    void testChineseSimpleDateFormat() throws Exception {
        String expected = "2025年6月5日";
        Date parsed = DateUtils.chineseSimpleDateStringToDate(expected);
        String formatted = DateUtils.dateToChineseSimpleDateString(parsed);
        assertEquals(expected, formatted);
    }

    /**
     * 测试 dateToChineseSimpleDateTimeString 是否能正确格式化日期时间
     */
    @Test
    void testDateToChineseSimpleDateTimeString() {
        // 构造一个特定日期时间：2025年6月15日 14:30:05
        Date date = DateUtils.parse("2025年06月15日 14:30:05", "yyyy年MM月dd日 HH:mm:ss");

        String formatted = DateUtils.dateToChineseSimpleDateTimeString(date);

        // 预期结果：不带前导0的格式
        assertEquals("2025年6月15日 14时30分5秒", formatted);
    }

    /**
     * 测试 chineseSimpleDateTimeStringToDate 是否能正确解析中文无补零格式
     */
    @Test
    void testChineseSimpleDateTimeStringToDate() throws Exception {
        String input = "2025年6月15日 14时30分5秒";

        Date parsed = DateUtils.chineseSimpleDateTimeStringToDate(input);

        // 验证是否解析为正确的日期时间
        Date expected = DateUtils.parse("2025年06月15日 14:30:05", "yyyy年MM月dd日 HH:mm:ss");
        assertNotNull(parsed);
        assertEquals(expected.getTime(), parsed.getTime());
    }

    /**
     * 测试非法格式输入是否会抛出异常
     */
    @Test
    void testChineseSimpleDateTimeStringToDate_withInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            DateUtils.chineseSimpleDateTimeStringToDate("2025/06/15 14:30:05");
        });
    }

    @Test
    void testChineseMonthDayFormat() throws Exception {
        String expected = "06月15日";
        Date parsed = DateUtils.chineseMonthDayStringToDate(expected);
        String formatted = DateUtils.dateToChineseMonthDayString(parsed);
        assertEquals(expected, formatted);
    }

    // 测试 toLocalDateTime
    @Test
    void testToLocalDateTime() {
        Date now = new Date();
        LocalDateTime ldt = DateUtils.toLocalDateTime(now);
        assertNotNull(ldt);
        assertEquals(now.getTime(), DateUtils.toDate(ldt).getTime(), 1000); // 允许1秒误差
    }

    // 测试 toDate
    @Test
    void testToDate() {
        LocalDateTime now = LocalDateTime.now();
        Date date = DateUtils.toDate(now);
        assertNotNull(date);
        assertEquals(now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                date.toInstant().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    // 测试 fromTimestamp

    @Test
    void testFromTimestamp() {
        long now = System.currentTimeMillis();
        Date date = DateUtils.fromTimestamp(now);
        assertEquals(now, date.getTime());
    }

    @Test
    void testToTimestamp() {
        Date now = new Date();
        long timestamp = DateUtils.toTimestamp(now);
        assertEquals(now.getTime(), timestamp);
    }

    @Test
    void testToTimestamp_withNullDate() {
        long timestamp = DateUtils.toTimestamp(null);
        assertEquals(0, timestamp);
    }
}