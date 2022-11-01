/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-02-21 14:46
 * @Since:
 */
package com.zja.juc.future;

import java.util.concurrent.Callable;

public class CallableTask implements Callable {
    @Override
    public Object call() throws Exception {
        return 666;
    }
}
