/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-17 14:53
 * @Since:
 */
package com.zja.zfb;

import com.alipay.v3.ApiClient;
import com.alipay.v3.ApiException;
import com.alipay.v3.Configuration;
import com.alipay.v3.api.AlipayTradeApi;
import com.alipay.v3.model.AlipayTradePayModel;
import com.alipay.v3.model.AlipayTradePayResponseModel;
import com.alipay.v3.util.model.AlipayConfig;

/**
 * 普通调用示例
 *
 * @author: zhengja
 * @since: 2023/10/17 14:53
 */
public class Demo1 {
    public static void main(String[] args) throws ApiException {
        String URL = "https://openapi.alipay.com/gateway.do";
        String APPID = "";
        String PRIVATE_KEY = "";
        String ALIPAY_PUBLIC_KEY = "";

        ApiClient apiClient = Configuration.getDefaultApiClient();
        //设置网关地址
        apiClient.setBasePath(URL);
        //设置alipayConfig参数（全局设置一次）
        AlipayConfig alipayConfig = new AlipayConfig();
        //设置应用ID
        alipayConfig.setAppId(APPID);
        //设置应用私钥
        alipayConfig.setPrivateKey(PRIVATE_KEY);
        //设置支付宝公钥
        alipayConfig.setAlipayPublicKey(ALIPAY_PUBLIC_KEY);
        apiClient.setAlipayConfig(alipayConfig);

        //实例化客户端
        AlipayTradeApi api = new AlipayTradeApi();
        //调用 alipay.trade.pay
        AlipayTradePayModel alipayTradePayModel = new AlipayTradePayModel()
                .outTradeNo("20210817010101001")
                .totalAmount("0.01")
                .subject("测试商品")
                .scene("bar_code")
                .authCode("28763443825664394");
        //发起调用
        AlipayTradePayResponseModel response = api.pay(alipayTradePayModel);


    }
}
