package com.zja.java.lang.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 通用的日志记录代理,可以应用于任意接口的实现类
 * <p>
 *
 * </p>
 *
 * @Author: zhengja
 * @Date: 2024-05-28 10:47
 */
public class DynamicProxyLogTest {

    // 接口
    public interface UserService {
        void createUser(String name);

        void deleteUser(String name);

        List<String> listUsers();
    }

    // 实现类
    public static class UserServiceImpl implements UserService {
        private List<String> users = new ArrayList<>();

        @Override
        public void createUser(String name) {
            users.add(name);
            System.out.println("Created user: " + name);
        }

        @Override
        public void deleteUser(String name) {
            users.remove(name);
            System.out.println("Deleted user: " + name);
        }

        @Override
        public List<String> listUsers() {
            return users;
        }
    }

    // 通用的日志记录动态代理
    public static class LoggingProxy implements InvocationHandler {
        private final Object target;

        public LoggingProxy(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 记录方法调用前的信息
            System.out.println("Calling method: " + method.getName());
            System.out.println("Arguments: " + Arrays.toString(args));

            // 调用目标方法
            Object result = method.invoke(target, args);

            // 记录方法调用后的信息
            System.out.println("Method returned: " + result);

            return result;
        }

        public static <T> T createLoggingProxy(Class<T> interfaceClass, T implementation) {
            return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new LoggingProxy(implementation));
        }
    }

    public static void main(String[] args) {
        UserService userService = LoggingProxy.createLoggingProxy(UserService.class, new UserServiceImpl());

        userService.createUser("John");
        userService.createUser("Jane");
        List<String> users = userService.listUsers();
        System.out.println("Users: " + users);
        userService.deleteUser("John");
    }

    // 输出结果：

    // Calling method: createUser
    // Arguments: [John]
    // Created user: John
    // Calling method: createUser
    // Arguments: [Jane]
    // Created user: Jane
    // Calling method: listUsers
    // Arguments: []
    // Method returned: [John, Jane]
    // Users: [John, Jane]
    // Calling method: deleteUser
    // Arguments: [John]
    // Deleted user: John

}
