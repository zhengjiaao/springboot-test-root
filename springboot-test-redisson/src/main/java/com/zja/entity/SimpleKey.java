package com.zja.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-02 11:31
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
public class SimpleKey implements Serializable {
    private String key;

    public SimpleKey(String key) {
        this.key = key;
    }
}
