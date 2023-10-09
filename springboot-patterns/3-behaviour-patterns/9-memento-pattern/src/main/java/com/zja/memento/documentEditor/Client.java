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
// 客户端代码
public class Client {
    public static void main(String[] args) {
        DocumentEditor editor = new DocumentEditor();
        DocumentHistory history = new DocumentHistory();

        // 创建文档并编辑内容
        editor.createDocument();
        editor.editDocument("Hello, world!");

        // 保存当前状态到备忘录
        history.push(editor.save());

        // 修改文档内容
        editor.editDocument("Hello, GPT!"); //输出：Hello, GPT!

        // 输出当前文档内容
        editor.printDocument();

        // 恢复到之前的状态
        editor.restore(history.pop());

        // 输出恢复后的文档内容
        editor.printDocument(); //输出：Hello, world!


        //输出结果：
        //Hello, GPT!
        //Hello, world!
    }
}