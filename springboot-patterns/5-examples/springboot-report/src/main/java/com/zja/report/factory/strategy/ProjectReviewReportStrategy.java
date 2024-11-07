package com.zja.report.factory.strategy;

import com.zja.report.model.request.ReportRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 合规性审查 报告生成
 *
 * @Author: zhengja
 * @Date: 2024-11-06 14:52
 */
@Slf4j
@Component
public class ProjectReviewReportStrategy extends ReportGenerateHandler implements ReportStrategy {

    public static final ReportType REPORT_TYPE = ReportType.COMPLIANCE_REVIEW_200;

    // @Autowired
    // ProjectReviewRepo repo;
    //
    // @Autowired
    // PlanCheckFeignClient planCheckFeignClient;

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

            // 构建报告参数
            // PlanCheckReportRequest reportRequest = buildReportRequest(request);

            // 远程调用生成报告
            /*String reportPath = remoteCallToGenerationReport(reportRequest);
            log.debug("Word报告生成成功，路径为：{}", reportPath);

            return reportPath;*/
            return "";
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
        if (request.getBusinessJsonData() == null) {
            throw new RuntimeException("传入的参数 businessJsonData 为空！");
        }
    }

    // 构建报告参数
    /*private PlanCheckReportRequest buildReportRequest(ReportRequest request) {
        ProjectReviewReportRequest reportRequest = request.getBusinessJsonData().toJavaObject(ProjectReviewReportRequest.class);
        return buildPlanCheckReportRequest(request.getBusinessId(), reportRequest);
    }*/

    // 远程调用生成报告（调用计算）
    /*private String remoteCallToGenerationReport(PlanCheckReportRequest reportRequest) throws IOException {
        String reportFilePath = ContextPathUtil.getTempFilePath("报告.docx");
        // planCheckFeignClient.generateReviewReport(reportRequest, reportFilePath);
        return reportFilePath;
    }*/
}
