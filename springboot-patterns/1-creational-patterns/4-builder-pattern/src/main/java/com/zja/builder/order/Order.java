/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:41
 * @Since:
 */
package com.zja.builder.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/10/08 10:41
 */
// 产品类 - 订单
@Getter
@Setter
class Order {
    private String orderNumber;
    private String customerName;
    private List<String> items;
    private String shippingAddress;
    private String couponCode;
    // ...其他属性和方法

    public Order(String orderNumber, String customerName, List<String> items) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.items = items;
    }

    // ...其他属性的设置方法
}