/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 13:59
 * @Since:
 */
package com.zja.decorator.coffee;

/**
 * @author: zhengja
 * @since: 2023/10/08 13:59
 */
// 客户端代码
public class Client {
    public static void main(String[] args) {
        // 创建基本咖啡对象
        Coffee basicCoffee = new BasicCoffee();
        System.out.println(basicCoffee.getDescription() + " - $" + basicCoffee.getCost()); // Basic Coffee - $2.0

        // 使用装饰者动态添加调料
        Coffee milkCoffee = new MilkDecorator(basicCoffee);
        System.out.println(milkCoffee.getDescription() + " - $" + milkCoffee.getCost()); // Basic Coffee, Milk - $2.5

        Coffee sugarMilkCoffee = new SugarDecorator(milkCoffee);
        System.out.println(sugarMilkCoffee.getDescription() + " - $" + sugarMilkCoffee.getCost()); // Basic Coffee, Milk, Sugar - $2.8
    }
}
