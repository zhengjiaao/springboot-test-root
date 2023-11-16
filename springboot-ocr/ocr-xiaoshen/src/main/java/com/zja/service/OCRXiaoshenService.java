/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 10:34
 * @Since:
 */
package com.zja.service;

import java.io.IOException;

/**
 * OCR服务
 *
 * @author: zhengja
 * @since: 2023/11/08 10:34
 */
public interface OCRXiaoshenService {

    String autoExtractContent(String inputFilePath);

    String accurateBasic(String inputFilePath, Integer pageNum);

    String accuratePosition(String inputFilePath, Integer pageNum);

    int getPdfPagesCount(String inputFilePath) throws IOException;
}