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
// 具体装饰者 - 满减促销
public class DiscountedPricePromotionDecorator extends PromotionDecorator {
    private double threshold;
    private double discountAmount;

    public DiscountedPricePromotionDecorator(Product product, double threshold, double discountAmount) {
        super(product);
        this.threshold = threshold;
        this.discountAmount = discountAmount;
    }

    @Override
    public double getPrice() {
        double originalPrice = super.getPrice();
        if (originalPrice >= threshold) {
            return originalPrice - discountAmount;
        } else {
            return originalPrice;
        }
    }
}