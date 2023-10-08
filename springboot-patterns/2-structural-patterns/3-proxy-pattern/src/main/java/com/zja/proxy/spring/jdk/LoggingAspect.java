/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 15:14
 * @Since:
 */
package com.zja.proxy.spring.jdk;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author: zhengja
 * @since: 2023/10/08 15:14
 */
// 切面类
class LoggingAspect implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Before method execution");
        Object result = invocation.proceed();
        System.out.println("After method execution");
        return result;
    }
}