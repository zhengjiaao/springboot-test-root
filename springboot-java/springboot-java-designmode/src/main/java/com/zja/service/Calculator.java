package com.zja.service;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-10-21 21:50
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class Calculator {

    public int calculate(Command command) {
        return command.execute();
    }
}
