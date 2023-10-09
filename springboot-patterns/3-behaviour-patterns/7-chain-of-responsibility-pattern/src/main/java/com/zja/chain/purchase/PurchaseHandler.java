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
public abstract class PurchaseHandler {
    private PurchaseHandler nextHandler;

    public void setNextHandler(PurchaseHandler handler) {
        this.nextHandler = handler;
    }

    public void handlePurchase(PurchaseRequest request) {
        if (canHandlePurchase(request)) {
            processPurchase(request);
        } else if (nextHandler != null) {
            nextHandler.handlePurchase(request);
        } else {
            System.out.println("Purchase cannot be processed.");
        }
    }

    protected abstract boolean canHandlePurchase(PurchaseRequest request);

    protected abstract void processPurchase(PurchaseRequest request);
}