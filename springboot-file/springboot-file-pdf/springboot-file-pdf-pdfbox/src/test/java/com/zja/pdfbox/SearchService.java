package com.zja.pdfbox;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现基于PDFBox的文件内容检索功能，支持关键词匹配及结果定位。
 *
 * @Author: zhengja
 * @Date: 2025-04-09 13:13
 */
public class SearchService {

    public static void main(String[] args) {
        // List<Map<String, Object>> results = searchKeywordInPdfs("D:\\temp\\pdf", "关键词");
        List<Map<String, Object>> results = searchKeywordInPdfs("D:\\temp\\pdf", "永久基本农田");
        for (Map<String, Object> result : results) {
            System.out.println("文件名：" + result.get("fileName"));
            System.out.println("匹配内容：");
            List<String> matchedContent = (List<String>) result.get("matchedContent");
            for (String content : matchedContent) {
                System.out.println(content);
            }
            System.out.println("----------------------------------------");
        }
    }

    public static List<Map<String, Object>> searchKeywordInPdfs(String directoryPath, String keyword) {
        List<Map<String, Object>> results = new ArrayList<>();
        File dir = new File(directoryPath);

        if (dir.isDirectory()) {
            File[] pdfFiles = dir.listFiles((d, name) -> name.endsWith(".pdf"));

            if (pdfFiles != null) {
                for (File pdf : pdfFiles) {
                    if (!isValidPDF(pdf)) {
                        System.err.println("Invalid PDF file: " + pdf.getName());
                        continue;
                    }

                    try {
                        String content = extractText(pdf);
                        if (content.contains(keyword)) {
                            Map<String, Object> result = new HashMap<>();
                            result.put("fileName", pdf.getName());
                            result.put("matchedContent", extractMatchedBlocks(content, keyword));
                            results.add(result);
                        }
                    } catch (IOException e) {
                        System.err.println("Error processing file: " + pdf.getName());
                        e.printStackTrace();
                    }
                }
            }
        }
        return results;
    }

    private static boolean isValidPDF(File file) {
        if (!file.exists() || file.isDirectory() || file.length() == 0) {
            return false;
        }
        // 可以进一步检查文件头是否符合 PDF 标准（如 "%PDF-"）
        return true;
    }

    private static List<String> extractMatchedBlocks(String content, String keyword) {
        // 示例：按行分割并匹配关键词
        List<String> blocks = new ArrayList<>();
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line.contains(keyword)) {
                blocks.add(line.trim());
            }
        }
        return blocks;
    }

    public static String extractText(File file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}