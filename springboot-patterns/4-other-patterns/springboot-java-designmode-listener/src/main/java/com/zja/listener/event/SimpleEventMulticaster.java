/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-02-16 9:22
 * @Since:
 */
package com.zja.listener.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单事件广播器实现
 * @author: zhengja
 * @since: 2023/02/16 9:22
 */
public class SimpleEventMulticaster implements EventMulticaster {

    private Map<Class<?>, List<EventListener>> eventObjectEventListenerMap = new ConcurrentHashMap<>();


    @Override
    public void multicastEvent(AbstractEvent event) {
        List<EventListener> eventListeners = eventObjectEventListenerMap.get(event.getClass());
        if (eventListeners != null) {
            eventListeners.parallelStream().forEach(e -> e.onEvent(event));
        }

    }

    @Override
    public void addEventListener(EventListener<?> eventListener) {
        Class<?> eventType = this.getEventType(eventListener);
        List<EventListener> listeners = this.eventObjectEventListenerMap.computeIfAbsent(eventType, e -> new ArrayList<>());
        listeners.add(eventListener);
    }

    @Override
    public void removeEventListener(EventListener<?> eventListener) {
        Class<?> eventType = this.getEventType(eventListener);
        List<EventListener> eventListeners = this.eventObjectEventListenerMap.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(eventListener);
        }
    }

    /**
     * 获取事件的类型,这里的代码可能不是很常见，其实就是获取泛型类型而已
     * @param eventListener
     * @return
     */
    protected Class<?> getEventType(EventListener<? extends AbstractEvent> eventListener) {
        ParameterizedType parameterizedType = (ParameterizedType) eventListener.getClass().getGenericInterfaces()[0];
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        return (Class<?>) actualTypeArgument;
    }

}
