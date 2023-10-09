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
 * 信用卡策略类
 *
 * @author: zhengja
 * @since: 2023/10/09 11:20
 */
// 具体策略类A
class CreditCardStrategy implements PaymentStrategy {
    private String cardNumber;
    private String cvv;

    public CreditCardStrategy(String cardNumber, String cvv) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " via credit card.");
        // 具体的支付逻辑
    }
}
