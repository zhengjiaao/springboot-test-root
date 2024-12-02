package com.zja.poitl.base;

import com.deepoove.poi.data.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-11-21 9:33
 */
public class RowRenderDataTest {

    // 存储单元格数据中是否包含图片
    @Test
    public void test_1() {
        RowRenderData row = createPicturesRow("pictures/logo.png", 3);

        for (CellRenderData cell : row.getCells()) {
            if (isCellContainPicture(cell)) {
                System.out.println("该单元格包含图片");
            } else {
                System.out.println("该单元格不包含图片");
            }
        }

        System.out.println("-----------------------------");

        RowRenderData textRow = createTextRow("文本", 3);
        for (CellRenderData cell : textRow.getCells()) {
            if (isCellContainPicture(cell)) {
                System.out.println("该单元格包含图片");
            } else {
                System.out.println("该单元格不包含图片");
            }
        }
    }

    // 存储单元格数据中是否包含文本
    @Test
    public void test_2() {
        RowRenderData row = createPicturesRow("pictures/logo.png", 3);

        for (CellRenderData cell : row.getCells()) {
            if (isCellContainText(cell)) {
                System.out.println("该单元格包含文本");
            } else {
                System.out.println("该单元格不包含文本");
            }
        }

        System.out.println("-----------------------------");

        RowRenderData textRow = createTextRow("文本", 3);
        for (CellRenderData cell : textRow.getCells()) {
            if (isCellContainText(cell)) {
                System.out.println("该单元格包含文本");
            } else {
                System.out.println("该单元格不包含文本");
            }
        }
    }

    // 存储单元格数据中是否同时包含图片和文本
    @Test
    public void test_3() {
        RowRenderData row = createPicturesAndTextRow("pictures/logo.png", "文本", 3, true);
        // RowRenderData row = createPicturesAndTextRow("pictures/logo.png", "文本", 3, false);
        // RowRenderData row = createPicturesRow("pictures/logo.png",  3);

        for (CellRenderData cell : row.getCells()) {
            if (isCellContainText(cell)) {
                System.out.println("该单元格包含文本");
            } else {
                System.out.println("该单元格不包含文本");
            }
        }

        System.out.println("-----------------------------");

        for (CellRenderData cell : row.getCells()) {
            if (isCellContainPicture(cell)) {
                System.out.println("该单元格包含图片");
            } else {
                System.out.println("该单元格不包含图片");
            }
        }

        System.out.println("-----------------------------");

        for (CellRenderData cell : row.getCells()) {
            // 检查单元格数据中是否同时包含图片和文本
            if (isCellContainPictureAndText(cell)) {
                System.out.println("该单元格同时包含图片和文本");
            } else {
                System.out.println("该单元格不同时包含图片和文本");
            }
        }
    }

    public boolean isCellContainText(CellRenderData cell) {
        if (cell == null || cell.getParagraphs() == null || cell.getParagraphs().isEmpty()) {
            return false;
        }

        for (ParagraphRenderData paragraph : cell.getParagraphs()) {
            List<RenderData> contents = paragraph.getContents();

            // 进一步检查 contents 列表中的 RenderData 是否包含 TextRenderData
            for (RenderData content : contents) {
                if (content instanceof TextRenderData) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isCellContainPicture(CellRenderData cell) {
        if (cell == null || cell.getParagraphs() == null || cell.getParagraphs().isEmpty()) {
            return false;
        }

        for (ParagraphRenderData paragraph : cell.getParagraphs()) {
            List<RenderData> contents = paragraph.getContents();

            // 进一步检查 contents 列表中的 RenderData 是否包含 PictureRenderData
            for (RenderData content : contents) {
                if (content instanceof PictureRenderData) {
                    return true;
                }
            }
        }

        return false;
    }

    // 检查单元格数据中是否同时包含图片和文本
    public boolean isCellContainPictureAndText(CellRenderData cell) {
        if (cell == null || cell.getParagraphs() == null || cell.getParagraphs().isEmpty()) {
            return false;
        }

        // 进一步检查 contents 列表中的 RenderData 是否同时包含 PictureRenderData 和 TextRenderData
        boolean hasPicture = false;
        boolean hasText = false;

        for (ParagraphRenderData paragraph : cell.getParagraphs()) {
            List<RenderData> contents = paragraph.getContents();

            for (RenderData content : contents) {
                if (content instanceof PictureRenderData) {
                    hasPicture = true;
                } else if (content instanceof TextRenderData) {
                    hasText = true;
                }

                if (hasPicture && hasText) {
                    return true;
                }
            }
        }

        return false;
    }

    private RowRenderData createPicturesRow(String fileName, int addCell) {
        // 本地图片
        String localPath = getPath(fileName);
        RowRenderData row1 = new RowRenderData();
        CellRenderData cell = new CellRenderData();
        PictureRenderData pictureRenderData = Pictures.ofLocal(localPath).size(200, 200).create();
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData();

        paragraphRenderData.addPicture(pictureRenderData);
        cell.addParagraph(paragraphRenderData);

        for (int i = 0; i < addCell; i++) {
            row1.addCell(cell);
        }

        return row1;
    }

    private RowRenderData createTextRow(String text, int addCell) {
        RowRenderData row2 = new RowRenderData();
        CellRenderData cell = new CellRenderData();
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData();

        paragraphRenderData.addText(new TextRenderData(text));
        cell.addParagraph(paragraphRenderData);

        for (int i = 0; i < addCell; i++) {
            row2.addCell(cell);
        }

        return row2;
    }

    // 同时插入图片和文本(同一个段落内容)
    private RowRenderData createPicturesAndTextRow(String fileName, String text, int addCell) {
        // 创建行
        RowRenderData row = new RowRenderData();

        // 创建单元格
        CellRenderData cell = new CellRenderData();

        // 创建段落（单元格内）
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData();

        // 插入图片
        String localPath = getPath(fileName);
        PictureRenderData pictureRenderData = Pictures.ofLocal(localPath).size(200, 200).create();
        paragraphRenderData.addPicture(pictureRenderData);
        // 插入文本
        paragraphRenderData.addText(new TextRenderData(text));

        // 将图片和文本添加到单元格
        cell.addParagraph(paragraphRenderData);

        for (int i = 0; i < addCell; i++) {
            row.addCell(cell);
        }

        return row;
    }

    private RowRenderData createPicturesAndTextRow(String fileName, String text, int addCell, boolean isAParagraph) {
        // 创建行
        RowRenderData row = new RowRenderData();

        // 创建单元格
        CellRenderData cell = new CellRenderData();

        // 创建段落（单元格内）
        ParagraphRenderData paragraphRenderData = new ParagraphRenderData();

        // 插入图片
        String localPath = getPath(fileName);
        PictureRenderData pictureRenderData = Pictures.ofLocal(localPath).size(200, 200).create();
        paragraphRenderData.addPicture(pictureRenderData);

        // 插入文本
        if (isAParagraph) {
            // 在同一个段落内容包含图片和文本
            paragraphRenderData.addText(new TextRenderData(text));
            // 将图片和文本添加到单元格
            cell.addParagraph(paragraphRenderData);
        } else {
            // 在不同段落内容包含图片和文本
            // 将图片和文本添加到单元格
            cell.addParagraph(paragraphRenderData);

            // 将文本段落添加到单元格
            ParagraphRenderData textParagraphRenderData = new ParagraphRenderData();
            textParagraphRenderData.addText(new TextRenderData(text));
            cell.addParagraph(textParagraphRenderData);
        }

        for (int i = 0; i < addCell; i++) {
            row.addCell(cell);
        }

        return row;
    }

    // 获取当前行内单元格数量
    @Test
    public void test_4() throws IOException {
        RowRenderData row = createPicturesRow("pictures/logo.png", 3);
        System.out.println(row.getCells().size());
    }

    private String getPath(String fileName) {
        return getClass().getResource("/" + fileName).getPath();
    }
}
