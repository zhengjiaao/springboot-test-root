package com.zja.service.Impl;

import com.zja.service.Operation;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-10-21 21:18
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class Addition implements Operation {
    @Override
    public int apply(int a, int b) {
        return a + b;
    }
}
