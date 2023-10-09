/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 14:40
 * @Since:
 */
package com.zja.command.order;

/**
 * @author: zhengja
 * @since: 2023/10/09 14:40
 */
// Order - 订单类
class Order {
    private String id;

    public Order(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}