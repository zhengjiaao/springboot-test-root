/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-03 16:07
 * @Since:
 */
package com.zja.ocrmypdf;

import com.zja.ocrmypdf.util.CommandUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * 安装：img2pdf
 * pip3 install img2pdf
 * apt install img2pdf
 *
 * @author: zhengja
 * @since: 2023/11/03 16:07
 */
@Deprecated
public class ImageImg2pdfExample {

    /**
     * todo 未测试成功
     * 如果您有多个图像，则必须使用img2pdf将图像转换为 PDF。使用 OCRmyPDF（仅限单个图像）.
     */
    @Test
    public void ocr_png_to_pdf() throws IOException, InterruptedException {
        String inputImagePath = "D:\\temp\\ocr\\input*.png";
        String outputPdfPath = "D:\\temp\\ocr\\output-Img2pdf.pdf";

        String command = "img2pdf " + inputImagePath + " | ocrmypdf - " + outputPdfPath;

        CommandUtil.command(command);
    }
}
