/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 15:19
 * @Since:
 */
package com.zja.proxy.spring.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author: zhengja
 * @since: 2023/10/08 15:19
 */
// 切面类
class LoggingAspect implements MethodInterceptor {
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("Before method execution");
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("After method execution");
        return result;
    }
}
