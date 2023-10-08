/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 14:15
 * @Since:
 */
package com.zja.decorator.product;

/**
 * @author: zhengja
 * @since: 2023/10/08 14:15
 */
// 具体装饰者 - 打折促销
public class DiscountPromotionDecorator extends PromotionDecorator {
    private double discount;

    public DiscountPromotionDecorator(Product product, double discount) {
        super(product);
        this.discount = discount;
    }

    @Override
    public double getPrice() {
        double originalPrice = super.getPrice();
        return originalPrice - (originalPrice * discount);
    }
}
