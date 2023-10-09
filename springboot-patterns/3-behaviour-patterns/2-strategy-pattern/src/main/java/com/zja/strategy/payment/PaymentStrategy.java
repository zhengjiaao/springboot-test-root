/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 11:20
 * @Since:
 */
package com.zja.strategy.payment;

/**
 * 支付策略接口类
 *
 * @author: zhengja
 * @since: 2023/10/09 11:20
 */
// 策略接口
interface PaymentStrategy {
    void pay(double amount);
}
