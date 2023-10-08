/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 10:14
 * @Since:
 */
package com.zja.abstract1.oscar;

/**
 * 客户端代码根据市场需求选择合适的汽车工厂，并使用工厂创建对应的汽车及其组件
 *
 * @author: zhengja
 * @since: 2023/10/08 10:14
 */
public class Client {
    public static void main(String[] args) {
        String market = "Economy";  // 假设当前市场需求为经济型汽车

        CarFactory factory;
        if (market.equals("Economy")) {
            factory = new EconomyCarFactory();
        } else {
            factory = new LuxuryCarFactory();
        }

        Car car = factory.createCar();
        Engine engine = factory.createEngine();
        Tire tire = factory.createTire();
        Seat seat = factory.createSeat();

        car.assemble();  // 输出：组装经济型汽车
        engine.design();  // 输出：设计经济型引擎
        tire.produce();  // 输出：生产经济型轮胎
        seat.install();  // 输出：安装经济型座椅
    }
}
