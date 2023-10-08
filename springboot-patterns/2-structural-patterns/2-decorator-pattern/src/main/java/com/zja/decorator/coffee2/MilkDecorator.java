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
// 具体装饰者 - 牛奶调料
public class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return coffee.getDescription() + ", Milk";
    }

    public double getCost() {
        return coffee.getCost() + 0.5;
    }
}
