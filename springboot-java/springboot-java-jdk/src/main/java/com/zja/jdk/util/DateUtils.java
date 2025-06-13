package com.zja.jdk.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * 日期与字符串转换工具类（线程安全、可扩展）
 *
 * @Author: zhengja
 * @Date: 2025-05-28 18:35
 */
public class DateUtils {

    // 常用日期时间格式
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String COMPACT_DATE_FORMAT = "yyyyMMdd";
    public static final String COMPACT_DATETIME_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_SLASH_FORMAT = "yyyy/MM/dd";
    public static final String DATETIME_SLASH_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_UNDERSCORE_TIME_MILLISECOND = "yyyy_MM_dd_HH_mm_ss_SSS";
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String AMERICAN_DATE_FORMAT = "MM/dd/yyyy";
    public static final String AMERICAN_DATETIME_FORMAT = "MM/dd/yyyy HH:mm:ss";
    // 中文格式
    public static final String CHINESE_DATE_FORMAT = "yyyy年MM月dd日";
    public static final String CHINESE_DATETIME_FORMAT = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String CHINESE_SHORT_DATETIME_FORMAT = "yyyy年MM月dd日 HH:mm:ss";
    public static final String CHINESE_FULL_DATETIME_FORMAT = "yyyy年MM月dd日 HH时mm分ss秒 EEEE"; // 带星期几
    public static final String CHINESE_SIMPLE_DATE_FORMAT = "yyyy年M月d日";                     // 不带补零
    public static final String CHINESE_SIMPLE_DATETIME_FORMAT = "yyyy年M月d日 H时m分s秒";          // 不带补零
    public static final String CHINESE_MONTH_DAY_FORMAT = "MM月dd日";                            // 仅月份与日期
    public static final String CHINESE_HOUR_MINUTE_FORMAT = "HH时mm分";                         // 仅小时与分钟


    /**
     * 将 Date 格式化为指定模式的字符串（通用方法）
     *
     * @param date    日期对象
     * @param pattern 格式模板，例如 "yyyy-MM-dd HH:mm:ss"
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.format(formatter);
    }

    /**
     * 将字符串解析为 Date 对象（通用方法）
     *
     * @param dateStr 日期字符串
     * @param pattern 字符串对应的格式模板
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date parse(String dateStr, String pattern) throws IllegalArgumentException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        if (pattern == null || pattern.trim().isEmpty()) {
            throw new IllegalArgumentException("格式模板 [pattern] 不能为空");
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            TemporalAccessor accessor = formatter.parse(dateStr);

            Instant instant;
            // 判断是否包含时间信息
            if (accessor.isSupported(java.time.temporal.ChronoField.HOUR_OF_DAY) ||
                    accessor.isSupported(java.time.temporal.ChronoField.MINUTE_OF_HOUR) ||
                    accessor.isSupported(java.time.temporal.ChronoField.SECOND_OF_MINUTE)) {
                // 如果包含时间字段，使用 LocalDateTime 解析
                LocalDateTime localDateTime = LocalDateTime.from(accessor);
                instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
            } else {
                // 否则只包含日期，使用 LocalDate，默认时间为 00:00
                LocalDate localDate = LocalDate.from(accessor);
                instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            }

            return Date.from(instant);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("日期格式错误: " + dateStr, e);
        }
    }


    /**
     * 将Date转换为默认日期格式字符串(yyyy-MM-dd)
     */
    public static String dateToDefaultDateString(Date date) {
        return format(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * 将Date转换为默认日期时间格式字符串(yyyy-MM-dd HH:mm:ss)
     */
    public static String dateToDefaultDateTimeString(Date date) {
        return format(date, DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 将默认日期格式字符串(yyyy-MM-dd)转换为Date
     */
    public static Date defaultDateStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, DEFAULT_DATE_FORMAT);
    }

    /**
     * 将默认日期时间格式字符串(yyyy-MM-dd HH:mm:ss)转换为Date
     */
    public static Date defaultDateTimeStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 将Date转换为紧凑日期格式字符串(yyyyMMdd)
     */
    public static String dateToCompactDateString(Date date) {
        return format(date, COMPACT_DATE_FORMAT);
    }

    /**
     * 将Date转换为紧凑日期时间格式字符串(yyyyMMddHHmmss)
     */
    public static String dateToCompactDateTimeString(Date date) {
        return format(date, COMPACT_DATETIME_FORMAT);
    }

    /**
     * 将紧凑日期格式字符串(yyyyMMdd)转换为Date
     */
    public static Date compactDateStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, COMPACT_DATE_FORMAT);
    }

    /**
     * 将紧凑日期时间格式字符串(yyyyMMddHHmmss)转换为Date
     */
    public static Date compactDateTimeStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, COMPACT_DATETIME_FORMAT);
    }

    /**
     * 将 Date 格式化为 yyyy/MM/dd 格式的字符串
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateToSlashString(Date date) {
        return format(date, DATE_SLASH_FORMAT);
    }

    /**
     * 将 yyyy/MM/dd 格式的字符串解析为 Date 对象
     *
     * @param dateStr 日期字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date slashStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, DATE_SLASH_FORMAT);
    }

    /**
     * 将 Date 格式化为 yyyy/MM/dd HH:mm:ss 格式的字符串
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateTimeToSlashString(Date date) {
        return format(date, DATETIME_SLASH_FORMAT);
    }

    /**
     * 将 yyyy/MM/dd HH:mm:ss 格式的字符串解析为 Date 对象
     *
     * @param dateStr 日期字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date slashDateTimeToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, DATETIME_SLASH_FORMAT);
    }

    /**
     * 将 Date 格式化为 yyyy_MM_dd_HH_mm_ss_SSS 格式的字符串（含毫秒）
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateToMillisecondString(Date date) {
        return format(date, DATE_UNDERSCORE_TIME_MILLISECOND);
    }

    /**
     * 将 yyyy_MM_dd_HH_mm_ss_SSS 格式的字符串解析为 Date 对象
     *
     * @param dateStr 日期字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date millisecondStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, DATE_UNDERSCORE_TIME_MILLISECOND);
    }

    /**
     * 将 Date 格式化为 ISO 8601 标准格式字符串：yyyy-MM-dd'T'HH:mm:ss
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateToIsoString(Date date) {
        return format(date, ISO_DATE_FORMAT);
    }

    /**
     * 将 ISO 8601 格式字符串（yyyy-MM-dd'T'HH:mm:ss）解析为 Date 对象
     *
     * @param dateStr ISO 8601 格式的日期字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date isoStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, ISO_DATE_FORMAT);
    }

    /**
     * 将 Date 格式化为美式日期格式字符串 MM/dd/yyyy
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateToAmericanDateString(Date date) {
        return format(date, AMERICAN_DATE_FORMAT);
    }

    /**
     * 将美式日期格式字符串 MM/dd/yyyy 解析为 Date 对象
     *
     * @param dateStr 美式日期字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date americanDateStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, AMERICAN_DATE_FORMAT);
    }

    /**
     * 将 Date 格式化为美式日期时间格式字符串 MM/dd/yyyy HH:mm:ss
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateToAmericanDateTimeString(Date date) {
        return format(date, AMERICAN_DATETIME_FORMAT);
    }

    /**
     * 将美式日期时间格式字符串 MM/dd/yyyy HH:mm:ss 解析为 Date 对象
     *
     * @param dateStr 美式日期时间字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date americanDateTimeStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, AMERICAN_DATETIME_FORMAT);
    }

    // 中文格式转换方法

    /**
     * 将 Date 格式化为中文日期格式字符串 yyyy年MM月dd日
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateToChineseDateString(Date date) {
        return format(date, CHINESE_DATE_FORMAT);
    }

    /**
     * 将中文日期格式字符串 yyyy年MM月dd日 解析为 Date 对象
     *
     * @param dateStr 中文日期字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date chineseDateStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, CHINESE_DATE_FORMAT);
    }

    /**
     * 将 Date 格式化为中文日期时间格式字符串 yyyy年MM月dd日 HH时mm分ss秒
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateToChineseDateTimeString(Date date) {
        return format(date, CHINESE_DATETIME_FORMAT);
    }

    /**
     * 将中文日期时间格式字符串 yyyy年MM月dd日 HH时mm分ss秒 解析为 Date 对象
     *
     * @param dateStr 中文日期时间字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date chineseDateTimeStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, CHINESE_DATETIME_FORMAT);
    }

    /**
     * 将 Date 格式化为中文短日期时间格式字符串 yyyy年MM月dd日 HH:mm:ss
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateToChineseShortDateTimeString(Date date) {
        return format(date, CHINESE_SHORT_DATETIME_FORMAT);
    }

    /**
     * 将中文短日期时间格式字符串 yyyy年MM月dd日 HH:mm:ss 解析为 Date 对象
     *
     * @param dateStr 中文短日期时间字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date chineseShortDateTimeStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, CHINESE_SHORT_DATETIME_FORMAT);
    }

    /**
     * 将 Date 格式化为带星期的中文日期时间格式字符串 yyyy年MM月dd日 HH时mm分ss秒 EEEE
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateToChineseFullDateTimeString(Date date) {
        return format(date, CHINESE_FULL_DATETIME_FORMAT);
    }

    /**
     * 将带星期的中文日期时间格式字符串解析为 Date 对象
     *
     * @param dateStr 中文完整日期时间字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date chineseFullDateTimeStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, CHINESE_FULL_DATETIME_FORMAT);
    }

    /**
     * 将 Date 格式化为无补零的中文日期格式字符串 yyyy年M月d日
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateToChineseSimpleDateString(Date date) {
        return format(date, CHINESE_SIMPLE_DATE_FORMAT);
    }

    /**
     * 将无补零的中文日期字符串解析为 Date
     *
     * @param dateStr 中文日期字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date chineseSimpleDateStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, CHINESE_SIMPLE_DATE_FORMAT);
    }

    /**
     * 将 Date 格式化为无补零的中文日期时间格式字符串 yyyy年M月d日 H时m分s秒
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateToChineseSimpleDateTimeString(Date date) {
        return format(date, CHINESE_SIMPLE_DATETIME_FORMAT);
    }

    /**
     * 将无补零的中文日期时间字符串解析为 Date
     *
     * @param dateStr 中文日期时间字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date chineseSimpleDateTimeStringToDate(String dateStr) throws IllegalArgumentException {
        return parse(dateStr, CHINESE_SIMPLE_DATETIME_FORMAT);
    }

    /**
     * 将 Date 格式化为 MM月dd日 格式的字符串
     *
     * @param date Date 对象
     * @return 格式化后的字符串，如果 date 为 null 返回 null
     */
    public static String dateToChineseMonthDayString(Date date) {
        return format(date, CHINESE_MONTH_DAY_FORMAT);
    }

    /**
     * 将 MM月dd日 格式的字符串解析为 Date（默认年份为今年）
     *
     * @param dateStr 月份+日期字符串
     * @return 解析后的 Date 对象，如果 dateStr 为空返回 null
     * @throws IllegalArgumentException 如果解析失败
     */
    public static Date chineseMonthDayStringToDate(String dateStr) throws IllegalArgumentException {
        int currentYear = LocalDate.now().getYear();
        String fullDateStr = currentYear + "年" + dateStr;
        return parse(fullDateStr, CHINESE_SIMPLE_DATE_FORMAT);
    }


    // LocalDateTime 转换

    /**
     * 将 Date 转换为 LocalDateTime
     *
     * @param date Date 对象
     * @return LocalDateTime 对象，如果 date 为 null 返回 null
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 将 LocalDateTime 转换为 Date
     *
     * @param localDateTime LocalDateTime 对象
     * @return Date 对象，如果 localDateTime 为 null 返回 null
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    // 时间戳转换

    /**
     * 将毫秒级时间戳转换为 Date 对象
     *
     * @param timestamp 毫秒级时间戳
     * @return 对应的 Date 对象
     */
    public static Date fromTimestamp(long timestamp) {
        return new Date(timestamp);
    }

    /**
     * 将 Date 对象转换为毫秒级时间戳
     *
     * @param date Date 对象
     * @return 毫秒级时间戳
     */
    public static long toTimestamp(Date date) {
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }
}
