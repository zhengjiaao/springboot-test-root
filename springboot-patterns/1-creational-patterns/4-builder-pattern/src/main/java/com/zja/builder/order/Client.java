/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:43
 * @Since:
 */
package com.zja.builder.order;

import java.util.ArrayList;
import java.util.List;

/**
 * 在客户端代码中，我们创建了一个指导者对象，并将特定的建造者对象传递给指导者。然后，通过指导者调用相应的方法来创建订单对象。最终，我们可以使用创建的订单对象进行后续操作。
 *
 * @author: zhengja
 * @since: 2023/10/08 10:43
 */
public class Client {
    public static void main(String[] args) {
        OrderBuilder standardOrderBuilder = new StandardOrderBuilder();
        OrderDirector director = new OrderDirector(standardOrderBuilder);

        List<String> items = new ArrayList<>();
        items.add("Item 1");
        items.add("Item 2");

        Order standardOrder = director.createOrder("12345", "John Doe", items);

        // 使用创建的订单对象
        System.out.println(standardOrder.getOrderNumber()); //12345
        System.out.println(standardOrder.getCustomerName()); //John Doe
        System.out.println(standardOrder.getItems()); //[Item 1, Item 2]
        // ...其他操作
    }
}
