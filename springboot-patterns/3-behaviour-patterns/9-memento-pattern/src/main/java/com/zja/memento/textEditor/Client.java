/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 17:21
 * @Since:
 */
package com.zja.memento.textEditor;

/**
 * @author: zhengja
 * @since: 2023/10/09 17:21
 */
public class Client {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        UndoManager undoManager = new UndoManager();

        // 编辑文本
        editor.setText("Hello, World!");

        // 保存当前状态
        undoManager.saveMemento(editor.save());

        // 修改文本
        editor.setText("Goodbye!");

        // 输出当前文本
        System.out.println(editor.getText()); // 输出: Goodbye!

        // 恢复到之前的状态
        editor.restore(undoManager.retrieveMemento());

        // 输出恢复后的文本
        System.out.println(editor.getText()); // 输出: Hello, World!


        //输出结果：
        //Goodbye!
        //Hello, World!
    }
}