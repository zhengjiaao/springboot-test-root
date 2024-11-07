package com.zja.report.service;

import com.alibaba.fastjson.JSONObject;
import com.zja.report.factory.ReportFactory;
import com.zja.report.factory.strategy.ReportStrategy;
import com.zja.report.factory.strategy.ReportType;
import com.zja.report.model.dto.StorageDTO;
import com.zja.report.model.request.ReportRequest;
import com.zja.report.util.ContextPathUtil;
import com.zja.report.util.ResourcesFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: zhengja
 * @Date: 2024-11-05 15:42
 */
@Slf4j
@Component
public class ReportService {

    @Autowired
    ReportFactory reportFactory;

    @Autowired
    StorageFileService storageFileService;

    /**
     * 生成报告
     *
     * @param request 业务参数
     * @return String 报告id(reportId)
     */
    public String generateReport(ReportRequest request) throws IOException {
        ReportStrategy reportStrategy = reportFactory.getReportStrategy(request.getTemplateCode());
        String reportPath = reportStrategy.generate(request);

        if (StringUtils.isEmpty(reportPath) || !new File(reportPath).exists()) {
            throw new RuntimeException("报告生成失败!");
        }

        try {
            return storageFileService.uploadFile(reportPath).getFileId();
        } finally {
            log.debug("删除临时文件: {}", reportPath);
            Files.deleteIfExists(Paths.get(reportPath));
        }
    }
    public String previewReport(String reportId) {
        return storageFileService.getFilePreviewUrl(reportId);
    }

    public void downloadReport(String reportId, HttpServletResponse response) throws IOException {
        if (storageFileService.exists(reportId)) {
            storageFileService.downloadFile(reportId, response);
        } else {
            throw new RuntimeException("报告不存在!");
        }
    }

    public boolean deleteReport(String reportId) {
        return storageFileService.deleteFile(reportId);
    }

    public List<JSONObject> templateList() {
        Set<Integer> templateCodeList = reportFactory.templateCodeList();
        if (templateCodeList == null) {
            return new ArrayList<>();
        }

        List<JSONObject> result = new ArrayList<>();
        for (Integer templateCode : templateCodeList) {
            ReportType reportType = ReportType.findByCode(templateCode);
            result.add(toJSONObject(reportType));
        }
        return result;
    }

    private JSONObject toJSONObject(ReportType reportType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("templateCode", reportType.getTemplateCode());
        jsonObject.put("templatePath", reportType.getTemplatePath());
        jsonObject.put("templateExamplePath", reportType.getTemplateExamplePath());
        jsonObject.put("businessType", reportType.getBusinessType());
        return jsonObject;
    }

    public String previewTemplate(Integer templateCode) {
        ReportStrategy reportStrategy = reportFactory.getReportStrategy(templateCode);
        String templatePath = reportStrategy.getTemplate();

        String fileName = FilenameUtils.getName(templatePath);

        try (InputStream inputStream = ResourcesFileUtil.getResourcesFileInputStream(templatePath)) {
            StorageDTO uploadFile = storageFileService.uploadFile(fileName, inputStream);
            String fileId = uploadFile.getFileId();
            storageFileService.deleteFileByDelayed(fileId, 60000 * 30); // 延迟30分钟删除

            return storageFileService.getFilePreviewUrl(fileId);
        } catch (Exception e) {
            throw new RuntimeException("模版文件下载失败", e);
        }
    }

    public String previewTemplateExampleStatic(ReportRequest request) {
        ReportStrategy reportStrategy = reportFactory.getReportStrategy(request.getTemplateCode());
        String templateExamplePath = reportStrategy.getTemplateExample();
        return ContextPathUtil.getAPIPathURL(templateExamplePath);
    }

    public String previewTemplateExampleDynamic(ReportRequest request) throws IOException {
        String reportId = generateReport(request);
        storageFileService.deleteFileByDelayed(reportId, 60000 * 30); // 延迟30分钟删除
        return previewReport(reportId);
    }

    public void downloadTemplate(Integer templateCode, HttpServletResponse response) {
        ReportStrategy reportStrategy = reportFactory.getReportStrategy(templateCode);
        String templatePath = reportStrategy.getTemplate();
        String fileName = FilenameUtils.getName(templatePath);

        try (InputStream inputStream = ResourcesFileUtil.getResourcesFileInputStream(templatePath)) {
            // 设置下载时的文件名称
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment; filename=" + encodedFileName);
            // 设置响应头以支持文件下载
            response.setContentType("application/octet-stream");

            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("模版文件下载失败", e);
        }
    }

}
