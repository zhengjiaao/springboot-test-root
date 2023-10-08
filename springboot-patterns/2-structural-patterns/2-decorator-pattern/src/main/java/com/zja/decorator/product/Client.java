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
// 客户端代码
public class Client {
    public static void main(String[] args) {
        // 创建一个衣服商品
        Product clothing = new Clothing("T-shirt", 20.0);
        System.out.println(clothing.getDescription() + " - $" + clothing.getPrice());

        // 添加打折促销
        Product discountedClothing = new DiscountPromotionDecorator(clothing, 0.2);
        System.out.println(discountedClothing.getDescription() + " - $" + discountedClothing.getPrice());

        // 添加满减促销
        Product discountedPriceClothing = new DiscountedPricePromotionDecorator(clothing, 30.0, 5.0);
        System.out.println(discountedPriceClothing.getDescription() + " - $" + discountedPriceClothing.getPrice());

        //输出结果：
        //T-shirt - $20.0
        //T-shirt - $16.0
        //T-shirt - $20.0
    }
}