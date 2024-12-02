package com.zja.poitl.base;

import com.deepoove.poi.data.*;
import org.junit.jupiter.api.Test;

/**
 * @Author: zhengja
 * @Date: 2024-11-21 10:53
 */
public class ParagraphRenderDataTest {

    // 检查段落数据中是否包含指定类型的 RenderData
    @Test
    public void test_1() {
        // 示例用法
        ParagraphRenderData paragraph = new ParagraphRenderData();
        paragraph.addPicture(Pictures.ofLocal("path/to/example.jpg").size(200, 200).create());
        paragraph.addText(new TextRenderData("这是文本内容"));

        boolean result1 = isParagraphContainTypes(paragraph, PictureRenderData.class);
        System.out.println("段落是否包含图片: " + result1); // 应该输出 true

        boolean result2 = isParagraphContainTypes(paragraph, TextRenderData.class);
        System.out.println("段落是否包含文本: " + result2); // 应该输出 true

        boolean result3 = isParagraphContainTypes(paragraph, PictureRenderData.class, TextRenderData.class);
        System.out.println("段落是否包含图片和文本: " + result3); // 应该输出 true

        boolean paragraphContainPictureAndText = isParagraphContainPictureAndText(paragraph);
        System.out.println("段落是否同时包含图片和文本: " + paragraphContainPictureAndText); // 应该输出 true
    }

    /**
     * 检查段落数据中是否包含指定类型的 RenderData
     *
     * @param paragraph 段落数据
     * @param types     要检查的 RenderData 类型列表
     * @return 是否包含所有指定类型的 RenderData
     */
    public static boolean isParagraphContainTypes(ParagraphRenderData paragraph, Class<?>... types) {
        if (paragraph == null || paragraph.getContents() == null || paragraph.getContents().isEmpty()) {
            return false;
        }

        boolean[] foundTypes = new boolean[types.length];
        for (int i = 0; i < types.length; i++) {
            foundTypes[i] = false;
        }

        for (RenderData content : paragraph.getContents()) {
            for (int i = 0; i < types.length; i++) {
                if (types[i].isInstance(content)) {
                    foundTypes[i] = true;
                }
            }

            if (allTypesFound(foundTypes)) {
                return true;
            }
        }

        return allTypesFound(foundTypes);
    }

    /**
     * 检查是否所有指定的类型都已找到
     *
     * @param foundTypes 找到的类型数组
     * @return 是否所有类型都已找到
     */
    private static boolean allTypesFound(boolean[] foundTypes) {
        for (boolean found : foundTypes) {
            if (!found) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查段落数据中是否同时包含图片和文本
     *
     * @param paragraph 段落数据
     * @return 是否同时包含图片和文本
     */
    public static boolean isParagraphContainPictureAndText(ParagraphRenderData paragraph) {
        if (paragraph == null || paragraph.getContents() == null || paragraph.getContents().isEmpty()) {
            return false;
        }

        boolean hasPicture = false;
        boolean hasText = false;

        for (RenderData content : paragraph.getContents()) {
            if (content instanceof PictureRenderData) {
                hasPicture = true;
            } else if (content instanceof TextRenderData) {
                hasText = true;
            }

            if (hasPicture && hasText) {
                return true;
            }
        }

        return false;
    }
}
