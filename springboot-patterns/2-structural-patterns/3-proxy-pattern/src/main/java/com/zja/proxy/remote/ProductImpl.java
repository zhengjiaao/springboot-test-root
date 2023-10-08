/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 14:48
 * @Since:
 */
package com.zja.proxy.remote;

/**
 * @author: zhengja
 * @since: 2023/10/08 14:48
 */
public class ProductImpl implements Product {
    private String name;
    private double price;

    public ProductImpl(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public void buy() {
        System.out.println("购买商品：" + name);
    }
}
