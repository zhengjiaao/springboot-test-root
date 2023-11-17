/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-17 11:16
 * @Since:
 */
package com.zja.util;

import com.aspose.cells.Workbook;
import com.aspose.slides.Presentation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author: zhengja
 * @since: 2023/11/17 11:16
 */
public class AsposePdfUtil {

    private AsposePdfUtil() {

    }


    public static void officeToPdf(String officeFilePath, String pdfFilePath) throws Exception {
        File officeFile = new File(officeFilePath);
        if (!officeFile.exists()) {
            throw new RuntimeException("文件不存在：" + officeFilePath);
        }

        String fileName = officeFile.getName().toLowerCase();
        if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            wordToPdf(officeFilePath, pdfFilePath);
        } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
            excelToPdf(officeFilePath, pdfFilePath);
        } else if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
            pptToPdf(officeFilePath, pdfFilePath);
        } else {
            throw new RuntimeException("不支持转换的文件：" + fileName);
        }
    }

    public static void wordToPdf(String sourceFile, String targetFile) throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        com.aspose.words.License license = new com.aspose.words.License();
        license.setLicense(is);

        try {
            long old = System.currentTimeMillis();
            FileOutputStream os = new FileOutputStream(targetFile);
            com.aspose.words.Document doc = new com.aspose.words.Document(sourceFile);
            doc.save(os, com.aspose.words.SaveFormat.PDF);
            os.close();
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  // 转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void excelToPdf(String sourceFile, String targetFile) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        com.aspose.cells.License license = new com.aspose.cells.License();
        license.setLicense(is);

        try {
            long old = System.currentTimeMillis();
            FileOutputStream os = new FileOutputStream(targetFile);
            Workbook excel = new Workbook(sourceFile);// 加载源文件数据
            excel.save(os, com.aspose.cells.SaveFormat.PDF);// 设置转换文件类型并转换
            os.close();
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  // 转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pptToPdf(String sourceFile, String targetFile) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        com.aspose.slides.License license = new com.aspose.slides.License();
        license.setLicense(is);
        
        try {
            long old = System.currentTimeMillis();
            FileOutputStream os = new FileOutputStream(targetFile);
            Presentation ppt = new Presentation(sourceFile);// 加载源文件数据
            ppt.save(os, com.aspose.slides.SaveFormat.Pdf);// 设置转换文件类型并转换
            os.close();
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  // 转化用时
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
