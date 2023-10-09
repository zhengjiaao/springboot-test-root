/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 14:21
 * @Since:
 */
package com.zja.command.light;

/**
 * 遥控器（RemoteControl）
 *
 * @author: zhengja
 * @since: 2023/10/09 14:21
 */
// Invoker（调用者）
class RemoteControl {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void pressButton() {
        command.execute();
    }
}