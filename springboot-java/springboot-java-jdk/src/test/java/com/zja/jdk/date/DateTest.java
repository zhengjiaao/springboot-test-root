package com.zja.jdk.date;

import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: zhengja
 * @Date: 2024-12-31 16:11
 */
public class DateTest {

    @Test
    public void test() {
        System.out.println("时间戳" + System.currentTimeMillis());

        // 分别输出，年、月、日
        // 使用 LocalDateTime 获取当前日期时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println("当前时间: " + now);
        System.out.println("年: " + now.getYear());
        System.out.println("月: " + now.getMonthValue());
        System.out.println("日: " + now.getDayOfMonth());
        System.out.println("当前日期时间: " + now);

        System.out.println("---------------------");

        // 如果需要考虑时区，可以使用 ZonedDateTime
        ZonedDateTime zonedNow = ZonedDateTime.now();
        System.out.println("带时区的年: " + zonedNow.getYear());
        System.out.println("带时区的月: " + zonedNow.getMonthValue());
        System.out.println("带时区的日: " + zonedNow.getDayOfMonth());
        System.out.println("带时区的日期时间: " + zonedNow);
    }

    @Test
    public void test2() {

        String dateStr1 = "2024-12-31 16:11:00";
        String dateStr2 = "2024-12-31";

        // string 转 date

        Date date = DateUtil.parse(dateStr1);
        System.out.println(date);
        Date date2 = DateUtil.parse(dateStr2);
        System.out.println(date2);

        System.out.println("-----------------");

        // string 转 LocalDateTime
        // 定义日期时间格式化器
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // string 转 LocalDateTime
        LocalDateTime dateTime = LocalDateTime.parse(dateStr1, formatter1);
        System.out.println(dateTime);

        // string 转 LocalDate（因为 dateStr2 只有日期部分）
        LocalDateTime dateTime2 = LocalDateTime.parse(dateStr2 + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println(dateTime2);

        LocalDateTime dateTime3 = LocalDateTime.parse(dateStr2 + "T00:00:00");
        System.out.println(dateTime3);

    }

    @Test
    public void test3() {
        // 测试 Date 格式化
        Date date = new Date();
        System.out.println(DateUtil.formatAsDatetime(date));

        System.out.println("-----------------");

        System.out.println(DateUtil.formatAsDatetimeWithMs(date));

        System.out.println("-----------------");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
}
