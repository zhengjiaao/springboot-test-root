/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-16 9:21
 * @Since:
 */
package com.zja.listener.event;

/**
 * 事件广播器：
 *    1.负责事件监听器的管理（注册监听器&移除监听器，将事件和监听器关联起来）
 *    2.负责事件的广播（将事件广播给所有的监听器，对该事件感兴趣的监听器会处理该事件）
 * @author: zhengja
 * @since: 2023/02/16 9:21
 */
public interface EventMulticaster {

    /**
     * 广播事件给所有的监听器，对该事件感兴趣的监听会处理该事件
     * @param event
     */
    void multicastEvent(AbstractEvent event);

    /**
     * 添加一个事件监听器
     * @param eventListener
     */
    void addEventListener(EventListener<?> eventListener);

    /**
     * 将一个监听器移除
     * @param eventListener
     */
    void removeEventListener(EventListener<?> eventListener);
}
