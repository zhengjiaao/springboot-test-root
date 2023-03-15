/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-03-15 16:38
 * @Since:
 */
package com.zja.spire.doc;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;

/**
 * @author: zhengja
 * @since: 2023/03/15 16:38
 */
public class DocTests {

    public static void main(String[] args) {
        //实例化Document类的对象
        Document doc = new Document();

        //加载Word
        doc.loadFromFile("D:\\temp\\word\\341000黄山市2020年年度体检报告.docx");

        //保存为PDF格式
        doc.saveToFile("D:\\temp\\word\\1.pdf", FileFormat.PDF);
    }
}
