/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-28 14:23
 * @Since:
 */
package com.zja.util;

import cn.hutool.core.date.DateUtil;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author: zhengja
 * @since: 2023/07/28 14:23
 */
public class JxlsUtils {


    /**
     * if判断
     * ${utils:ifelse(b, o1, o2)} 单元格内
     * utils:ifelse(b, o1, o2) 批注内
     *
     * @param b
     * @param o1
     * @param o2
     * @return
     */
    public Object ifElse(boolean b, Object o1, Object o2) {
        return b ? o1 : o2;
    }

    /**
     * 日期转换
     * ${utils:dateConvert(dateTime, dateTimeFormatter)} 单元格内
     * utils:dateConvert(dateTime, dateTimeFormatter) 批注内
     *
     * @param dateTime          日期时间，示例 {@link Date} or {@link LocalDateTime}
     * @param dateTimeFormatter 时间格式，示例 yyyy-MM-dd or yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String dateConvert(Object dateTime, Object dateTimeFormatter) {
        if (ObjectUtils.isEmpty(dateTime)) {
            return null;
        }

        if (ObjectUtils.isEmpty(dateTimeFormatter)) {
            throw new RuntimeException("转换日期失败: dateTime=" + dateTime + " ,dateTimeFormatter is not is null." + dateTimeFormatter);
        }

        String pattern = (String) dateTimeFormatter;

        if (dateTime instanceof Date) {
            //Date 时间
            Date date = (Date) dateTime;
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }

        if (dateTime instanceof LocalDateTime) {
            //java8 时间格式
            LocalDateTime localDate = (LocalDateTime) dateTime;
            return localDate.format(DateTimeFormatter.ofPattern(pattern));
        }

        if (dateTime instanceof Long) {
            //时间戳
            Long timeStamp = (Long) dateTime;
            Instant instant = Instant.ofEpochMilli(timeStamp);
            Date date = Date.from(instant);
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }

        if (dateTime instanceof String) {
            //字符串时间,使用java8 时间格式
            String dateStr = (String) dateTime;
            Date date = DateUtil.parse(dateStr);
            return DateUtil.format(date, pattern);
        }

        throw new RuntimeException("转换日期失败: dateTime=" + dateTime + " ,dateTimeFormatter=" + dateTimeFormatter);
    }

}
