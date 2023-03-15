/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-16 9:20
 * @Since:
 */
package com.zja.listener.event;

/**
 * 事件监听: 监听到事件发生的时候，做一些处理，比如上面的：路人A、路人B
 * @author: zhengja
 * @since: 2023/02/16 9:20
 */
public interface EventListener<E extends AbstractEvent> {
    /**
     * 此方法负责处理事件
     * @param e 事件对象
     */
    void onEvent(E e);
}
