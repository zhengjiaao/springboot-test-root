package com.zja.easyexcel.model.web;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 基础数据类
 * @Author: zhengja
 * @Date: 2024-12-05 14:16
 */
@Getter
@Setter
@EqualsAndHashCode
public class DownloadData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题")
    private Date date;
    @ExcelProperty("数字标题")
    private Double doubleData;
}