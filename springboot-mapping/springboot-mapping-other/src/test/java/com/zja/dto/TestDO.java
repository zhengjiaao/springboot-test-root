package com.zja.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/11/27 17:23
 */
@Data
public class TestDO {
    private String name;
    private String age; // 类型不样
    private String number; // 字段名称
    private List<String> subjects;
    private Pes people;
    private List<Pes> pes; // 对象
    private Date createDate;
    private String updateDate; // 时间类型不一样
}
