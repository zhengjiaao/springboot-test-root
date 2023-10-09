/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 14:03
 * @Since:
 */
package com.zja.iterator.MyCollection;

import java.util.Iterator;

/**
 * @author: zhengja
 * @since: 2023/10/09 14:03
 */
// 示例代码
public class Client {
    public static void main(String[] args) {
        MyCollection collection = new MyCollection(5);
        collection.add("Apple");
        collection.add("Banana");
        collection.add("Orange");

        // 使用迭代器遍历集合
        Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            System.out.println(element);
        }
    }

    //输出结果：
    //Apple
    //Banana
    //Orange
}
