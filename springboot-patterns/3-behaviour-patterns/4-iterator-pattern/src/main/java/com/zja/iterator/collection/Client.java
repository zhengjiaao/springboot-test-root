/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 13:42
 * @Since:
 */
package com.zja.iterator.collection;

/**
 * @author: zhengja
 * @since: 2023/10/09 13:42
 */
// 示例代码
public class Client {
    public static void main(String[] args) {
        String[] elements = {"Apple", "Banana", "Orange"};
        Collection collection = new ConcreteCollection(elements);
        Iterator iterator = collection.createIterator();

        while (iterator.hasNext()) {
            Object element = iterator.next();
            System.out.println(element);
        }

        //输出结果：
        //Apple
        //Banana
        //Orange
    }
}
