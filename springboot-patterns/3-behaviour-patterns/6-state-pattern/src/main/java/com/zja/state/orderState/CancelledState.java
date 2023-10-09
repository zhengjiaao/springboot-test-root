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
// Concrete State - 已取消状态
class CancelledState implements OrderState {
    private Order order;

    public CancelledState(Order order) {
        this.order = order;
    }

    @Override
    public void cancelOrder() {
        // 已取消状态下无需重复取消订单
        System.out.println("订单已取消");
    }

    @Override
    public void verifyPayment() {
        // 已取消状态下无需验证支付
        System.out.println("订单已取消");
    }

    @Override
    public void shipOrder() {
        // 已取消状态下无法发货
        System.out.println("订单已取消");
    }
}
