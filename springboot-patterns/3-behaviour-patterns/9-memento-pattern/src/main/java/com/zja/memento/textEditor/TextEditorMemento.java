/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 17:20
 * @Since:
 */
package com.zja.memento.textEditor;

/**文本的状态快照
 * @author: zhengja
 * @since: 2023/10/09 17:20
 */
public class TextEditorMemento {
    private final String state;

    public TextEditorMemento(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}