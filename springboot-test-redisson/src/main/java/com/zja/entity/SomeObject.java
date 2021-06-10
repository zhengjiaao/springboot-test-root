package com.zja.entity;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-01 17:32
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class SomeObject implements Serializable {

    private String a;
    private String b;

    public SomeObject(String a) {
        this.a = a;
    }

    public SomeObject(String a, String b) {
        this.a = a;
        this.b = b;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "SomeObject{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                '}';
    }
}
