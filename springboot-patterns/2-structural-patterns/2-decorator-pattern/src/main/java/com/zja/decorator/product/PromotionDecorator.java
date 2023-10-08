/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 14:14
 * @Since:
 */
package com.zja.decorator.product;

/**
 * @author: zhengja
 * @since: 2023/10/08 14:14
 */
// 抽象装饰者 - 促销
public abstract class PromotionDecorator implements Product {
    protected Product product;

    public PromotionDecorator(Product product) {
        this.product = product;
    }

    @Override
    public String getDescription() {
        return product.getDescription();
    }

    @Override
    public double getPrice() {
        return product.getPrice();
    }
}
