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
 * @author: zhengja
 * @since: 2023/10/09 14:21
 */
public class Client {
    public static void main(String[] args) {
        Light light = new Light();
        Command lightOnCommand = new LightOnCommand(light);

        RemoteControl remoteControl = new RemoteControl();
        remoteControl.setCommand(lightOnCommand);
        remoteControl.pressButton();

        //输出结果：
        //Light is on
    }
}
