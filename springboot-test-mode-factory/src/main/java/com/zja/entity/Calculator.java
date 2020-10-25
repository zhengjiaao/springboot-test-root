package com.zja.entity;

import com.zja.constant.Operator;
import com.zja.service.Command;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-10-21 21:36
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class Calculator {

    //工厂模式
    public int calculate(int a, int b, Operator operator) {
        return operator.apply(a, b);
    }

    //命令模式
    public int calculate(Command command) {
        return command.execute();
    }

}
