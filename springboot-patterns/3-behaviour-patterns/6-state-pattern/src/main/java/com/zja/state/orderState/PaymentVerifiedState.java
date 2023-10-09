/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 15:17
 * @Since:
 */
package com.zja.state.orderState;

/**
 * @author: zhengja
 * @since: 2023/10/09 15:17
 */
// Concrete State - 支付验证通过状态
class PaymentVerifiedState implements OrderState {
    private Order order;

    public PaymentVerifiedState(Order order) {
        this.order = order;
    }

    @Override
    public void cancelOrder() {
        // 处理取消订单的逻辑
        System.out.println("订单已取消");
        order.setState(new CancelledState(order));
    }

    @Override
    public void verifyPayment() {
        // 支付验证通过状态下无需重新验证支付
        System.out.println("订单支付已验证");
    }

    @Override
    public void shipOrder() {
        // 处理发货的逻辑
        System.out.println("订单已发货");
        order.setState(new ShippedState(order));
    }
}