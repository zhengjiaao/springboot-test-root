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
 * 远程代理类 RemoteProductProxy，该类实现了 Product 接口，并通过网络与远程服务器进行通信
 *
 * @author: zhengja
 * @since: 2023/10/08 14:48
 */
public class RemoteProductProxy implements Product {
    private String serverUrl;

    public RemoteProductProxy(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public String getName() {
        // 发送远程调用请求获取商品名称
        String request = "GET /product/name";
        String response = sendRequest(request);
        return response;
    }

    @Override
    public double getPrice() {
        // 发送远程调用请求获取商品价格
        String request = "GET /product/price";
        String response = sendRequest(request);
//        return Double.parseDouble(response);
        return 22;
    }

    @Override
    public void buy() {
        // 发送远程调用请求购买商品
        String request = "POST /product/buy";
        sendRequest(request);
    }

    private String sendRequest(String request) {
        // 创建网络连接并发送请求到远程服务器
        // ...
        // 接收并解析远程服务器的响应
        // ...
        return "Response from server";
    }
}