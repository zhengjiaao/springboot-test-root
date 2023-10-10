/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-16 9:18
 * @Since:
 */
package com.zja.listener.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 事件对象
 * @author: zhengja
 * @since: 2023/02/16 9:18
 */
@Data
@AllArgsConstructor
public class AbstractEvent {

    /**
     * 事件源：事件的触发者，比如上面的注册器就是事件源。
     */
    private Object source;

}
