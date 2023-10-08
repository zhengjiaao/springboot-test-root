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
 * 在客户端中使用远程代理对象进行远程商品操作
 *
 * @author: zhengja
 * @since: 2023/10/08 14:48
 */
public class Client {
    public static void main(String[] args) {
        Product product = new RemoteProductProxy("http://example.com");
        System.out.println("Product Name: " + product.getName());
        System.out.println("Product Price: " + product.getPrice());
        product.buy();
    }
}