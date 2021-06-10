package com.zja.service.impl;

import com.zja.service.SomeServiceInterface;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-03 13:47
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class SomeServiceImpl implements SomeServiceInterface {

    @Override
    public String doSomeStuff(String str) {

        if (str.equals("a")){
            return "is a";
        }
        return "not is a";
    }
}
