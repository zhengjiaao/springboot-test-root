package com.zja.easyexcel.model.fill;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 填充数据
 *
 * @Author: zhengja
 * @Date: 2024-12-05 14:21
 */
@Getter
@Setter
@EqualsAndHashCode
public class FillData {
    private String name;
    private double number;
    private Date date;
}