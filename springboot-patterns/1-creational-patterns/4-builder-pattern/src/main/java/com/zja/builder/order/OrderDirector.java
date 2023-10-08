/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:43
 * @Since:
 */
package com.zja.builder.order;

import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/10/08 10:43
 */
// 指导者
class OrderDirector {
    private OrderBuilder builder;

    public OrderDirector(OrderBuilder builder) {
        this.builder = builder;
    }

    public Order createOrder(String orderNumber, String customerName, List<String> items) {
        builder.setOrderNumber(orderNumber);
        builder.setCustomerName(customerName);
        builder.setItems(items);
        return builder.build();
    }
}
