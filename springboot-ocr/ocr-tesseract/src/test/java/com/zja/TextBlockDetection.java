/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-06 15:39
 * @Since:
 */
package com.zja;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/11/06 15:39
 */
public class TextBlockDetection {
    public static void main(String[] args) {
        String imagePath = "D:\\temp\\ocr\\input-3.png";
        String tessDataPath = "E:\\App\\tesseract-ocr\\tessdata";

        // 创建Tesseract实例
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessDataPath);
        tesseract.setLanguage("chi_sim+eng");

        try {
            // 从图像中获取文本块
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            List<Rectangle> textBlocks = tesseract.getSegmentedRegions(originalImage, ITessAPI.TessPageIteratorLevel.RIL_TEXTLINE);

            // 打印每个文本块的边界框坐标
            for (Rectangle block : textBlocks) {
                System.out.println("Text Block: " + block);
            }

        } catch (TesseractException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
