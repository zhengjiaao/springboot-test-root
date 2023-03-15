/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-16 9:26
 * @Since:
 */
package com.zja.listener.mylistener;

import com.zja.listener.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 注册成功后发优惠券
 * @author: zhengja
 * @since: 2023/02/16 9:26
 */
@Order(2)  // 值越小约先执行
@Component
public class GetCouponRegisterSuccessListener implements EventListener<RegisterSuccessEvent> {

    @Override
    public void onEvent(RegisterSuccessEvent event) {
        System.out.println(event.getUsername() + "注册成功，赠送优惠券。。。。。");
    }
}
