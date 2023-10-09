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
 * PayPal策略
 *
 * @author: zhengja
 * @since: 2023/10/09 11:20
 */
// 具体策略类B
class PayPalStrategy implements PaymentStrategy {
    private String email;
    private String password;

    public PayPalStrategy(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " via PayPal.");
        // 具体的支付逻辑
    }
}