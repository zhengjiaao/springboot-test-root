package com.zja.report.factory.strategy;

import com.alibaba.fastjson.JSON;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 报告生成处理器
 *
 * @Author: zhengja
 * @Date: 2024-11-05 13:16
 */
@Slf4j
public class ReportHandler {

    public String generateWord(String templatePath, Map<String, Object> data) throws IOException {
        return generateWord("报告.docx", templatePath, data);
    }

    public String generateWord(String reportName, String templatePath, Map<String, Object> data) throws IOException {
        String dirName = UUID.randomUUID().toString();
        Path outputDir = Paths.get(System.getProperty("user.dir"), dirName);
        Path outputPath = outputDir.resolve(reportName);

        // 创建目标目录
        createParentDirectories(outputPath);

        // 编译模板
        XWPFTemplate template = null;
        try {
            template = XWPFTemplate.compile(
                    Objects.requireNonNull(this.getClass().getResourceAsStream(templatePath)),
                    Configure.builder().useSpringEL(true).build());
        } catch (Exception e) {
            log.error("模板编译失败，请检查模板路径是否正确，模板路径为：{}", templatePath);
            throw new RuntimeException("模板编译失败，请检查模板路径是否正确，模板路径为：" + templatePath, e);
        }

        // 渲染模板并写入文件
        try (OutputStream outputStream = Files.newOutputStream(outputPath)) {
            Objects.requireNonNull(template).render(data, outputStream);
        } catch (Exception e) {
            log.error("数据写入失败，请检查模板路径是否正确，模板路径为：{}，数据：{}", templatePath, JSON.toJSONString(data));
            throw new RuntimeException("数据写入失败：", e);
        }

        log.debug("生成路径: {}，数据：{}", outputPath, JSON.toJSONString(data));

        return outputPath.toString();
    }

    private static void createParentDirectories(Path path) throws IOException {
        Files.createDirectories(path.getParent());
    }
}
