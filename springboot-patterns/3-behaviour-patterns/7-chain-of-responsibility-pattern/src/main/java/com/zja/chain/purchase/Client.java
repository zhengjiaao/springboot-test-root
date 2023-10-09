/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:16
 * @Since:
 */
package com.zja.chain.purchase;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:16
 */
public class Client {
    public static void main(String[] args) {
        // 创建处理者对象
        PurchaseHandler couponHandler = new CouponHandler();
        PurchaseHandler discountHandler = new DiscountHandler();
        PurchaseHandler freeShippingHandler = new FreeShippingHandler();

        // 设置处理者的顺序
        couponHandler.setNextHandler(discountHandler);
        discountHandler.setNextHandler(freeShippingHandler);

        // 创建购买请求
        PurchaseRequest purchaseRequest = new PurchaseRequest(150.0, true);

        // 处理购买请求
        couponHandler.handlePurchase(purchaseRequest);

        //输出结果：
        //Applying coupon to purchase.
    }
}
