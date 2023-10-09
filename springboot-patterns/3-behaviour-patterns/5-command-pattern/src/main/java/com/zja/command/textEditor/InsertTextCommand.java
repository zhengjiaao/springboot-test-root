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
// Concrete Command - 插入文本命令
class InsertTextCommand implements Command {
    private TextEditor textEditor;
    private String text;

    public InsertTextCommand(TextEditor textEditor, String text) {
        this.textEditor = textEditor;
        this.text = text;
    }

    @Override
    public void execute() {
        textEditor.insertText(text);
    }

    @Override
    public void undo() {
        textEditor.deleteText(text);
    }
}