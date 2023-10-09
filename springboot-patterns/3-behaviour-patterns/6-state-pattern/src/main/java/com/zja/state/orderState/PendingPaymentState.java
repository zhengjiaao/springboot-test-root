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
// Concrete State - 待支付状态
class PendingPaymentState implements OrderState {
    private Order order;

    public PendingPaymentState(Order order) {
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
        // 处理支付验证的逻辑
        System.out.println("订单支付已验证");
        order.setState(new PaymentVerifiedState(order));
    }

    @Override
    public void shipOrder() {
        // 待支付状态下不能发货
        System.out.println("订单尚未支付，无法发货");
    }
}
