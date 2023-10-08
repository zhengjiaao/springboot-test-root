/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:42
 * @Since:
 */
package com.zja.builder.order;

import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/10/08 10:42
 */
// 具体建造者
class StandardOrderBuilder implements OrderBuilder {
    private Order order;

    public StandardOrderBuilder() {
        order = new Order(null, null, null);
    }

    @Override
    public void setOrderNumber(String orderNumber) {
        order.setOrderNumber(orderNumber);
    }

    @Override
    public void setCustomerName(String customerName) {
        order.setCustomerName(customerName);
    }

    @Override
    public void setItems(List<String> items) {
        order.setItems(items);
    }

    @Override
    public void setShippingAddress(String shippingAddress) {
        order.setShippingAddress(shippingAddress);
    }

    @Override
    public void setCouponCode(String couponCode) {
        order.setCouponCode(couponCode);
    }

    @Override
    public Order build() {
        return order;
    }
}