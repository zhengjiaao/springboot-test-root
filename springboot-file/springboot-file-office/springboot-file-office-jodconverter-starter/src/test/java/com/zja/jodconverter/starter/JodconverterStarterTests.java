package com.zja.jodconverter.starter;

import com.zja.jodconverter.starter.util.LibreOfficeUtil;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.office.OfficeException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;

/**
 * @Author: zhengja
 * @Date: 2025-04-18 16:37
 */
@SpringBootTest
public class JodconverterStarterTests {

    @Resource
    private DocumentConverter documentConverter;

    // 支持 本地 LibreOffice or 远程 LibreOffice
    // 支持 doc、docx、excel、pptx、pdf等互相转换
    @Test
    public void test() {
        File sourceFile = new File("D:\\temp\\word\\test.docx");
        File targetFile = new File("D:\\temp\\word\\test.docx_1.pdf");

        // 调用转换方法
        try {
            documentConverter.convert(sourceFile).to(targetFile).execute();
        } catch (OfficeException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void test2() {
        File sourceFile = new File("D:\\temp\\word\\test.docx");
        File targetFile = new File("D:\\temp\\word\\test.docx_2.pdf");

        byte[] sourceFileByte;
        try {
            sourceFileByte = Files.readAllBytes(sourceFile.toPath());
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to read source file", e);
        }

        // 可以传文件，也可以传字节流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            documentConverter.convert(new ByteArrayInputStream(sourceFileByte)).to(baos).as(DefaultDocumentFormatRegistry.PDF).execute();
        } catch (OfficeException e) {
            throw new RuntimeException("Conversion failed", e);
        }
        byte[] resultByte = baos.toByteArray();

        // 输出到 targetFile 文件
        try {
            Files.write(targetFile.toPath(), resultByte);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to write target file", e);
        }
    }

}
