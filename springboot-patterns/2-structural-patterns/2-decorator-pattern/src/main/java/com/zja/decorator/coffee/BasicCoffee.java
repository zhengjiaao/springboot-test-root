/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 13:57
 * @Since:
 */
package com.zja.decorator.coffee;

/**
 * @author: zhengja
 * @since: 2023/10/08 13:57
 */
// 基本咖啡类（核心组件的具体实现）
public class BasicCoffee implements Coffee {
    @Override
    public String getDescription() {
        return "Basic Coffee";
    }

    @Override
    public double getCost() {
        return 2.0;
    }
}
