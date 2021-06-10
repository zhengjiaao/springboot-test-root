package com.zja.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-02 9:44
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
public class MyKey implements Serializable {
    private String key;

    public MyKey(String key) {
        this.key = key;
    }
}
