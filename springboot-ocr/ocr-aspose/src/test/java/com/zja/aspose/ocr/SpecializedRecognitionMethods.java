/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-14 10:01
 * @Since:
 */
package com.zja.aspose.ocr;

import com.aspose.ocr.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Aspose.OCR for Java 提供了微调的 OCR 方法，用于从某些类型的图像中提取文本：
 * 从身份证中提取文本：自动读取扫描或拍照的身份证中的文本。
 * 从护照中提取文本：将护照的扫描件或照片数字化。
 * 从车辆牌照中提取文本：无需手动重新输入即可读取车辆牌照。
 * 从发票中提取文本：将扫描的发票数字化，无需手动重新打字。
 * 从收据中提取文本：将扫描的收据数字化，无需手动重新打字。
 * 从街道照片中提取文本：从街道照片、价格标签、菜单、目录和其他具有稀疏文本和嘈杂/彩色背景的图像中提取文本。
 *
 * @author: zhengja
 * @since: 2023/11/14 10:01
 */
@Deprecated // 读取特定识别场景效果不好，比如身份证件
public class SpecializedRecognitionMethods {

    /**
     * 身份证识别：身份证、工作许可证、居住许可证、护照卡或其他身份证明文件。
     * todo 效果不好
     *
     * @throws IOException
     */
    @Test
    public void ocr_IDCardRecognition_test() throws IOException {
        AsposeOCR api = new AsposeOCR();
        // Add images to the recognition batch
        OcrInput input = new OcrInput(InputType.SingleImage);
        input.add("D:\\temp\\ocr\\身份证件识别\\身份证-正面.png");
        input.add("D:\\temp\\ocr\\身份证件识别\\身份证-背面.png");
        // Recognition settings
        IDCardRecognitionSettings recognitionSettings = new IDCardRecognitionSettings();
        recognitionSettings.setAllowedCharacters(CharactersAllowedType.LATIN_ALPHABET);
        recognitionSettings.setLanguage(Language.Chi);
        // Recognize ID cards
        ArrayList<RecognitionResult> results = api.RecognizeIDCard(input, recognitionSettings);
        results.forEach((result) -> {
            System.out.println(result.recognitionText);
        });
    }


}
