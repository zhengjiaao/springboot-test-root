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
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * 中文支持效果不好,识别内容不全
 *
 * @author: zhengja
 * @since: 2023/11/03 9:25
 */
@Deprecated //todo 未破解成功
public class OCRTest {

    //支持英语、法语、西班牙语和葡萄牙语
    //OCR 支持 JPG、PNG、GIF、BMP 和 TIFF 图像文件格式
    @Test
    public void ocr_png_test() throws IOException {
    /*    InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);
*/
        AsposeOCR api = new AsposeOCR();
        RecognitionSettings recognitionSettings = new RecognitionSettings();
        recognitionSettings.setLinesFiltration(false);
        recognitionSettings.setDetectAreas(true);
        recognitionSettings.setAllowedCharacters(CharactersAllowedType.ALL);
        recognitionSettings.setLanguage(Language.Chi);
        RecognitionResult result = api.RecognizePage("D:\\temp\\ocr\\input.png", recognitionSettings);
        System.out.println("Recognition result:\n" + result.recognitionText + "\n\n");
    }

}