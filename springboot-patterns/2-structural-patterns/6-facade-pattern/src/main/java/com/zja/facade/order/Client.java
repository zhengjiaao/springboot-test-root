/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 17:39
 * @Since:
 */
package com.zja.facade.order;

/**
 * @author: zhengja
 * @since: 2023/10/08 17:39
 */
public class Client {
    public static void main(String[] args) {
        OrderFacade orderFacade = new OrderFacade();
        orderFacade.placeOrder("12345", 2, "credit_card");

        //输出结果：
        //Order placed successfully.
    }
}
