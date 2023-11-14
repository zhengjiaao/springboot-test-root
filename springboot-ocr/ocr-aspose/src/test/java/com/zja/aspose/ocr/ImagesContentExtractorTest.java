/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-03 9:25
 * @Since:
 */
package com.zja.aspose.ocr;

import com.aspose.ocr.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author: zhengja
 * @since: 2023/11/03 9:25
 */
public class ImagesContentExtractorTest {

    // 单个图像
    // 支持英语、法语、西班牙语和葡萄牙语
    // OCR 支持 JPG、PNG、GIF、BMP 和 TIFF 图像文件格式
    @Test
    public void ocr_image_test() throws IOException {
        // todo 默认需要认证，已破解不需要
        /*InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);*/

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


    // 多个图像
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
    }

}
