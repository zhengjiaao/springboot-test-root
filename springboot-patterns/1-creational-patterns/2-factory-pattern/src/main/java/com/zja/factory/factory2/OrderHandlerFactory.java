/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 9:30
 * @Since:
 */
package com.zja.factory.factory2;

/**
 * 抽象工厂类，用于创建订单处理器
 *
 * @author: zhengja
 * @since: 2023/10/08 9:30
 */
public abstract class OrderHandlerFactory {
    public abstract OrderHandler createOrderHandler();
}
