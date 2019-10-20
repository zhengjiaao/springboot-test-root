package com.dist.config;

/**
 * 用户在线监听
 * @author yinxp@dist.com.cn
 * @date 2019/2/11
 */
//@Configuration
public class OnlineListener {

    /**
     * session删除事件监听
     * @param deletedEvent
     */
   /* @EventListener
    public void onSessionDeleted(SessionDeletedEvent deletedEvent) {
        String sessionId = deletedEvent.getSessionId();
        System.out.println("onSessionDeleted"+sessionId);
    }*/

    /**
     * session创建事件监听
     * @param createdEvent
     */
    /*@EventListener
    public void onSessionCreated(SessionCreatedEvent createdEvent) {
        String sessionId = createdEvent.getSessionId();
        System.out.println("onSessionCreated"+sessionId);
    }*/
}
