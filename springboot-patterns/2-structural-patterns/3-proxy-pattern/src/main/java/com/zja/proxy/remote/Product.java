/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 14:47
 * @Since:
 */
package com.zja.proxy.remote;

/**
 * 商品接口 Product：该接口包含获取商品信息和购买商品的方法
 *
 * @author: zhengja
 * @since: 2023/10/08 14:47
 */
public interface Product {
    String getName();

    double getPrice();

    void buy();
}
