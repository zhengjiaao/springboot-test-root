package com.zja.report.factory.strategy;


/**
 * @Author: zhengja
 * @Date: 2024-11-05 10:01
 */
public interface ReportStrategy {

    /**
     * 获取报告类型
     *
     * @return Integer 参考：{@link ReportType#templateCode} 示例：100、101
     */
    Integer getType();

    /**
     * 获取报告模版路径
     *
     * @return String 参考：{@link ReportType#templatePath} 示例： /template/XX报告模版.docx
     */
    String getTemplate();

    /**
     * 获取报告模版示例路径
     *
     * @return String 参考：{@link ReportType#templateExamplePath} 示例： /template/规划条件提取报告模版示例.docx
     */
    String getTemplateExample();

    /**
     * 生成报告
     *
     * @param param 业务参数(任意类型)
     * @return String 生成的报告路径
     */
    String generate(Object param);

}