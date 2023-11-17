/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-17 9:22
 * @Since:
 */
package com.zja.aspose;

import com.aspose.cells.SaveFormat;
import com.aspose.words.Document;
import com.aspose.words.ImageSaveOptions;
import com.aspose.words.License;
import com.aspose.words.PageSet;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * @author: zhengja
 * @since: 2023/11/17 9:22
 */
public class WordToImageConverter {

    @Test
    public void wordToImageTest() throws Exception {
        // 先认证
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);

        // 加载 Word 文档
        Document doc = new Document("D:\\temp\\word\\test.docx");

        // 创建图像保存选项
        ImageSaveOptions options = new ImageSaveOptions(SaveFormat.PNG);

        // 循环遍历文档的每一页，并将其保存为图像文件
        for (int pageIndex = 0; pageIndex < doc.getPageCount(); pageIndex++) {
            String outputFileName = String.format("D:\\temp\\word\\to\\output-%d.png", pageIndex);

            // 设置保存选项，指定保存的页面范围
            options.setPageSet(new PageSet(pageIndex));

            // 保存当前页面为图像
            doc.save(outputFileName, options);
        }

        System.out.println("Word 文档已成功转换为图像。");
    }

}
