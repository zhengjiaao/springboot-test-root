/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 14:07
 * @Since:
 */
package com.zja.decorator.coffee2;

/**
 * @author: zhengja
 * @since: 2023/10/08 14:07
 */
// 抽象装饰者 - 调料
public abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;

    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    public String getDescription() {
        return coffee.getDescription();
    }

    public double getCost() {
        return coffee.getCost();
    }
}