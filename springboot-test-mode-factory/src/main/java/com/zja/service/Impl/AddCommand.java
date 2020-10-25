package com.zja.service.Impl;

import com.zja.service.Command;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-10-21 21:49
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class AddCommand implements Command {

    // Instance variables
    private int a;
    private int b;

    public AddCommand(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Integer execute() {
        return a + b;
    }
}
