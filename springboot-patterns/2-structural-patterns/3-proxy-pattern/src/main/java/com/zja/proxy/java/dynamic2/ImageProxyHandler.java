/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 15:02
 * @Since:
 */
package com.zja.proxy.java.dynamic2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author: zhengja
 * @since: 2023/10/08 15:02
 */
// 动态代理处理器
class ImageProxyHandler implements InvocationHandler {
    private Object realObject;

    public ImageProxyHandler(Object realObject) {
        this.realObject = realObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        System.out.println("Before method invocation");
        result = method.invoke(realObject, args);
        System.out.println("After method invocation");
        return result;
    }
}
