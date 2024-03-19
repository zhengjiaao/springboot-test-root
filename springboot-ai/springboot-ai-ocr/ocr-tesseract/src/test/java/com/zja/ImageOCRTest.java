/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-02 9:41
 * @Since:
 */
package com.zja;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/11/02 9:41
 */
public class ImageOCRTest {

    //简单示例1
    @Test
    public void ocr_image_recognizing_text() throws IOException {
        Tesseract tesseract = new Tesseract();
        //设置指定训练集位置，推荐配置为全局环境变量：TESSDATA_PREFIX=E:\App\tesseract-ocr\tessdata
        tesseract.setDatapath("E:\\App\\tesseract-ocr\\tessdata");
        //设置识别语言(默认是英文识别):指定一个或多个语言，用逗号分隔，例如："eng"表示英语，"chi_sim"表示简体中文。
        tesseract.setLanguage("chi_sim+eng"); //chi_sim 是中文

        //需要识别的图
        File imageFile = new File("D:\\temp\\ocr\\input.png");

        String result = null;
        try {
            //doOCR()方法对图像进行文本识别。识别结果将作为一个字符串返回.
            result = tesseract.doOCR(imageFile);
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
        System.out.println(result);
        //输出结果：要想在其他目录使用tesseract.exe工具，那么奖该工具父目录添加到环境变量中
    }

    //简单示例2
    @Test
    public void ocr_image_recognizing_text_2() throws IOException {

        // 创建实例
        ITesseract instance = new Tesseract();

        // 设置识别语言
        instance.setLanguage("chi_sim");
//        instance.setLanguage("jpn");

        // 设置识别引擎
        instance.setOcrEngineMode(1);
        instance.setPageSegMode(6);

        // 读取文件
        BufferedImage image = ImageIO.read(new File("D:\\temp\\ocr\\input.png"));
        try {
            // 识别
            //String res = instance.doOCR(new File("C:\\Users\\Lenovo\\Pictures\\联想截图\\联想截图_20230220144409.png"));
            String result = instance.doOCR(image);

            // 将识别结果保存到文件
//            saveResultToFile(result, "output.txt");

            // 输出结果
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

    //复杂实例：增强识别率
    @Test
    public void ocr_image_recognizing_text_3() throws IOException {
        //设置训练库的位置
        String modelPath = "E:\\App\\tesseract-ocr\\tessdata";

        //原图(可能存在模糊不好识别情况)
        File imageFile = new File("D:\\temp\\ocr\\input.png");
        //处理后的图(放大倍数，增强识别率)
        File imageFileEnlarge = new File("D:\\temp\\ocr\\ocr-output.png");

        String result = null;
        try {
            double start = System.currentTimeMillis();
            BufferedImage textImage = ImageIO.read(imageFile);
            // 这里对图片黑白处理,增强识别率.这里先通过截图,截取图片中需要识别的部分
            textImage = ImageHelper.convertImageToGrayscale(textImage);
            // 图片锐化
            textImage = ImageHelper.convertImageToBinary(textImage);
            // 图片放大倍数,增强识别率(很多图片本身无法识别,放大5倍时就可以轻易识,但是考滤到客户电脑配置低,针式打印机打印不连贯的问题,这里就放大5倍)
            textImage = ImageHelper.getScaledInstance(textImage, textImage.getWidth() * 1, textImage.getHeight() * 1);

            textImage = ImageHelper.convertImageToBinary(textImage);
            ImageIO.write(textImage, "png", imageFileEnlarge);

            Tesseract instance = new Tesseract();

            //设置模型训练集位置
            instance.setDatapath(modelPath);
            //设置中文识别
            instance.setLanguage("chi_sim");

            //文字识别
            result = instance.doOCR(textImage);

            double end = System.currentTimeMillis();
            System.out.println("耗时" + (end - start) / 1000 + " s");
            System.out.println("输出结果：" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 将OCR的文本层叠加在原始图片的上方
     * 要在OCR图片后在图片上方显示文字层,你可以使用Java的图形处理库（如Java 2D或JavaFX）来实现这个功能。
     */
    @Test
    @Deprecated //todo 未成功，效果不佳
    public void ocr_image_recognizing_text_4() throws IOException {
        Tesseract tesseract = new Tesseract();
        //设置指定训练集位置，推荐配置为全局环境变量：TESSDATA_PREFIX=E:\App\tesseract-ocr\tessdata
        tesseract.setDatapath("E:\\App\\tesseract-ocr\\tessdata");
        //设置识别语言(默认是英文识别):指定一个或多个语言，用逗号分隔，例如："eng"表示英语，"chi_sim"表示简体中文。
        tesseract.setLanguage("chi_sim"); //chi_sim 是中文

        //需要识别的图
        File imageFile = new File("D:\\temp\\ocr\\input.png");

        String result = null;
        try {
            // 读取图像并进行OCR
            BufferedImage originalImage = ImageIO.read(imageFile);
            result = tesseract.doOCR(originalImage);

            // 创建带有文本层的新图像
            BufferedImage imageWithText = createImageWithTextOverlay(originalImage, result);

            // 保存带有文本层的图像
            ImageIO.write(imageWithText, "jpg", new File("D:\\temp\\ocr\\output.jpg"));

            // 输出识别结果
            System.out.println(result);
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
    }

    //createImageWithTextOverlay方法创建了一个新的BufferedImage对象，它的宽度和高度与原始图像相同，但添加了一个额外的高度来容纳文本层。
    //然后，使用Graphics2D对象在新图像上绘制原始图像和文本。
    //你可以根据需要调整文本的字体、颜色、位置等参数，以及添加更多的绘制操作来自定义文本层。
    private static BufferedImage createImageWithTextOverlay(BufferedImage originalImage, String text) {
        // 创建一个新的BufferedImage，包含原始图像的宽度和高度，以及透明的色彩模式
        BufferedImage imageWithText = new BufferedImage(
                originalImage.getWidth(), originalImage.getHeight() + 50, BufferedImage.TYPE_INT_ARGB);

        // 获取Graphics2D对象，用于在图像上绘制文本
        Graphics2D g2d = imageWithText.createGraphics();

        // 将原始图像绘制到新图像中
        g2d.drawImage(originalImage, 0, 0, null);

        // 设置文本的字体、颜色和位置
        Font font = new Font("Arial", Font.BOLD, 36);
        Color color = Color.RED;
        int x = 10;
        int y = originalImage.getHeight() + 40;

        // 在新图像上绘制文本
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(text, x, y);

        // 释放资源
        g2d.dispose();

        return imageWithText;
    }

    // 将识别结果保存到文件
    private static void saveResultToFile(String result, String filePath) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(result);
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to save result to file: " + e.getMessage());
        }
    }

}
