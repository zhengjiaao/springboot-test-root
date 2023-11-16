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
import com.zja.util.PDFBoxUtil;
import com.zja.util.PaddleOCRUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

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

        String fileExtension = FilenameUtils.getExtension(name).toLowerCase();
        if (isImage(fileExtension)) {
            return OCRTesseractUtil.ocrImage(inputFilePath);
        } /*else if (isPDF(fileExtension)) {
            //result = OCRmyPDFUtil.ocrPdf(inputFilePath); // 效果差,整体效果差，图文识别效果很差
            //result = OCRTesseractUtil.ocrPdf(inputFilePath); // 效果差(图的文字识别差)
        } */ else {
            return OCRApacheTikaUtil.autoExtractedContent(inputFilePath);
        }
    }

    @Override
    public String accurateBasic(String inputFilePath, Integer pageNum) {
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            throw new RuntimeException("文件不存在：" + inputFilePath);
        }

        String name = inputFile.getName();
        String fileExtension = FilenameUtils.getExtension(name).toLowerCase();

        try {
            if (isImage(fileExtension)) {
                return PaddleOCRUtil.ocrImagesNoCoordinates(Collections.singletonList(inputFilePath), false);
            } else if (isPDF(fileExtension)) {
                return PaddleOCRUtil.ocrPdfNoCoordinates(inputFilePath, ObjectUtils.isEmpty(pageNum) ? 0 : pageNum, false);
            } else {
                throw new RuntimeException("不支持的文件格式:[" + fileExtension + "],请尝试先转为图像或PDF.");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String accuratePosition(String inputFilePath, Integer pageNum) {
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            throw new RuntimeException("文件不存在：" + inputFilePath);
        }

        String name = inputFile.getName();
        String fileExtension = FilenameUtils.getExtension(name).toLowerCase();

        try {
            if (isImage(fileExtension)) {
                return PaddleOCRUtil.ocrImages(Collections.singletonList(inputFilePath), false);
            } else if (isPDF(fileExtension)) {
                return PaddleOCRUtil.ocrPdf(inputFilePath, ObjectUtils.isEmpty(pageNum) ? 0 : pageNum, false);
            } else {
                throw new RuntimeException("不支持的文件格式:[" + fileExtension + "],请尝试先转为图像或PDF.");
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getPdfPagesCount(String inputFilePath) throws IOException {
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            throw new RuntimeException("文件不存在：" + inputFilePath);
        }

        String name = inputFile.getName();
        String fileExtension = FilenameUtils.getExtension(name).toLowerCase();

        if (isPDF(fileExtension)) {
            return PDFBoxUtil.getPdfPagesCount(inputFilePath);
        }

        throw new RuntimeException("仅支持pdf，不支持的类型：" + fileExtension);
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