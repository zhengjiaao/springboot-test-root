package com.zja.jdk.date;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * @Author: zhengja
 * @Date: 2025-05-29 17:59
 */
public class LocalDateTimeTest {

    @Test
    public void test() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
    }


    @Test
    public void toDate_test() {
        String dateTimeStr = "2025-05-21T16:00:00.000Z";

        // 使用 OffsetDateTime 解析带时区信息的字符串
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeStr);
        LocalDateTime localDateTime = offsetDateTime.toLocalDateTime();

        System.out.println(localDateTime);
        System.out.println(localDateTime.toLocalDate());

        // 转换为 Date
        Date date = Date.from(localDateTime.toInstant(ZoneOffset.UTC));
        System.out.println(date);

        // Thu May 22 00:00:00 CST 2025 转为 LocalDateTime
        LocalDateTime localDateTime1 = LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
        System.out.println(localDateTime1);


        // DateUtils.parseDate(String.valueOf(value));
    }
}
