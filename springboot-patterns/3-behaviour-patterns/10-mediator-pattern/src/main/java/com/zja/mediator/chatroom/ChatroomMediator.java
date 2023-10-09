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
// 中介者接口
interface ChatroomMediator {
    void sendMessage(String message, User sender);
    void addUser(User user);
}