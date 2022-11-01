package com.zja.service;

import com.zja.service.Impl.Addition;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-10-21 21:18
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
public class OperatorFactory {
    static Map<String, Operation> operationMap = new HashMap<>();
    static {
        operationMap.put("add", new Addition());
        //operationMap.put("divide", new Division());
        // more operators
    }

    public static Optional<Operation> getOperation(String operator) {
        return Optional.ofNullable(operationMap.get(operator));
    }
}
