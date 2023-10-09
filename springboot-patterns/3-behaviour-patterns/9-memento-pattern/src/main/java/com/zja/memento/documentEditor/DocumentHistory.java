/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 17:26
 * @Since:
 */
package com.zja.memento.documentEditor;

import java.util.Stack;

/**
 * @author: zhengja
 * @since: 2023/10/09 17:26
 */
// 管理者（Caretaker）类
class DocumentHistory {
    private Stack<DocumentMemento> stack = new Stack<>();

    public void push(DocumentMemento memento) {
        stack.push(memento);
    }

    public DocumentMemento pop() {
        return stack.pop();
    }
}