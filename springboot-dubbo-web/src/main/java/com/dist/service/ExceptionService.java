package com.dist.service;

import com.dist.util.exception.UnRecordedException;
import org.springframework.stereotype.Component;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-05-19 9:39
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Component
public class ExceptionService {

    public Object exceptionTest(boolean result) throws Exception {
        if (!result){
            throw new UnRecordedException("用户不存在");
        }
        return result;
    }
}
