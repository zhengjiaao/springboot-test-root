/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 18:06
 * @Since:
 */
package com.zja.mediator.chat;

/**
 * @author: zhengja
 * @since: 2023/10/09 18:06
 */
// 客户端代码
public class Client {
    public static void main(String[] args) {
        ChatMediator chatMediator = new ChatRoom();

        User user1 = new ChatUser("User 1", chatMediator);
        User user2 = new ChatUser("User 2", chatMediator);
        User user3 = new ChatUser("User 3", chatMediator);

        chatMediator.addUser(user1);
        chatMediator.addUser(user2);
        chatMediator.addUser(user3);

        user1.sendMessage("Hello, everyone!");
        user2.sendMessage("Hi, User 1!");

        //输出结果：
        //User 1 sends message: Hello, everyone!
        //User 2 receives message: Hello, everyone!
        //User 3 receives message: Hello, everyone!
        //User 2 sends message: Hi, User 1!
        //User 1 receives message: Hi, User 1!
        //User 3 receives message: Hi, User 1!
    }
}