/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 9:31
 * @Since:
 */
package com.zja.factory.factory2;

/**
 * @author: zhengja
 * @since: 2023/10/08 9:31
 */
public class ClothingOrderHandlerFactory extends OrderHandlerFactory {
    @Override
    public OrderHandler createOrderHandler() {
        return new ClothingOrderHandler();
    }
}
