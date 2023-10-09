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
// Receiver - 文本编辑器
class TextEditor {
    private StringBuilder content;

    public TextEditor() {
        content = new StringBuilder();
    }

    public void insertText(String text) {
        content.append(text);
        System.out.println("Inserted text: " + text);
    }

    public void deleteText(String text) {
        int index = content.indexOf(text);
        if (index != -1) {
            content.delete(index, index + text.length());
            System.out.println("Deleted text: " + text);
        }
    }

    public void printContent() {
        System.out.println("Content: " + content.toString());
    }
}
