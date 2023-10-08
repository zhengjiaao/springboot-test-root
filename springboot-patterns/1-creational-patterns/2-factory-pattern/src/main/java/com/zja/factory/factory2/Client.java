/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 9:32
 * @Since:
 */
package com.zja.factory.factory2;

/**
 * 客户端代码可以根据用户选择的商品类型，使用相应的订单处理器工厂创建订单处理器
 *
 * @author: zhengja
 * @since: 2023/10/08 9:32
 */
public class Client {
    public static void main(String[] args) {
        // 假设用户选择了服装商品类型
        OrderHandlerFactory factory = new ClothingOrderHandlerFactory();
        OrderHandler orderHandler = factory.createOrderHandler();
        orderHandler.handleOrder();  // 输出：处理服装订单

        // 假设用户选择了电子产品商品类型
        factory = new ElectronicsOrderHandlerFactory();
        orderHandler = factory.createOrderHandler();
        orderHandler.handleOrder();  // 输出：处理电子产品订单

        // 假设用户选择了家具商品类型
        factory = new FurnitureOrderHandlerFactory();
        orderHandler = factory.createOrderHandler();
        orderHandler.handleOrder();  // 输出：处理家具订单
    }
}
