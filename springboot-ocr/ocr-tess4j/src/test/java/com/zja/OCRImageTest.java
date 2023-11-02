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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author: zhengja
 * @since: 2023/11/02 9:41
 */
public class OCRImageTest {

    //简单示例1
    @Test
    public void ocr_png_test1() throws IOException {
        Tesseract tesseract = new Tesseract();
        //设置指定训练集位置，推荐配置为全局环境变量：TESSDATA_PREFIX=E:\App\tesseract-ocr\tessdata
        tesseract.setDatapath("E:\\App\\tesseract-ocr\\tessdata");
        //设置识别语言(默认是英文识别)
        tesseract.setLanguage("chi_sim"); //chi_sim 是中文

        //需要识别的图
        File imageFile = new File("D:\\temp\\images\\ocr-test.png");

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
    public void ocr_png_test2() throws IOException {

        // 创建实例
        ITesseract instance = new Tesseract();

        // 设置识别语言
        instance.setLanguage("chi_sim");
//        instance.setLanguage("jpn");

        // 设置识别引擎
        instance.setOcrEngineMode(1);
        instance.setPageSegMode(6);

        // 读取文件
        BufferedImage image = ImageIO.read(new File("D:\\temp\\images\\ocr-test.png"));
        try {
            // 识别
            //String res = instance.doOCR(new File("C:\\Users\\Lenovo\\Pictures\\联想截图\\联想截图_20230220144409.png"));
            String result = instance.doOCR(image);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

    //复杂实例：增强识别率
    @Test
    public void ocr_png_test3() throws IOException {
        //设置训练库的位置
        String modelPath = "E:\\App\\tesseract-ocr\\tessdata";

        //原图(可能存在模糊不好识别情况)
        File imageFile = new File("D:\\temp\\images\\ocr-test.png");
        //处理后的图(放大倍数，增强识别率)
        File imageFileEnlarge = new File("D:\\temp\\images\\ocr-test-temp.png");

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

}
