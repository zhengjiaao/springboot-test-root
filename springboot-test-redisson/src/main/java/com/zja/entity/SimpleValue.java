package com.zja.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-02 11:32
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
public class SimpleValue implements Serializable {
    private String value;

    public SimpleValue(String value) {
        this.value = value;
    }
}
