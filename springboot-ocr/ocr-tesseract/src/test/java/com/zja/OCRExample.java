/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-02 10:57
 * @Since:
 */
package com.zja;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * @author: zhengja
 * @since: 2023/11/02 10:57
 */
public class OCRExample {
    public static void main(String[] args) {
        // 指定Tesseract OCR的数据文件路径（根据你的系统和安装位置进行相应调整）
        String tessDataPath = "E:\\App\\tesseract-ocr\\tessdata";

        // 创建Tesseract实例
        ITesseract tesseract = new Tesseract();

        // 设置Tesseract OCR的数据文件路径
        tesseract.setDatapath(tessDataPath);

        // 设置其他配置参数
//        tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO); // 页面分割模式, 默认 -1
//        tesseract.setOcrEngineMode(ITessAPI.TessOcrEngineMode.OEM_DEFAULT); // OCR引擎模式， 默认 3

        try {
            //设置识别语言(默认是英文识别):指定一个或多个语言，用逗号分隔，例如："eng"表示英语，"chi_sim"表示简体中文。
            tesseract.setLanguage("chi_sim+eng"); //chi_sim 是中文

            // 读取图像文件
            File imageFile = new File("D:\\temp\\ocr\\input-3.png");

            // 进行文本识别
            String result = tesseract.doOCR(imageFile);

            // 输出识别结果
            System.out.println("OCR Result:\n" + result);
        } catch (TesseractException e) {
            System.err.println("Error during OCR: " + e.getMessage());
        }
    }
}
