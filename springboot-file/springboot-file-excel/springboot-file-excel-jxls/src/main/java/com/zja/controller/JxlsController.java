/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-07-28 10:35
 * @Since:
 */
package com.zja.controller;

import com.alibaba.fastjson.JSONObject;
import com.zja.model.Employee;
import com.zja.util.JxlsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Jxls Excel 操作
 *
 * @author: zhengja
 * @since: 2023/07/28 10:35
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/rest/jxls")
@Api(tags = {"Jxls Excel 操作页面"})
public class JxlsController {

    @PostMapping(value = "/excel/Import")
    @ApiOperation(value = "excel导入")
    public Boolean excelImport(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file) {
        System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());


        return true;
    }

    @GetMapping(value = "/excel/export/v1")
    @ApiOperation(value = "excel导出", notes = "简单示例")
    public void excelExport(HttpServletResponse response) throws IOException {

        //如果想下载试自动填好文件名，需要设置Content-Disposition响应头
        response.setHeader("Content-Disposition", "attachment;filename=" + "object_collection_output.xls");
        response.setContentType("application/vnd.ms-excel");

        log.info("Running Object Collection demo");
        List<Employee> employees = generateSampleEmployeeData();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("templates/object_collection_template.xls")) {
            //本地保存
//            try (OutputStream os = new FileOutputStream("object_collection_output.xls")) {
            //直接下载
            try (OutputStream os = response.getOutputStream()) {
                Context context = new Context();
                context.putVar("employees", employees);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        }
    }


    private static final String JSON_DATA = "{\"summary\":\"汇总\",\"performList\":[{\"adoptDate\":1672800485279,\"approveCode\":\"003\",\"bjmc\":\"name2\",\"dynzybjmc\":\"123\",\"hbghed\":1.0000,\"hbxzjshydmj\":1.0000,\"nzypwh\":\"123\",\"pid\":\"f87b0b85-5787-407c-aeec-84aa4379df40\",\"pzsj\":\"123\",\"qzghxzjsyd\":1.00,\"regionCity\":\"某市\",\"sjysyghed\":1.00,\"submitStatus\":1,\"targetDate\":\"2023\",\"xmmc\":\"123\",\"zjdydkxmmc\":\"\",\"zjdynzybjmc\":\"\",\"zjnzyslh\":\"\"},{\"adoptDate\":1672804391971,\"approveCode\":\"004\",\"bjmc\":\"某市2023年度测试某啥啥第2次使用\",\"dynzybjmc\":\"123\",\"hbghed\":21.0000,\"hbxzjshydmj\":21.0000,\"nzypwh\":\"123\",\"pid\":\"123-419b-8fe9-a654dfa9c500\",\"pzsj\":\"123\",\"qzghxzjsyd\":1.00,\"regionCity\":\"某市\",\"sjysyghed\":1.00,\"submitStatus\":1,\"targetDate\":\"2023\",\"xmmc\":\"测试\",\"zjdydkxmmc\":\"123\",\"zjdynzybjmc\":\"123\",\"zjnzyslh\":\"123\",\"zjsyghed\":20,\"zjxzjsyd\":20}],\"hbxzjshydmj\":22.0000,\"hbghed\":22.0000}";


    @GetMapping(value = "/excel/export/v2")
    @ApiOperation(value = "excel导出", notes = "添加自定义工具类(函数)")
    public void excelExport2(HttpServletResponse response) throws IOException {

        JSONObject jsonObject = JSONObject.parseObject(JSON_DATA);
        Map<String, Object> map = (Map<String, Object>) jsonObject;

        //如果想下载试自动填好文件名，需要设置Content-Disposition响应头
        response.setHeader("Content-Disposition", "attachment;filename=" + "map_output.xlsx");
        response.setContentType("application/vnd.ms-excel");

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("templates/map_template.xlsx")) {
            //本地保存
//            try (OutputStream os = new FileOutputStream("map_output.xlsx")) {
            //直接下载
            try (OutputStream os = response.getOutputStream()) {
                Context context = new Context(map);
                //context.putVar("data", map); //data.performList

                //测试工具类处理时间
                context.putVar("newDate", new Date());
                context.putVar("newLocalDate", LocalDateTime.now());
                context.putVar("stringDate", "2023-01-05");
                context.putVar("timeStamp", 1672800485279L);
                context.putVar("dateFormatter", "yyyy-MM-dd");
                context.putVar("dateTimeFormatter", "yyyy-MM-dd HH:mm:ss");


                JxlsHelper jxlsHelper = JxlsHelper.getInstance();
                Transformer transformer = jxlsHelper.createTransformer(is, os);
                //获得配置
                JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig()
                        .getExpressionEvaluator();
                //函数强制，自定义功能
                Map<String, Object> funcs = new HashMap<String, Object>();
                funcs.put("utils", new JxlsUtils()); //添加自定义功能
                JexlBuilder jb = new JexlBuilder();
                jb.namespaces(funcs);
//                jb.silent(true); //设置静默模式，不报警告
                JexlEngine je = jb.create();
                evaluator.setJexlEngine(je);

                //解决两个问题：
                // 1、单元格无法自增序列号
                // 2、时间戳过长无法显示 “#####” (设置时间格式也不能用)
                JxlsHelper.getInstance().processTemplate(context, transformer);
            }
        }
    }

    @GetMapping(value = "/excel/export/v3")
    @ApiOperation(value = "excel导出", notes = "设置公式 Excel和jxls方式")
    public void excelExport3(HttpServletResponse response) throws IOException {

        //如果想下载试自动填好文件名，需要设置Content-Disposition响应头
        response.setHeader("Content-Disposition", "attachment;filename=" + "function_output.xlsx");
        response.setContentType("application/vnd.ms-excel");

        log.info("Running Object Collection demo");
        List<Employee> employees = generateSampleEmployeeData();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("templates/function_output_template.xlsx")) {
            //本地保存
//            try (OutputStream os = new FileOutputStream("object_collection_output.xls")) {
            //直接下载
            try (OutputStream os = response.getOutputStream()) {
                Context context = new Context();
                context.putVar("money1", 80);
                context.putVar("money2", 20);
                //以下两种都生效
                JxlsHelper.getInstance().processTemplate(is, os, context);
//                JxlsHelper.getInstance().processTemplateAtCell(is, os, context, "Result!A1");
            }
        }
    }

    @GetMapping(value = "/excel/export/v4")
    @ApiOperation(value = "excel导出", notes = "设置公式")
    public void excelExport4(HttpServletResponse response) throws IOException {

        //如果想下载试自动填好文件名，需要设置Content-Disposition响应头
        response.setHeader("Content-Disposition", "attachment;filename=" + "param_formulas_output.xls");
        response.setContentType("application/vnd.ms-excel");

        log.info("Running Object Collection demo");
        List<Employee> employees = generateSampleEmployeeData();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("templates/param_formulas_template.xls")) {
            //本地保存
//            try (OutputStream os = new FileOutputStream("param_formulas_output.xls")) {
            //直接下载
            try (OutputStream os = response.getOutputStream()) {
                Context context = new Context();
                context.putVar("employees", employees);
                context.putVar("bonus", 0.1);
                JxlsHelper.getInstance().processTemplateAtCell(is, os, context, "Result!A1");
            }
        }
    }

    // 以下是内部方法

    private List<Employee> generateSampleEmployeeData() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(new Employee("李四1", new Date(), BigDecimal.valueOf(123), BigDecimal.valueOf(456)));
        employeeList.add(new Employee("张三", new Date(), BigDecimal.valueOf(123), BigDecimal.valueOf(456)));
        employeeList.add(new Employee("李四3", new Date(), BigDecimal.valueOf(123), BigDecimal.valueOf(456)));
        employeeList.add(new Employee("张三4", new Date(), BigDecimal.valueOf(123), BigDecimal.valueOf(456)));
        return employeeList;
    }

}