package com.zja.preview.libreoffice;

import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * LibreOffice 实例
 * <p>
 * 更多应用，参考 kkFileView
 *
 * @author: zhengja
 * @since: 2024/03/05 10:57
 */
public class LibreOfficeJodconverterTest {

    // Word
    @Test
    public void Word_test() {
        File sourceFile = new File("D:\\temp\\word\\test.docx");
        File targetFile = new File("D:\\temp\\word\\test.docx.pdf");
        officeConvertToPDF(sourceFile, targetFile);
    }

    // Excel
    @Test
    public void Excel_test() {
        // 多个Sheet1，分页展示
        File sourceFile = new File("D:\\temp\\excel\\input.xlsx");
        File targetFile = new File("D:\\temp\\excel\\input.xlsx.pdf");

        // todo 超宽的Sheet页，转换pdf后会被分割多页展示，效果不好
        // File sourceFile = new File("D:\\temp\\excel\\sample.xlsx");
        // File targetFile = new File("D:\\temp\\excel\\sample.xlsx.pdf");
        officeConvertToPDF(sourceFile, targetFile);
    }

    // PPT
    @Test
    public void PPT_test() {
        File sourceFile = new File("D:\\temp\\ppt\\input.pptx");
        File targetFile = new File("D:\\temp\\ppt\\input.pptx.pdf");
        officeConvertToPDF(sourceFile, targetFile);
    }

    // PDF
    @Test
    public void PDF_test() {
        File sourceFile = new File("D:\\temp\\pdf\\input.pdf");
        File targetFile = new File("D:\\temp\\pdf\\input.pdf.pdf");
        officeConvertToPDF(sourceFile, targetFile);
    }

    /**
     * 伪代码
     * <p>
     * 支持 [doc/docx/xls/xlsx/ppt/pptx/pdf] 转为 pdf
     */
    public static boolean officeConvertToPDF(File sourceFile, File targetFile) {
        try {
            LocalOfficeManager.Builder builder = LocalOfficeManager.builder();
            builder.officeHome("C:\\Program Files\\LibreOffice");
            builder.portNumbers(8100);
            long taskExecutionTimeout = 5 * 1000 * 60;
            builder.taskExecutionTimeout(taskExecutionTimeout); // minute
            long taskQueueTimeout = 1000 * 60 * 60;
            builder.taskQueueTimeout(taskQueueTimeout); // hour

            OfficeManager build = builder.build();
            build.start();
            LocalConverter make = LocalConverter.make(build);
            make.convert(sourceFile).to(targetFile).execute();
            build.stop();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
