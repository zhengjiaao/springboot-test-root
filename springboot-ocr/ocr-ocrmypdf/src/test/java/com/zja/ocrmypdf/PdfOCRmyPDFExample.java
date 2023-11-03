/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-03 14:55
 * @Since:
 */
package com.zja.ocrmypdf;

import com.zja.ocrmypdf.util.OCRmyPDFUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/11/03 14:55
 */
public class PdfOCRmyPDFExample {

    @Test
    public void ocr_pdf_to_pdf() throws IOException, InterruptedException {
        // 输入PDF文件路径
        String inputPdfPath = "D:\\temp\\ocr\\input.pdf";
        // 输出PDF文件路径
        String outputPdfPath = "D:\\temp\\ocr\\output.pdf";

        // OCRmyPDF命令
        String command = "ocrmypdf -l eng+chi_sim --force-ocr " + inputPdfPath + " " + outputPdfPath;

        // 执行OCRmyPDF命令
        OCRmyPDFUtil.ocrCommand(command);
    }

}
