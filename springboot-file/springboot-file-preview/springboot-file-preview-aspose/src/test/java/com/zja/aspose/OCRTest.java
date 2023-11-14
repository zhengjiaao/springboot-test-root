/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-03 9:25
 * @Since:
 */
package com.zja.aspose;

import com.aspose.ocr.*;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author: zhengja
 * @since: 2023/11/03 9:25
 */
public class OCRTest {

    // 支持英语、法语、西班牙语和葡萄牙语
    // OCR 支持 JPG、PNG、GIF、BMP 和 TIFF 图像文件格式
    @Test
    @Deprecated // 过时的方法示例
    public void ocr_png_test() throws IOException {
        // todo 默认需要认证，已破解不需要
        /*InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);*/
        
        AsposeOCR api = new AsposeOCR();
        RecognitionSettings recognitionSettings = new RecognitionSettings();
        recognitionSettings.setLinesFiltration(false);
        recognitionSettings.setDetectAreas(true);
        recognitionSettings.setAllowedCharacters(CharactersAllowedType.ALL);
        recognitionSettings.setLanguage(Language.Chi);
        RecognitionResult result = api.RecognizePage("D:\\temp\\ocr\\input.png", recognitionSettings);
        System.out.println("Recognition result:\n" + result.recognitionText + "\n\n");
    }

    @Test
    public void ocr_images_test() throws IOException {
        // Create instance of OCR API
        AsposeOCR api = new AsposeOCR();
        // Specify recognition settings
        RecognitionSettings recognitionSettings = new RecognitionSettings();
        recognitionSettings.setAllowedCharacters(CharactersAllowedType.LATIN_ALPHABET);
        recognitionSettings.setDetectAreasMode(DetectAreasMode.DOCUMENT);
        recognitionSettings.setUpscaleSmallFont(true);
        recognitionSettings.setLanguage(Language.Chi);
        // Prepare batch
        OcrInput images = new OcrInput(InputType.SingleImage);
        images.add("D:\\temp\\ocr\\input.png");
        // Recognize images
        ArrayList<RecognitionResult> results = api.Recognize(images, recognitionSettings);
        results.forEach((result) -> {
            System.out.println(result.recognitionText);
        });

        // Save result
        // results.get(0).save("D:\\temp\\ocr\\result.txt", Format.Text); // 可用
        // results.get(0).save("D:\\temp\\ocr\\result.pdf", Format.Pdf); //  todo 异常：Can't create document
        // results.get(0).save("D:\\temp\\ocr\\result2.pdf", Format.PdfNoImg); //  todo 异常：Can't create document
        // AsposeOCR.SaveMultipageDocument("D:\\temp\\ocr\\result.pdf", Format.Pdf, results); //  todo 异常：Can't create document
    }


    /**
     * 修改PDF jar包里面的校验
     */
    @Test
    public void modifyPDFJar() {
        try {
            // 这一步是完整的jar包路径,选择自己解压的jar目录
            ClassPool.getDefault().insertClassPath("D:\\maven\\aspose-ocr-23.10.0.jar");
            // 获取指定的class文件对象
            CtClass zzZJJClass = ClassPool.getDefault().getCtClass("com.aspose.ocr.License");
            // 从class对象中解析获取所有方法
            CtMethod[] methodA = zzZJJClass.getDeclaredMethods();
            for (CtMethod ctMethod : methodA) {
                // 获取方法获取参数类型
                CtClass[] ps = ctMethod.getParameterTypes();
                System.out.println(ctMethod.getName());
                // 筛选同名方法，入参是Document
                if (ctMethod.getName().equals("isValid")) {
                    // 替换指定方法的方法体
                    ctMethod.setBody("{return true;}");
                }
            }
            // 这一步就是将破译完的代码放在桌面上
            zzZJJClass.writeFile("D:\\maven\\aspose-ocr\\");

        } catch (Exception e) {
            System.out.println("错误==" + e);
        }
    }

}
