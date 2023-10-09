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
// 使用示例
public class Client {
    public static void main(String[] args) {
        Order order = new Order();
        order.verifyPayment(); // 输出：订单支付已验证
        order.cancelOrder(); // 输出：订单已取消
        order.verifyPayment(); // 输出：订单已取消
        order.shipOrder(); // 输出：订单已取消

        // 切换订单状态为支付验证通过状态
        order.setState(new ShippedState(order));

        order.verifyPayment(); // 输出：订单支付已验证
        order.shipOrder(); // 输出：订单已发货

        //输出结果：
        //订单支付已验证
        //订单已取消
        //订单已取消
        //订单已取消
        //订单支付已验证
        //订单已发货
    }
}
