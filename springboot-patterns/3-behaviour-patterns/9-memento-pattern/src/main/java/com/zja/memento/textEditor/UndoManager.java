/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 17:21
 * @Since:
 */
package com.zja.memento.textEditor;

/**管理者（Caretaker）类，用于保存和恢复备忘录对象。
 * @author: zhengja
 * @since: 2023/10/09 17:21
 */
public class UndoManager {
    private TextEditorMemento memento;

    public void saveMemento(TextEditorMemento memento) {
        this.memento = memento;
    }

    public TextEditorMemento retrieveMemento() {
        return memento;
    }
}