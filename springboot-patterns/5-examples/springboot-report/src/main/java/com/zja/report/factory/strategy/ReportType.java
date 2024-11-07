package com.zja.report.factory.strategy;

import lombok.Getter;

/**
 * @Author: zhengja
 * @Date: 2024-11-05 10:32
 */
@Getter
public enum ReportType {

    // 支持一个业务类型对应多个模版，例如：规划条件核提-产品 对应 100、101
    // 合规性审查报告管理(调用计算生成)
    COMPLIANCE_REVIEW_200(200, "", "", "合规性审查报告"),

    // 测试模版
    TEST_PDF_10000(10000, "", "", "pdf-测试"),
    TEST_WORD_10001(10001, "/template/XX报告模版.docx", "/template/XX报告模版-样例.pdf", "word-测试");

    private final Integer templateCode;
    private final String templatePath;
    private final String templateExamplePath;
    private final String businessType;

    ReportType(Integer templateCode, String templatePath, String templateExamplePath, String businessType) {
        this.templateCode = templateCode;
        this.templatePath = templatePath;
        this.templateExamplePath = templateExamplePath;
        this.businessType = businessType;
    }

    public static ReportType findByCode(Integer code) {
        for (ReportType type : values()) {
            if (type.getTemplateCode().equals(code)) {
                return type;
            }
        }

        throw new RuntimeException("未找到对应的枚举类型code：【" + code + "】");
    }

    public static ReportType findByCodeAndBusinessType(Integer code, String businessType) {
        for (ReportType type : values()) {
            if (type.getTemplateCode().equals(code) && type.getBusinessType().equals(businessType)) {
                return type;
            }
        }

        throw new RuntimeException("未找到对应的枚举类型code=【" + code + "】,businessType=【" + businessType + "】");
    }

    public static void check(Integer code, String businessType) {
        findByCodeAndBusinessType(code, businessType);
    }

    public static void check(Integer code) {
        findByCode(code);
    }

}
