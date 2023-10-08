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
// 抽象建造者
interface OrderBuilder {
    void setOrderNumber(String orderNumber);

    void setCustomerName(String customerName);

    void setItems(List<String> items);

    void setShippingAddress(String shippingAddress);

    void setCouponCode(String couponCode);

    Order build();
}
