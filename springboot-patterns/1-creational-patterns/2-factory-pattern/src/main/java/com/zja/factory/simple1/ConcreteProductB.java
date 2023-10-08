/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 9:13
 * @Since:
 */
package com.zja.factory.simple1;

/**
 * @author: zhengja
 * @since: 2023/10/08 9:13
 */
// 具体产品B
class ConcreteProductB implements Product {
    @Override
    public void doSomething() {
        System.out.println("ConcreteProductB doSomething");
    }
}