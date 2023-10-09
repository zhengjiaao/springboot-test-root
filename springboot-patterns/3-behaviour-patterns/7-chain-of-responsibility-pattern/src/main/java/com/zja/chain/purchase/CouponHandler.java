/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:15
 * @Since:
 */
package com.zja.chain.purchase;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:15
 */
public class CouponHandler extends PurchaseHandler {
    @Override
    protected boolean canHandlePurchase(PurchaseRequest request) {
        return request.hasCoupon();
    }

    @Override
    protected void processPurchase(PurchaseRequest request) {
        System.out.println("Applying coupon to purchase.");
    }
}