package com.zja.easyexcel.dao;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 基础数据类
 *
 * @Author: zhengja
 * @Date: 2024-12-05 14:18
 */
@Getter
@Setter
@EqualsAndHashCode
public class UploadData {
    private String string;
    private Date date;
    private Double doubleData;
}