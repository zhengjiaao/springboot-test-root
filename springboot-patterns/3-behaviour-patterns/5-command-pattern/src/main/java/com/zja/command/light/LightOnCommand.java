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
 * 指示灯打开命令（LightOnCommand）
 *
 * @author: zhengja
 * @since: 2023/10/09 14:21
 */
// ConcreteCommand（具体命令）
class LightOnCommand implements Command {
    private Light light;

    public LightOnCommand(Light light) {
        this.light = light;
    }

    @Override
    public void execute() {
        light.turnOn();
    }
}