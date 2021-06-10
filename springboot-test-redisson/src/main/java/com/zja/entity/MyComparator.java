package com.zja.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-02 13:41
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Data
public class MyComparator implements Serializable {
    private Integer a;

    public MyComparator(Integer a) {
        this.a = a;
    }
}
