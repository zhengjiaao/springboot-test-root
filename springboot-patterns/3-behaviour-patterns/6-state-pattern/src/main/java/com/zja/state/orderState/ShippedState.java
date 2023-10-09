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
// Concrete State - 已发货状态
class ShippedState implements OrderState {
    private Order order;

    public ShippedState(Order order) {
        this.order = order;
    }

    @Override
    public void cancelOrder() {
        // 已发货状态下不能取消订单
        System.out.println("订单已发货，无法取消");
    }

    @Override
    public void verifyPayment() {
        // 已发货状态下无需重新验证支付
        System.out.println("订单支付已验证");
    }

    @Override
    public void shipOrder() {
        // 已发货状态下无需重新发货
        System.out.println("订单已发货");
    }
}
