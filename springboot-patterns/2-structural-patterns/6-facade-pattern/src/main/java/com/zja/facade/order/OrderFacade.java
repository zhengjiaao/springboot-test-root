/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 17:38
 * @Since:
 */
package com.zja.facade.order;

import com.zja.facade.order.service.InventoryService;
import com.zja.facade.order.service.OrderService;
import com.zja.facade.order.service.PaymentService;

/**
 * @author: zhengja
 * @since: 2023/10/08 17:38
 */
public class OrderFacade {
    private InventoryService inventoryService;
    private OrderService orderService;
    private PaymentService paymentService;

    public OrderFacade() {
        inventoryService = new InventoryService();
        orderService = new OrderService();
        paymentService = new PaymentService();
    }

    public void placeOrder(String productId, int quantity, String paymentMethod) {
        if (inventoryService.checkStock(productId, quantity)) {
            Order order = orderService.createOrder(productId, quantity);
            paymentService.processPayment(order, paymentMethod);
            System.out.println("Order placed successfully.");
        } else {
            System.out.println("Insufficient stock.");
        }
    }
}
