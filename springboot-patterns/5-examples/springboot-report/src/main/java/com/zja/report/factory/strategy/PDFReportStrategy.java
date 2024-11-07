package com.zja.report.factory.strategy;

import com.zja.report.model.request.ReportRequest;
import com.zja.report.util.ContextPathUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: zhengja
 * @Date: 2024-11-05 10:01
 */
@Slf4j
@Component
public class PDFReportStrategy extends ReportGenerateHandler implements ReportStrategy {

    public static final ReportType REPORT_TYPE = ReportType.TEST_PDF_10000;

    @Override
    public Integer getType() {
        return REPORT_TYPE.getTemplateCode();
    }

    @Override
    public String getTemplate() {
        return REPORT_TYPE.getTemplatePath();
    }

    @Override
    public String getTemplateExample() {
        return REPORT_TYPE.getTemplateExamplePath();
    }

    @Override
    public String generate(Object param) {
        log.debug("正在生成Word报告...");
        try {
            ReportRequest request = (ReportRequest) param;
            // 校验参数
            checkParam(request);

            // 获取模版路径
            String template = getTemplate();

            // 构建报告参数（模版数据）
            Map<String, Object> reportRequest = buildReportRequest(request);

            // 本地调用生成报告(调用本地poi-tl)
            String reportPath = localCallToGenerationReport(template, reportRequest);
            log.debug("Word报告生成成功，路径为：{}", reportPath);

            return reportPath;
        } catch (Exception e) {
            log.error("生成Word报告时出错.", e);
            throw new RuntimeException("生成Word报告时出错.", e);
        }
    }

    // 校验参数
    private void checkParam(ReportRequest request) {
        if (StringUtils.isEmpty(request.getBusinessId())) {
            throw new RuntimeException("传入的参数 businessId 为空！");
        }
    }

    // 构建报告参数（模版数据）
    private Map<String, Object> buildReportRequest(ReportRequest request) {

        return null;
    }

    // 本地调用生成报告(调用本地poi-tl)
    private String localCallToGenerationReport(String template, Map<String, Object> reportRequest) throws Exception {
        String reportFilePath = ContextPathUtil.getTempFilePath("报告.docx");
        generateWord(reportFilePath, template, reportRequest);
        return reportFilePath;
    }
}
