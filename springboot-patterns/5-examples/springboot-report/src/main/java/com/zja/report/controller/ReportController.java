package com.zja.report.controller;

import com.alibaba.fastjson.JSONObject;
import com.zja.report.factory.strategy.ReportType;
import com.zja.report.model.request.ReportRequest;
import com.zja.report.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-11-05 10:03
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/rest/report")
@Api(tags = {"BASE-报告生成服务管理页面"})
public class ReportController {

    @Autowired
    ReportService reportService;

    @PostMapping("/generate")
    @ApiOperation("生成报告")
    public String generateReport(@Valid @RequestBody ReportRequest request) throws IOException {
        // 此处有必要验证业务类型，考虑后期方便维护
        ReportType.check(request.getTemplateCode());
        return reportService.generateReport(request);
    }

    @GetMapping("/preview/{reportId}")
    @ApiOperation("预览报告")
    public String previewReport(@NotBlank @PathVariable("reportId") String reportId) {
        return reportService.previewReport(reportId);
    }

    @GetMapping("/download/{reportId}")
    @ApiOperation("下载报告")
    public void downloadReport(@NotBlank @PathVariable("reportId") String reportId, HttpServletResponse response) throws IOException {
        reportService.downloadReport(reportId, response);
    }

    @DeleteMapping("/delete/{reportId}")
    @ApiOperation("删除报告")
    public boolean deleteReport(@NotBlank @PathVariable("reportId") String reportId) {
        return reportService.deleteReport(reportId);
    }

    @PostMapping("/preview/example/static")
    @ApiOperation(value = "预览报告-示例效果（静态）", notes = "此接口一般是手动转换后的pdf格式报告示例，若要实时显示生成报告内容效果，则推荐调用[预览报告-示例效果（动态）]接口，返回报告静态预览地址")
    public String previewTemplateExampleStatic(@Valid @RequestBody ReportRequest request) {
        ReportType.check(request.getTemplateCode());
        return reportService.previewTemplateExampleStatic(request);
    }

    @PostMapping("/preview/example/dynamic")
    @ApiOperation(value = "预览报告-示例效果（动态）", notes = "此接口是实时显示生成的报告示例，返回报告动态预览地址")
    public String previewTemplateExampleDynamic(@Valid @RequestBody ReportRequest request) throws IOException {
        ReportType.check(request.getTemplateCode());
        return reportService.previewTemplateExampleDynamic(request);
    }

    @GetMapping("/template/list")
    @ApiOperation("模版列表-在用的")
    public List<JSONObject> templateList() {
        return reportService.templateList();
    }

    @GetMapping("/template/preview/{templateCode}")
    @ApiOperation("预览模版")
    public String previewTemplate(@PathVariable("templateCode") Integer templateCode) {
        ReportType.check(templateCode);
        return reportService.previewTemplate(templateCode);
    }

    @GetMapping("/template/download/{templateCode}")
    @ApiOperation("下载模版")
    public void downloadTemplate(@PathVariable("templateCode") Integer templateCode, HttpServletResponse response) {
        ReportType.check(templateCode);
        reportService.downloadTemplate(templateCode, response);
    }

}