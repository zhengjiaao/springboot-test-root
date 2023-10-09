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
// Context 类 - 订单
class Order {
    private OrderState currentState;

    public Order() {
        this.currentState = new PendingPaymentState(this);
    }

    public void setState(OrderState state) {
        this.currentState = state;
    }

    public void cancelOrder() {
        currentState.cancelOrder();
    }

    public void verifyPayment() {
        currentState.verifyPayment();
    }

    public void shipOrder() {
        currentState.shipOrder();
    }
}