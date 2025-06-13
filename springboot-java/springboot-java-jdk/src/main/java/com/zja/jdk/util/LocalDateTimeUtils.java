package com.zja.jdk.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * LocalDateTime 与字符串互转工具类
 *
 * @Author: zhengja
 * @Date: 2025-05-28 16:40
 */
public class LocalDateTimeUtils {

    // 常用日期时间格式
    public static final String DATE_ONLY_FORMAT = "yyyy-MM-dd";
    public static final String CHINESE_DATE_ONLY_FORMAT = "yyyy年MM月dd日";
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String CHINESE_DATETIME_FORMAT = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String COMPACT_DATETIME_FORMAT = "yyyyMMddHHmmss";
    public static final String TIME_ONLY_FORMAT = "HH:mm:ss";
    public static final String CHINESE_TIME_ONLY_FORMAT = "HH时mm分ss秒";
    public static final String CHINESE_FULL_DATETIME_FORMAT = "yyyy年MM月dd日 EEEE HH时mm分ss秒"; // 带星期几
    public static final String CHINESE_SIMPLE_DATETIME_FORMAT = "yyyy年M月d日 H时m分s秒";          // 不带前导0
    public static final String AMERICAN_DATETIME_FORMAT = "MM/dd/yyyy HH:mm:ss";


    /**
     * 将字符串解析为 LocalDateTime（通用方法）
     *
     * @param text    日期字符串
     * @param pattern 格式模板
     * @return 解析后的 LocalDateTime 对象
     */
    public static LocalDateTime parse(String text, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(text, formatter);
    }

    /**
     * 按默认格式 (yyyy-MM-dd HH:mm:ss) 解析字符串
     */
    public static LocalDateTime parseDefault(String text) {
        return parse(text, DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 按 ISO 格式 (yyyy-MM-dd'T'HH:mm:ss) 解析字符串
     */
    public static LocalDateTime parseISO(String text) {
        return parse(text, ISO_DATETIME_FORMAT);
    }

    /**
     * 按中文格式 (yyyy年MM月dd日 HH时mm分ss秒) 解析字符串
     */
    public static LocalDateTime parseChinese(String text) {
        return parse(text, CHINESE_DATETIME_FORMAT);
    }

    /**
     * 按紧凑格式 (yyyyMMddHHmmss) 解析字符串
     */
    public static LocalDateTime parseCompact(String text) {
        return parse(text, COMPACT_DATETIME_FORMAT);
    }

    /**
     * 将 LocalDateTime 格式化为字符串（通用方法）
     *
     * @param dateTime LocalDateTime 对象
     * @param pattern  格式模板
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    /**
     * 按默认格式 (yyyy-MM-dd HH:mm:ss) 格式化 LocalDateTime
     */
    public static String formatDefault(LocalDateTime dateTime) {
        return format(dateTime, DEFAULT_DATETIME_FORMAT);
    }

    /**
     * 按 ISO 格式 (yyyy-MM-dd'T'HH:mm:ss) 格式化 LocalDateTime
     */
    public static String formatISO(LocalDateTime dateTime) {
        return format(dateTime, ISO_DATETIME_FORMAT);
    }

    /**
     * 按中文格式 (yyyy年MM月dd日 HH时mm分ss秒) 格式化 LocalDateTime
     */
    public static String formatChinese(LocalDateTime dateTime) {
        return format(dateTime, CHINESE_DATETIME_FORMAT);
    }

    /**
     * 按紧凑格式 (yyyyMMddHHmmss) 格式化 LocalDateTime
     */
    public static String formatCompact(LocalDateTime dateTime) {
        return format(dateTime, COMPACT_DATETIME_FORMAT);
    }

    /**
     * 按带星期的中文完整格式解析字符串
     */
    public static LocalDateTime parseChineseFull(String text) {
        return parse(text, CHINESE_FULL_DATETIME_FORMAT);
    }

    /**
     * 按不带前导0的中文格式解析字符串
     */
    public static LocalDateTime parseChineseSimple(String text) {
        return parse(text, CHINESE_SIMPLE_DATETIME_FORMAT);
    }

    /**
     * 按美式格式解析字符串（MM/dd/yyyy HH:mm:ss）
     */
    public static LocalDateTime parseAmerican(String text) {
        return parse(text, AMERICAN_DATETIME_FORMAT);
    }

    /**
     * 按默认时间格式 (HH:mm:ss) 格式化 LocalDateTime（仅时间）
     */
    public static String formatTimeOnly(LocalDateTime dateTime) {
        return format(dateTime, TIME_ONLY_FORMAT);
    }

    /**
     * 按中文时间格式 (HH时mm分ss秒) 格式化 LocalDateTime
     */
    public static String formatChineseTimeOnly(LocalDateTime dateTime) {
        return format(dateTime, CHINESE_TIME_ONLY_FORMAT);
    }

    /**
     * 按带星期的中文完整格式格式化 LocalDateTime
     */
    public static String formatChineseFull(LocalDateTime dateTime) {
        return format(dateTime, CHINESE_FULL_DATETIME_FORMAT);
    }

    /**
     * 按不带前导0的中文格式格式化 LocalDateTime
     */
    public static String formatChineseSimple(LocalDateTime dateTime) {
        return format(dateTime, CHINESE_SIMPLE_DATETIME_FORMAT);
    }

    /**
     * 按美式格式 (MM/dd/yyyy HH:mm:ss) 格式化 LocalDateTime
     */
    public static String formatAmerican(LocalDateTime dateTime) {
        return format(dateTime, AMERICAN_DATETIME_FORMAT);
    }

    /**
     * 将仅含日期的字符串解析为 LocalDateTime，时间默认为 00:00:00
     *
     * @param text    日期字符串（如 "2025-06-15"）
     * @param pattern 格式模板（如 "yyyy-MM-dd"）
     * @return LocalDateTime 对象，默认时间为当天 00:00:00
     */
    public static LocalDateTime parseDateOnly(String text, String pattern) {
        LocalDate localDate = LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern));
        return localDate.atStartOfDay(); // 默认时间 00:00:00
    }

    /**
     * 将 LocalDateTime 格式化为仅日期字符串（忽略时间部分）
     *
     * @param dateTime LocalDateTime 对象
     * @param pattern  输出格式（如 "yyyy-MM-dd"）
     * @return 仅日期的字符串
     */
    public static String formatDateOnly(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.toLocalDate().format(DateTimeFormatter.ofPattern(pattern));
    }

    // ====== 快捷方法示例 ======

    /**
     * 按 yyyy-MM-dd 格式解析日期字符串
     */
    public static LocalDateTime parseDateOnly(String text) {
        return parseDateOnly(text, DATE_ONLY_FORMAT);
    }

    /**
     * 按 yyyy-MM-dd 格式输出日期字符串
     */
    public static String formatDateOnly(LocalDateTime dateTime) {
        return formatDateOnly(dateTime, DATE_ONLY_FORMAT);
    }

    /**
     * 按中文日期格式 yyyy年MM月dd日 解析
     */
    public static LocalDateTime parseChineseDateOnly(String text) {
        return parseDateOnly(text, CHINESE_DATE_ONLY_FORMAT);
    }

    /**
     * 按中文日期格式 yyyy年MM月dd日 输出
     */
    public static String formatChineseDateOnly(LocalDateTime dateTime) {
        return formatDateOnly(dateTime, CHINESE_DATE_ONLY_FORMAT);
    }

    // Date 转换

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
    public static LocalDateTime fromTimestamp(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public static long toTimestamp(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
