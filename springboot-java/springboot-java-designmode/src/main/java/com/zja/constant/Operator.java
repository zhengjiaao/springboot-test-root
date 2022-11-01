package com.zja.constant;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-10-21 21:20
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：常量
 */
public enum Operator {


    ADD {
        @Override
        public int apply(int a, int b) {
            return a + b;
        }
    },
    MULTIPLY {
        @Override
        public int apply(int a, int b) {
            return a * b;
        }
    };
    // other operators

    public abstract int apply(int a, int b);

}
