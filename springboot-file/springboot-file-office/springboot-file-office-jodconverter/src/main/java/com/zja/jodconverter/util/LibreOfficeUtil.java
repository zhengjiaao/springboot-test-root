package com.zja.jodconverter.util;

import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @Author: zhengja
 * @Date: 2025-04-18 11:29
 */
public class LibreOfficeUtil {

    private static final Logger logger = LoggerFactory.getLogger(LibreOfficeUtil.class);

    // 配置参数
    private static final String LIBRE_OFFICE_HOME = "D:\\APP\\LibreOffice";
    private static final int PORT_NUMBER = 8100;
    private static final long TASK_EXECUTION_TIMEOUT = 5 * 1000 * 60; // 5 minutes
    private static final long TASK_QUEUE_TIMEOUT = 1000 * 60 * 60; // 1 hour

    /**
     * 将 Office 文档转换为 目标格式
     *
     * @param sourceFilePath 源文件路径
     * @param targetFilePath 目标文件路径
     * @return 转换是否成功
     */
    public static boolean officeConvert(String sourceFilePath, String targetFilePath) {
        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(targetFilePath);
        return officeConvert(sourceFile, targetFile);
    }

    /**
     * 将 Office 文档转换为 目标格式
     *
     * @param sourceFile 源文件 pdf/docx/pptx/html/txt/md/...
     * @param targetFile 目标文件 pdf/docx/pptx/html/txt/md/...
     * @return 转换是否成功
     */
    public static boolean officeConvert(File sourceFile, File targetFile) {
        if (sourceFile == null || !sourceFile.exists()) {
            logger.error("Source file is null or does not exist: {}", sourceFile);
            return false;
        }

        if (targetFile == null) {
            logger.error("Target file is null");
            return false;
        }

        OfficeManager officeManager = null;
        try {
            LocalOfficeManager.Builder builder = LocalOfficeManager.builder();
            builder.officeHome(LIBRE_OFFICE_HOME);
            builder.portNumbers(PORT_NUMBER);
            builder.taskExecutionTimeout(TASK_EXECUTION_TIMEOUT);
            builder.taskQueueTimeout(TASK_QUEUE_TIMEOUT);

            officeManager = builder.build();
            officeManager.start();

            LocalConverter converter = LocalConverter.make(officeManager);
            logger.info("start Conversion {} to {}", sourceFile, targetFile);
            converter.convert(sourceFile).to(targetFile).execute();

            logger.info("Conversion successful: {} -> {}", sourceFile, targetFile);
            return true;
        } catch (OfficeException e) {
            logger.error("Office conversion failed: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error during conversion: {}", e.getMessage(), e);
        } finally {
            if (officeManager != null) {
                try {
                    officeManager.stop();
                } catch (OfficeException e) {
                    logger.error("Error stopping OfficeManager: {}", e.getMessage(), e);
                }
            }
        }

        return false;
    }
}
