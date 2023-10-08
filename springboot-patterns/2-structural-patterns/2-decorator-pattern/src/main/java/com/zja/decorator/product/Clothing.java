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
// 具体商品类 - 衣服
public class Clothing implements Product {
    private String description;
    private double price;

    public Clothing(String description, double price) {
        this.description = description;
        this.price = price;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getPrice() {
        return price;
    }
}