package com.zja.poitl.util;

import com.alibaba.fastjson.JSON;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 根据word模版生成word文档
 *
 * @author: zhengja
 * @since: 2024/04/02 10:13
 */
@Slf4j
public class WordPoiTLUtil {

    private WordPoiTLUtil() {
    }

    /**
     * 根据word模版生成word文档
     *
     * @param wordPath     word生成后的存路径
     * @param data         数据
     * @param templatePath 模版路径，在resources下面的模版
     * @throws IOException IO异常
     */
    public static void generateWord(String wordPath, Map<String, Object> data, String templatePath) throws IOException {
        Configure configure = Configure.builder().useSpringEL(true).build();
        generateWord(wordPath, data, templatePath, configure);
    }

    /**
     * 根据word模版生成word文档
     *
     * @param wordPath       word生成后的存路径
     * @param data           数据
     * @param templateStream 模版流
     * @throws IOException IO异常
     */
    public static void generateWord(String wordPath, Map<String, Object> data, InputStream templateStream) throws IOException {
        Configure configure = Configure.builder().useSpringEL(true).build();
        generateWord(wordPath, data, templateStream, configure);
    }

    /**
     * 根据word模版生成word文档
     *
     * @param wordPath       word生成后的存路径
     * @param data           数据
     * @param templateStream 模版流
     * @param configure      poi-tl 配置
     * @throws IOException IO异常
     */
    public static void generateWord(String wordPath, Map<String, Object> data, InputStream templateStream, Configure configure) throws IOException {
        Path wordFilePath = Paths.get(wordPath);
        createParentDirectories(wordFilePath);
        if (templateStream == null) {
            throw new IOException("templateStream is null.");
        }

        try {
            XWPFTemplate xwpfTemplate = XWPFTemplate.compile(templateStream, configure).render(data);
            try (OutputStream outputStream = Files.newOutputStream(wordFilePath)) {
                xwpfTemplate.writeAndClose(outputStream);
            }
        } catch (IOException e) {
            log.error("Error generating word: {}", e.getMessage(), e);
            throw e;
        } finally {
            log.debug("生成路径 wordPath: {}  \n 模版数据：{}", wordPath, JSON.toJSONString(data));
        }
    }

    /**
     * 根据word模版生成word文档
     *
     * @param wordPath     word生成后的存路径
     * @param data         数据
     * @param templatePath 模版路径，在resources下面的模版
     * @param configure    poi-tl 配置
     * @throws IOException IO异常
     */
    public static void generateWord(String wordPath, Map<String, Object> data, String templatePath, Configure configure) throws IOException {
        Path wordFilePath = Paths.get(wordPath);
        createParentDirectories(wordFilePath);

        try (InputStream templateStream = getInputStream(templatePath)) {
            if (templateStream == null) {
                throw new FileNotFoundException("Template not found: " + templatePath);
            }

            XWPFTemplate xwpfTemplate = XWPFTemplate.compile(templateStream, configure).render(data);
            try (OutputStream outputStream = Files.newOutputStream(wordFilePath)) {
                xwpfTemplate.writeAndClose(outputStream);
            }
        } catch (IOException e) {
            log.error("Error generating word: {}", e.getMessage(), e);
            throw e;
        } finally {
            log.debug("生成路径 wordPath: {} \n templatePath: {} \n 模版数据：{}", wordPath, templatePath, JSON.toJSONString(data));
        }
    }

    /**
     * 读取文件流，优先读取classpath下的文件，如未找到，则读取本地文件
     */
    public static InputStream getInputStream(String fileName) throws IOException {
        try {
            return getResourcesFileInputStream(fileName);
        } catch (IOException e) {
            try {
                return getFileInputStream(fileName);
            } catch (IOException e1) {
                e.addSuppressed(e1);
                throw new IOException("file not found：" + fileName, e);
            }
        }
    }

    // 读取本地文件流
    private static InputStream getFileInputStream(String filePath) throws IOException {
        return Files.newInputStream(Paths.get(filePath));
    }

    // 读取资源文件流（支持读取jar下面的资源文件）
    private static InputStream getResourcesFileInputStream(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        return resource.getInputStream();
    }

    // 创建父目录路径
    private static void createParentDirectories(Path path) throws IOException {
        Files.createDirectories(path.getParent());
    }
}
