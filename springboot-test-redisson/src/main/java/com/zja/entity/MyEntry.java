package com.zja.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-02 14:59
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
public class MyEntry implements Comparable<MyEntry>, Serializable {
    private String key;
    private Integer value;

    public MyEntry(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int compareTo(MyEntry o) {
        return key.compareTo(o.key);
    }
}
