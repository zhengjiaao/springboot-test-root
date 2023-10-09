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
// Concrete Command - 支付订单命令
class PayOrderCommand implements Command {
    private OrderService orderService;
    private Order order;

    public PayOrderCommand(OrderService orderService, Order order) {
        this.orderService = orderService;
        this.order = order;
    }

    @Override
    public void execute() {
        orderService.payOrder(order);
    }

    @Override
    public void undo() {
        orderService.cancelPayment(order);
    }
}
