/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 9:14
 * @Since:
 */
package com.zja.factory.simple1;

/**
 * @author: zhengja
 * @since: 2023/10/08 9:14
 */
// 客户端代码
public class Client {
    public static void main(String[] args) {
        Product productA = SimpleFactory.createProduct("A");
        productA.doSomething();  // 输出：ConcreteProductA doSomething

        Product productB = SimpleFactory.createProduct("B");
        productB.doSomething();  // 输出：ConcreteProductB doSomething
    }
}
