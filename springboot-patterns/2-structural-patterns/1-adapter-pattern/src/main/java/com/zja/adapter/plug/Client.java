/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 13:20
 * @Since:
 */
package com.zja.adapter.plug;

/**
 * @author: zhengja
 * @since: 2023/10/08 13:20
 */
public class Client {
    public static void main(String[] args) {
        //使用适配器将英国插头适配到中国插座，并进行插拔操作
        UKPlug ukPlug = new UKPlug();
        ChinaSocket chinaSocket = new UKPlugAdapter(ukPlug);

        chinaSocket.insertChinaPlug();
        //输出:
        //插上英国插头
        //使用适配器将英国插头适配到中国插座
    }
}
