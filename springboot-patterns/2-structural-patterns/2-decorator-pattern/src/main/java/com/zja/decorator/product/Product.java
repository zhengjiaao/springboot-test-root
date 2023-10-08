/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 14:14
 * @Since:
 */
package com.zja.decorator.product;

/**
 * @author: zhengja
 * @since: 2023/10/08 14:14
 */
// 核心组件接口 - 商品
public interface Product {
    String getDescription();
    double getPrice();
}
