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
 * @Date: 2025-04-09 13:22
 */
public class SinglePdfSearchService {

    public static void main(String[] args) {
        // 示例：搜索单个 PDF 文件中的关键词
        String pdfFilePath = "D:\\temp\\pdf\\100M.pdf"; // 单个 PDF 文件路径
        String keyword = "永久基本农田"; // 要搜索的关键词

        Map<String, Object> result = searchKeywordInPdf(pdfFilePath, keyword);
        if (result != null) {
            System.out.println("文件名：" + result.get("fileName"));
            System.out.println("匹配内容：");
            List<String> matchedContent = (List<String>) result.get("matchedContent");
            for (String content : matchedContent) {
                System.out.println(content);
            }
        } else {
            System.out.println("未找到匹配内容或文件无效。");
        }
    }

    /**
     * 在单个 PDF 文件中搜索关键词。
     *
     * @param pdfFilePath PDF 文件路径
     * @param keyword     要搜索的关键词
     * @return 包含文件名和匹配内容的结果，如果没有匹配则返回 null
     */
    public static Map<String, Object> searchKeywordInPdf(String pdfFilePath, String keyword) {
        File file = new File(pdfFilePath);

        if (!isValidPDF(file)) {
            System.err.println("Invalid PDF file: " + file.getName());
            return null;
        }

        try {
            String content = extractText(file);
            if (content.contains(keyword)) {
                Map<String, Object> result = new HashMap<>();
                result.put("fileName", file.getName());
                result.put("matchedContent", extractMatchedBlocks(content, keyword));
                return result;
            }
        } catch (IOException e) {
            System.err.println("Error processing file: " + file.getName());
            e.printStackTrace();
        }
        return null;
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