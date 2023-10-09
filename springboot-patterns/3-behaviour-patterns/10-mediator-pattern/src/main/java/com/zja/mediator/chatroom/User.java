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
// 同事接口
interface User {
    void sendMessage(String message);
    void receiveMessage(String message);
}