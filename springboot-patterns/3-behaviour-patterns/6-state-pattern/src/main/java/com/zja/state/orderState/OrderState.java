/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 15:16
 * @Since:
 */
package com.zja.state.orderState;

/**
 * @author: zhengja
 * @since: 2023/10/09 15:16
 */
// OrderState 接口
interface OrderState {
    void cancelOrder(); //取消订单
    void verifyPayment(); //支付验证
    void shipOrder(); //订单已发货
}
