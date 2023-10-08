/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 15:02
 * @Since:
 */
package com.zja.proxy.java.dynamic2;

import java.lang.reflect.Proxy;

/**
 * @author: zhengja
 * @since: 2023/10/08 15:02
 */
// 使用示例
public class Client {
    public static void main(String[] args) {
        Image realImage = new RealImage("image.jpg");
        Image proxyImage = (Image) Proxy.newProxyInstance( //通过调用 Proxy.newProxyInstance() 方法动态地创建代理对象，并将代理对象传递给处理器。
                realImage.getClass().getClassLoader(),
                realImage.getClass().getInterfaces(),
                new ImageProxyHandler(realImage));
        proxyImage.display(); //代理对象在调用方法时会自动调用处理器的 invoke() 方法，从而实现了动态代理的功能。

        //输出结果：
        //Loading image: image.jpg
        //Before method invocation
        //Displaying image: image.jpg
        //After method invocation
    }
}