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
// Concrete Command - 取消订单命令
class CancelOrderCommand implements Command {
    private OrderService orderService;
    private Order order;

    public CancelOrderCommand(OrderService orderService, Order order) {
        this.orderService = orderService;
        this.order = order;
    }

    @Override
    public void execute() {
        orderService.cancelOrder(order);
    }

    @Override
    public void undo() {
        orderService.createOrder(order);
    }
}
