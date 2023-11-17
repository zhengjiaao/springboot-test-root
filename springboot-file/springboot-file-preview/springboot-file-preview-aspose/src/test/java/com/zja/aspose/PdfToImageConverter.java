/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-17 9:57
 * @Since:
 */
package com.zja.aspose;

import com.aspose.pdf.Document;
import com.aspose.pdf.License;
import com.aspose.pdf.devices.ImageDevice;
import com.aspose.pdf.devices.PngDevice;

import java.io.InputStream;

/**
 * @author: zhengja
 * @since: 2023/11/17 9:57
 */
public class PdfToImageConverter {
    public static void main(String[] args) throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);

        // 加载 PDF 文档
        Document document = new Document("D:\\temp\\pdf\\input.pdf");

        // 创建图像转换器
        ImageDevice imageDevice = new PngDevice();

        // 设置转换器参数
        // ImageRenderingOptions options = new ImageRenderingOptions();
        // options.setHorizontalResolution(Resolution.to_Resolution(300)); // 设置分辨率为 300 dpi

        // 循环遍历文档的每一页，并将其保存为图像文件
        for (int pageIndex = 1; pageIndex <= document.getPages().size(); pageIndex++) {
            String outputFileName = String.format("D:\\temp\\pdf\\to\\output-%d.png", pageIndex);
            // 渲染并保存图像
            imageDevice.process(document.getPages().get_Item(pageIndex), outputFileName);
        }

        System.out.println("PDF 文档已成功转换为图像。");
    }
}
