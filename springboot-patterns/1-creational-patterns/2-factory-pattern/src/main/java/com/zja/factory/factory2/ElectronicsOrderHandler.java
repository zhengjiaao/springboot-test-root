/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 9:29
 * @Since:
 */
package com.zja.factory.factory2;

/**订单处理器实现
 * @author: zhengja
 * @since: 2023/10/08 9:29
 */
public class ElectronicsOrderHandler implements OrderHandler {
    @Override
    public void handleOrder() {
        System.out.println("处理电子产品订单");
    }
}
