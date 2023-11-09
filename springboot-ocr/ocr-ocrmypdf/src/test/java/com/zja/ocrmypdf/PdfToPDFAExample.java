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
public class PdfToPDFAExample {

    @Test
    public void ocr_pdf_to_pdf() throws IOException, InterruptedException {
        // 输入PDF文件路径
        String inputPdfPath = "D:\\temp\\ocr\\文件识别测试\\测试2.pdf";
        // 输出PDF文件路径
        String outputPdfPath = "D:\\temp\\ocr\\文件识别测试\\output2.pdf";

        // force-ocr 效果是最好的，但是会丢失标题
        //String command = "ocrmypdf -l eng+chi_sim --force-ocr " + inputPdfPath + " " + outputPdfPath;
        //skip-text 仅转文字，不转图片
        //ocrmypdf --output-type pdfa --pdfa-image-compression jpeg --skip-text input.pdf output4.pdf
        //redo-ocr 把pdf中图片也转为可识别的文字
        //ocrmypdf --output-type pdfa --pdfa-image-compression jpeg --redo-ocr input.pdf output3.pdf

        // 执行OCRmyPDF命令
        OCRmyPDFUtil.ocrPdfToPDFA(inputPdfPath, outputPdfPath);
    }

}
