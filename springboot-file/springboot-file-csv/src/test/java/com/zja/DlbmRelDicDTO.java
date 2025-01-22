package com.zja;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zhengja
 * @Date: 2025-01-21 17:47
 */
@Data
public class DlbmRelDicDTO implements Serializable {
    @CsvBindByName(column = "一类编码")
    private String level1Code;
    @CsvBindByName(column = "一类名称")
    private String level1Name;
    @CsvBindByName(column = "地类编码")
    private String level2Code;
    @CsvBindByName(column = "地类名称")
    private String level2Name;
}
