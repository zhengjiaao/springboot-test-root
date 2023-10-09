/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 16:16
 * @Since:
 */
package com.zja.chain.purchase;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: zhengja
 * @since: 2023/10/09 16:16
 */
@Data
public class PurchaseRequest implements Serializable {
    private Double amount;
    private boolean coupon;

    public Double getAmount() {
        return amount;
    }

    public boolean hasCoupon() {
        return coupon;
    }

    public PurchaseRequest(Double amount, boolean hasCoupon) {
        this.amount = amount;
        this.coupon = hasCoupon;
    }
}
