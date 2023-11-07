/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-07 17:30
 * @Since:
 */
package com.zja.pdfbox;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * pdfbox将多个PDF文档合并成一个pdf
 *
 * @author: zhengja
 * @since: 2023/11/07 17:30
 */
public class PDFMerger {

    public static void main(String[] args) throws FileNotFoundException {
        String outputFilePath = "D:\\temp\\pdf\\merged-file.pdf";

        PDFMergerUtility mergerUtility = new PDFMergerUtility();

        // 添加要合并的PDF文档
        mergerUtility.addSource("D:\\temp\\pdf\\output4page_1.pdf");
        mergerUtility.addSource("D:\\temp\\pdf\\output4page_2.pdf");

        mergerUtility.setDestinationFileName(outputFilePath);

        try {
            mergerUtility.mergeDocuments(null);

            System.out.println("PDF documents merged successfully. Merged file: " + outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
