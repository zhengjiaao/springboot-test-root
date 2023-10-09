/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 14:30
 * @Since:
 */
package com.zja.command.textEditor;

import java.util.Stack;

/**
 * @author: zhengja
 * @since: 2023/10/09 14:30
 */
// Invoker - 命令管理者
class CommandManager {
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public CommandManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void executeCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }
}