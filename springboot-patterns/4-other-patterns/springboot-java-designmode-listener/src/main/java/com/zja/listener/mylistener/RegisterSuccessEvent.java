/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-16 9:24
 * @Since:
 */
package com.zja.listener.mylistener;

import com.zja.listener.event.AbstractEvent;
import lombok.Getter;

/**
 * 用户注册事件
 * @author: zhengja
 * @since: 2023/02/16 9:24
 */
@Getter
public class RegisterSuccessEvent extends AbstractEvent {

    private String username;

    public RegisterSuccessEvent(Object source, String username) {
        super(source);
        this.username = username;
    }
}
