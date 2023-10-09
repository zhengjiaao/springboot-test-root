/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 14:40
 * @Since:
 */
package com.zja.command.order;

/**
 * @author: zhengja
 * @since: 2023/10/09 14:40
 */
// Receiver - 订单服务
class OrderService {
    public void createOrder(Order order) {
        System.out.println("Order created: " + order.getId());
    }

    public void cancelOrder(Order order) {
        System.out.println("Order cancelled: " + order.getId());
    }

    public void payOrder(Order order) {
        System.out.println("Order paid: " + order.getId());
    }

    public void cancelPayment(Order order) {
        System.out.println("Payment cancelled for order: " + order.getId());
    }
}