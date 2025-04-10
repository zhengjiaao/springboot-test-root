package com.zja.pdfbox;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 提取PDF页面：提取页面输出到新的pdf文件
 *
 * @Author: zhengja
 * @Date: 2025-04-10 10:58
 */
public class PdfPageExtractor {

    public static void main(String[] args) {
        try {
            // File source = new File("input.pdf");
            File source = new File("D:\\temp\\pdf\\100M.pdf");
            File output = new File("D:\\temp\\pdf\\100M-page-output.pdf");
            String pageRanges = "1-3,5,7-8"; // 支持连续范围和单页

            PdfPageExtractor.extractPages(source, output, pageRanges);
            System.out.println("页面提取完成！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void extractPages(File sourceFile, File outputFile, String pageRanges) throws IOException {
        try (PDDocument sourceDoc = Loader.loadPDF(sourceFile);
             PDDocument newDoc = new PDDocument()) {

            // 解析页码范围（支持格式：1-3,5,7-8）
            List<Integer> pagesToExtract = parsePageRanges(pageRanges, sourceDoc.getNumberOfPages());

            // 添加指定页面到新文档
            for (int pageNumber : pagesToExtract) {
                PDPage page = sourceDoc.getPage(pageNumber - 1); // 转换为0-based索引
                newDoc.addPage(page);
            }

            // 保存新文档
            newDoc.save(outputFile);
        }
    }

    private static List<Integer> parsePageRanges(String ranges, int totalPages) {
        List<Integer> pages = new ArrayList<>();
        for (String range : ranges.split(",")) {
            if (range.contains("-")) {
                String[] parts = range.split("-");
                int start = Integer.parseInt(parts[0]);
                int end = Integer.parseInt(parts[1]);
                for (int i = start; i <= end; i++) {
                    if (i <= totalPages) pages.add(i);
                }
            } else {
                int page = Integer.parseInt(range);
                if (page <= totalPages) pages.add(page);
            }
        }
        return pages;
    }
}