/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-16 9:27
 * @Since:
 */
package com.zja.listener.service;

import com.zja.listener.event.EventMulticaster;
import com.zja.listener.mylistener.RegisterSuccessEvent;
import lombok.Data;

/**
 * @author: zhengja
 * @since: 2023/02/16 9:27
 */
@Data
public class RegisterService {

    private EventMulticaster eventMulticaster;

    public void register(String username) {
        //用户注册，将数据写人到数据库中
        System.out.println("用户注册成功。。。。" + username);

        //事件广播
        //使用事件发布者eventPublisher发布用户注册成功的消息:
        this.eventMulticaster.multicastEvent(new RegisterSuccessEvent(this, username));
    }
}
