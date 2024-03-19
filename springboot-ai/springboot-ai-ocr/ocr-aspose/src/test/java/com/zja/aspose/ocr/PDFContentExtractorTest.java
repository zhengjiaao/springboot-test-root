/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-14 9:40
 * @Since:
 */
package com.zja.aspose.ocr;

import com.aspose.ocr.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author: zhengja
 * @since: 2023/11/14 9:40
 */
@Deprecated // 无法识别完整pdf，仅能读取pdf中图像内容
public class PDFContentExtractorTest {

    /**
     * todo 仅能读取pdf里图像中的文字，无法读取整个 pdf 内容
     */
    @Test
    public void ocr_pdf_test() throws IOException {
        // Create instance of OCR API
        AsposeOCR api = new AsposeOCR();
        // Specify recognition settings
        RecognitionSettings recognitionSettings = new RecognitionSettings();
        // recognitionSettings.setAllowedCharacters(CharactersAllowedType.LATIN_ALPHABET);
        // recognitionSettings.setDetectAreasMode(DetectAreasMode.DOCUMENT);
        // recognitionSettings.setUpscaleSmallFont(true);
        recognitionSettings.setLanguage(Language.Chi);
        // Prepare batch
        OcrInput ocrInput = new OcrInput(InputType.PDF);
        ocrInput.add("D:\\temp\\ocr\\input.pdf");
        // ocrInput.add("D:\\temp\\ocr\\input.pdf",0,5); //指定扫描页
        // Recognize file
        ArrayList<RecognitionResult> results = api.Recognize(ocrInput, recognitionSettings);
        results.forEach((result) -> {
            System.out.println(result.recognitionText);
        });
    }


    // 读取pdf中复杂图像识别
    // todo 效果不好，示例：识别营业执照
    @Test
    public void ocr_images_complex_test() throws IOException {
        // Create instance of OCR API
        AsposeOCR api = new AsposeOCR();
        // Specify recognition settings
        RecognitionSettings recognitionSettings = new RecognitionSettings();
        recognitionSettings.setAllowedCharacters(CharactersAllowedType.LATIN_ALPHABET);
        recognitionSettings.setDetectAreasMode(DetectAreasMode.DOCUMENT);
        recognitionSettings.setUpscaleSmallFont(true);
        recognitionSettings.setLanguage(Language.Chi);
        // Prepare batch
        OcrInput images = new OcrInput(InputType.PDF);
        images.add("D:\\temp\\ocr\\文件识别测试\\测试2.pdf");
        // Recognize file
        ArrayList<RecognitionResult> results = api.Recognize(images, recognitionSettings);
        results.forEach((result) -> {
            System.out.println(result.recognitionText);
        });
    }
}
