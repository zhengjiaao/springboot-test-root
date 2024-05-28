package com.zja.java.lang.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理: 动态代理是 Java 反射机制的一个强大应用,它可以用于实现接口代理、方法拦截、功能扩展等多种场景,是构建基于面向切面编程(AOP)、插件化等设计模式的关键技术之一。
 * </p>
 * 定义了一个 HelloService 接口和它的实现类 HelloServiceImpl。
 * 然后,我们创建了一个 HelloServiceProxy 类,它实现了 InvocationHandler 接口,用于拦截 HelloService 对象的方法调用。
 * 最后,我们使用 Proxy.newProxyInstance() 方法动态生成了一个 HelloService 的代理对象,并调用了 sayHello() 方法。
 *
 * @Author: zhengja
 * @Date: 2024-05-28 10:41
 */
public class DynamicProxyTest {

    // 定义接口
    interface HelloService {
        void sayHello();
    }

    // 实现类
    static class HelloServiceImpl implements HelloService {
        public void sayHello() {
            System.out.println("Hello from HelloServiceImpl!");
        }
    }

    // 动态代理类
    static class HelloServiceProxy implements InvocationHandler {
        private Object target;

        public HelloServiceProxy(Object target) {
            this.target = target;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("Before method call: " + method.getName());
            Object result = method.invoke(target, args);
            System.out.println("After method call: " + method.getName());
            return result;
        }
    }

    // 使用动态代理
    public static void main(String[] args) {
        HelloService helloService = (HelloService) Proxy.newProxyInstance(HelloService.class.getClassLoader(), new Class[]{HelloService.class}, new HelloServiceProxy(new HelloServiceImpl()));

        helloService.sayHello(); // 输出: Before method call: sayHello, Hello from HelloServiceImpl!, After method call: sayHello
    }
}
