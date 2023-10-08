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
// 具体咖啡类 - 卡布奇诺
public class Cappuccino implements Coffee {
    public String getDescription() {
        return "Cappuccino";
    }

    public double getCost() {
        return 2.99;
    }
}