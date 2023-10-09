/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 14:40
 * @Since:
 */
package com.zja.command.order;

import java.util.Stack;

/**
 * @author: zhengja
 * @since: 2023/10/09 14:40
 */
// Invoker - 命令管理者
class CommandManager {
    private Stack<Command> commands;

    public CommandManager() {
        commands = new Stack<>();
    }

    public void executeCommand(Command command) {
        command.execute();
        commands.push(command);
    }

    public void undo() {
        if (!commands.isEmpty()) {
            Command command = commands.pop();
            command.undo();
        }
    }
}