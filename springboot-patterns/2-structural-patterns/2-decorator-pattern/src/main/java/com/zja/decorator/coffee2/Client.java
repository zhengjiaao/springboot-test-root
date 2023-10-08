/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 14:08
 * @Since:
 */
package com.zja.decorator.coffee2;

/**
 * @author: zhengja
 * @since: 2023/10/08 14:08
 */
// 客户端代码
public class Client {
    public static void main(String[] args) {
        // 制作一杯浓缩咖啡
        Coffee espresso = new Espresso();
        System.out.println(espresso.getDescription() + " - $" + espresso.getCost());

        // 添加牛奶
        Coffee mochaLatte = new MilkDecorator(new MochaDecorator(espresso));
        System.out.println(mochaLatte.getDescription() + " - $" + mochaLatte.getCost());

        // 制作一杯卡布奇诺
        Coffee cappuccino = new Cappuccino();
        System.out.println(cappuccino.getDescription() + " - $" + cappuccino.getCost());

        // 添加牛奶和摩卡
        Coffee mochaCappuccino = new MochaDecorator(new MilkDecorator(cappuccino));
        System.out.println(mochaCappuccino.getDescription() + " - $" + mochaCappuccino.getCost());

        //输出结果：
        //Espresso - $1.99
        //Espresso, Mocha, Milk - $3.24
        //Cappuccino - $2.99
        //Cappuccino, Milk, Mocha - $4.24
    }
}
