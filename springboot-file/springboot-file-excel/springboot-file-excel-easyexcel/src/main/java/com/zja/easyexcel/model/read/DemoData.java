package com.zja.easyexcel.model.read;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 基础数据类.这里的排序和excel里面的排序一致
 *
 * @Author: zhengja
 * @Date: 2024-12-05 13:43
 */
@Getter
@Setter
@EqualsAndHashCode
public class DemoData {
    private String string;
    private Date date;
    private Double doubleData;
}
