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
// 发起人（Originator）类
class DocumentEditor {
    private Document document;

    public void createDocument() {
        document = new Document();
    }

    public void editDocument(String content) {
        document.setContent(content);
    }

    public DocumentMemento save() {
        return new DocumentMemento(document.copy());
    }

    public void restore(DocumentMemento memento) {
        document = memento.getDocument().copy();
    }

    public void printDocument() {
        System.out.println(document.getContent());
    }
}