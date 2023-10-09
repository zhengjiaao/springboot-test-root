/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 17:26
 * @Since:
 */
package com.zja.memento.documentEditor;

/**
 * @author: zhengja
 * @since: 2023/10/09 17:26
 */
// 备忘录（Memento）类
class DocumentMemento {
    private Document document;

    public DocumentMemento(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }
}
