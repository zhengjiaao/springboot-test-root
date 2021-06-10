package com.zja.entity;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-01 14:15
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class AnyObject implements Serializable {

    private Integer a;

    public AnyObject(Integer a) {
        this.a = a;
    }

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    @Override
    public String toString() {
        return "AnyObject{" +
                "a=" + a +
                '}';
    }
}
