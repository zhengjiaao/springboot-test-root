/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 17:37
 * @Since:
 */
package com.zja.mediator.chatroom;

/**
 * @author: zhengja
 * @since: 2023/10/09 17:37
 */
// 具体同事
class ChatUser implements User {
    private String name;
    private ChatroomMediator mediator;

    public ChatUser(String name, ChatroomMediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }

    @Override
    public void sendMessage(String message) {
        System.out.println(name + " sends message: " + message);
        mediator.sendMessage(message, this);
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println(name + " receives message: " + message);
    }
}
