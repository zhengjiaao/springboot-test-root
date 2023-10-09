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
// 中介者接口
interface ChatMediator {
    void sendMessage(String message, User user);
    void addUser(User user);
}