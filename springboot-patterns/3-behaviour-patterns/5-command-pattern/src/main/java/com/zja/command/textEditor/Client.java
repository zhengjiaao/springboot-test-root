/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 14:30
 * @Since:
 */
package com.zja.command.textEditor;

/**
 * @author: zhengja
 * @since: 2023/10/09 14:30
 */
public class Client {
    public static void main(String[] args) {
        TextEditor textEditor = new TextEditor();
        CommandManager commandManager = new CommandManager();

        Command insertCommand = new InsertTextCommand(textEditor, "Hello ");
        commandManager.executeCommand(insertCommand);

        Command deleteCommand = new DeleteTextCommand(textEditor, "World");
        commandManager.executeCommand(deleteCommand);

        textEditor.printContent(); // Output: Content: Hello

        commandManager.undo();//撤销
        textEditor.printContent(); // Output: Content: Hello World

        commandManager.redo(); //重做
        textEditor.printContent(); // Output: Content: Hello


        //输出结果:
        //Inserted text: Hello
        //Content: Hello
        //Inserted text: World
        //Content: Hello World
        //Deleted text: World
        //Content: Hello
    }
}
