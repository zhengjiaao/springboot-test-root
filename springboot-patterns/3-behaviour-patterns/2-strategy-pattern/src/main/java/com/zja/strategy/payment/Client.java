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
 * @author: zhengja
 * @since: 2023/10/09 11:20
 */
// 示例代码
public class Client {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();

        // 使用信用卡策略进行支付
        PaymentStrategy creditCardStrategy = new CreditCardStrategy("123456789", "123");
        cart.setPaymentStrategy(creditCardStrategy);
        cart.checkout(100.0);

        // 使用PayPal策略进行支付
        PaymentStrategy payPalStrategy = new PayPalStrategy("example@example.com", "password");
        cart.setPaymentStrategy(payPalStrategy);
        cart.checkout(200.0);

        //输出结果：
        //Paid 100.0 via credit card.
        //Paid 200.0 via PayPal.
    }
}
