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
// 具体咖啡类 - 浓缩咖啡
public class Espresso implements Coffee {
    public String getDescription() {
        return "Espresso";
    }

    public double getCost() {
        return 1.99;
    }
}