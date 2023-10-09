/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-09 17:27
 * @Since:
 */
package com.zja.memento.documentEditor;

/**
 * @author: zhengja
 * @since: 2023/10/09 17:27
 */
// 文档类
class Document {
    private String content;

    public Document copy() {
        Document copy = new Document();
        copy.setContent(content);
        return copy;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}