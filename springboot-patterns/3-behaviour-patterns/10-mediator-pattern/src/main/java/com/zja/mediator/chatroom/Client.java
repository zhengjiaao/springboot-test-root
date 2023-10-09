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
// 客户端代码
public class Client {
    public static void main(String[] args) {
        ChatroomMediator chatroom = new Chatroom();
        User user1 = new ChatUser("Alice", chatroom);
        User user2 = new ChatUser("Bob", chatroom);
        User user3 = new ChatUser("Charlie", chatroom);

        chatroom.addUser(user1);
        chatroom.addUser(user2);
        chatroom.addUser(user3);

        user1.sendMessage("Hello everyone!");
        user2.sendMessage("Nice to meet you, Alice!");

        //输出结果：
        //Alice sends message: Hello everyone!
        //Bob receives message: Hello everyone!
        //Charlie receives message: Hello everyone!
        //Bob sends message: Nice to meet you, Alice!
        //Alice receives message: Nice to meet you, Alice!
        //Charlie receives message: Nice to meet you, Alice!
    }
}