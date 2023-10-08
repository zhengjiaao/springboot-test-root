/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 11:24
 * @Since:
 */
package com.zja.prototype.deep;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/10/08 11:24
 */
public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
       ConcretePrototype prototype = new ConcretePrototype(10,new ReferenceType());
        ConcretePrototype clone = (ConcretePrototype) prototype.deepClone();

        // 修改克隆对象的值
        clone.setValue(20);

        System.out.println("原型对象的值：" + prototype.getValue()); // 原型对象的值：10
        System.out.println("克隆对象的值：" + clone.getValue()); // 克隆对象的值：20
    }
}
