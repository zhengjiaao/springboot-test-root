/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 17:39
 * @Since:
 */
package com.zja.facade.order;

import lombok.Data;

/**
 * @author: zhengja
 * @since: 2023/10/08 17:39
 */
@Data
public class Order {
    private String orderId; //订单名称
    private String productId; //产品id
    private String paymentMethod; //付款方式
    private int quantity; //订单数量
}
