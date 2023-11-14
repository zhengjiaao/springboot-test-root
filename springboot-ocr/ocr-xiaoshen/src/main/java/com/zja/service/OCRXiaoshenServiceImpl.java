/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 10:35
 * @Since:
 */
package com.zja.service;

import com.zja.define.ImageEnum;
import com.zja.util.OCRApacheTikaUtil;
import com.zja.util.OCRTesseractUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import java.io.File;

/**
 * OCR 服务
 *
 * @author: zhengja
 * @since: 2023/11/08 10:35
 */
@Slf4j
@Validated
@Service
public class OCRXiaoshenServiceImpl implements OCRXiaoshenService {

    @Override
    public String autoExtractContent(String inputFilePath) {
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            throw new RuntimeException("文件不存在：" + inputFilePath);
        }

        String name = inputFile.getName();

        String result = "";

        String fileExtension = FilenameUtils.getExtension(name).toLowerCase();
        if (isImage(fileExtension)) {
            result = OCRTesseractUtil.ocrImage(inputFilePath);
        } /*else if (isPDF(fileExtension)) {
            //result = OCRmyPDFUtil.ocrPdf(inputFilePath); // 效果差,整体效果差，图文识别效果很差
            //result = OCRTesseractUtil.ocrPdf(inputFilePath); // 效果差(图的文字识别差)
        } */else {
            result = OCRApacheTikaUtil.autoExtractedContent(inputFilePath);
        }

        return result;
    }

    private boolean isImage(String fileExtension) {
        ImageEnum imageEnum = ImageEnum.get(fileExtension.toLowerCase());

        if (ObjectUtils.isEmpty(imageEnum)) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private boolean isPDF(String fileExtension) {
        return "pdf".equalsIgnoreCase(fileExtension) ? Boolean.TRUE : Boolean.FALSE;
    }

}