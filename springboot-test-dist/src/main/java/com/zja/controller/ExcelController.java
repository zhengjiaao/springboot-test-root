package com.zja.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zja.util.excel.ExportExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/26 13:12
 */
@Api(tags = {"ExcelController"},description = "生成excel测试")
@RestController
@RequestMapping(value = "rest/excel")
public class ExcelController {

    @ApiOperation(value = "生成excel",httpMethod = "GET",notes = "ExportExcel")
    @RequestMapping(value = "v1/excel",method = RequestMethod.GET)
    public Object excel(){

        Map<String,Integer> map = new HashMap();
        map.put("李四",1);
        map.put("政治零",5);
        map.put("张三",10);
        map.put("欧阳丽娜",15);

        List<String> headListCN = Arrays.asList("用户名", "登录次数");

        //创建excel对象
        ExportExcel excel = new ExportExcel();
        //设置页名称
        excel.initSheet("系统访客量");
        //设置文档第一标题行
        excel.setExcelTitle(headListCN);
        //给表格赋值
        excel.setExcelData(map);
        //表格移动，改变格式
        //excel.dataShiftPosition(3);
        //设置表格样式
        excel.setExcelCellStyle();
        //自适应表格的长度，
        excel.autoSheet();
        String uuid = UUID.randomUUID().toString();
        String path = "c:/DIST/" + uuid + ".xls";
        excel.saveToPath(path);
        System.out.println(path);
        return path;
    }

    @ApiOperation(value = "生成excel2",httpMethod = "GET",notes = "ExportExcel")
    @RequestMapping(value = "v2/excel",method = RequestMethod.GET)
    public Object excel2(){

        String json = "{\n" +
                "\t\"data\": [{\n" +
                "\t\t\"Code\": \"R\",\n" +
                "\t\t\"Name\": \"居住用地\",\n" +
                "\t\t\"Count\": 2339,\n" +
                "\t\t\"Area\": 97285261.69,\n" +
                "\t\t\"Ratio\": 0.2402,\n" +
                "\t\t\"CountRatio\": 0.1358,\n" +
                "\t\t\"ChildItems\": [{\n" +
                "\t\t\t\"Code\": \"R1\",\n" +
                "\t\t\t\"Name\": \"一类居住用地\",\n" +
                "\t\t\t\"Count\": 112,\n" +
                "\t\t\t\"Area\": 2331597.17,\n" +
                "\t\t\t\"Ratio\": 0.0058,\n" +
                "\t\t\t\"CountRatio\": 0.0065,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"R11\",\n" +
                "\t\t\t\t\"Name\": \"住宅用地\",\n" +
                "\t\t\t\t\"Count\": 94,\n" +
                "\t\t\t\t\"Area\": 1861243.16,\n" +
                "\t\t\t\t\"Ratio\": 0.0046,\n" +
                "\t\t\t\t\"CountRatio\": 0.0055,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"R12\",\n" +
                "\t\t\t\t\"Name\": \"服务设施用地\",\n" +
                "\t\t\t\t\"Count\": 1,\n" +
                "\t\t\t\t\"Area\": 2442.29,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0001,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"R2\",\n" +
                "\t\t\t\"Name\": \"二类居住用地\",\n" +
                "\t\t\t\"Count\": 1586,\n" +
                "\t\t\t\"Area\": 79788328.74,\n" +
                "\t\t\t\"Ratio\": 0.197,\n" +
                "\t\t\t\"CountRatio\": 0.0921,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"R21\",\n" +
                "\t\t\t\t\"Name\": \"住宅用地\",\n" +
                "\t\t\t\t\"Count\": 1202,\n" +
                "\t\t\t\t\"Area\": 65197663.06,\n" +
                "\t\t\t\t\"Ratio\": 0.161,\n" +
                "\t\t\t\t\"CountRatio\": 0.0698,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"R22\",\n" +
                "\t\t\t\t\"Name\": \"服务设施用地\",\n" +
                "\t\t\t\t\"Count\": 43,\n" +
                "\t\t\t\t\"Area\": 322496.34,\n" +
                "\t\t\t\t\"Ratio\": 0.0008,\n" +
                "\t\t\t\t\"CountRatio\": 0.0025,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"R3\",\n" +
                "\t\t\t\"Name\": \"三类居住用地\",\n" +
                "\t\t\t\"Count\": 0,\n" +
                "\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"R31\",\n" +
                "\t\t\t\t\"Name\": \"住宅用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"R32\",\n" +
                "\t\t\t\t\"Name\": \"服务设施用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"RB\",\n" +
                "\t\t\t\"Name\": \"商住混合用地\",\n" +
                "\t\t\t\"Count\": 540,\n" +
                "\t\t\t\"Area\": 12957894.14,\n" +
                "\t\t\t\"Ratio\": 0.032,\n" +
                "\t\t\t\"CountRatio\": 0.0313,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"Ra\",\n" +
                "\t\t\t\"Name\": \"其他居住用地\",\n" +
                "\t\t\t\"Count\": 101,\n" +
                "\t\t\t\"Area\": 2207441.64,\n" +
                "\t\t\t\"Ratio\": 0.0055,\n" +
                "\t\t\t\"CountRatio\": 0.0059,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"Raa\",\n" +
                "\t\t\t\t\"Name\": \"学生公寓用地\",\n" +
                "\t\t\t\t\"Count\": 6,\n" +
                "\t\t\t\t\"Area\": 227466.32,\n" +
                "\t\t\t\t\"Ratio\": 0.0006,\n" +
                "\t\t\t\t\"CountRatio\": 0.0003,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"Rab\",\n" +
                "\t\t\t\t\"Name\": \"老年公寓用地\",\n" +
                "\t\t\t\t\"Count\": 8,\n" +
                "\t\t\t\t\"Area\": 705241.06,\n" +
                "\t\t\t\t\"Ratio\": 0.0017,\n" +
                "\t\t\t\t\"CountRatio\": 0.0005,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"Rac\",\n" +
                "\t\t\t\t\"Name\": \"职工公寓用地\",\n" +
                "\t\t\t\t\"Count\": 5,\n" +
                "\t\t\t\t\"Area\": 545566.76,\n" +
                "\t\t\t\t\"Ratio\": 0.0013,\n" +
                "\t\t\t\t\"CountRatio\": 0.0003,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"Rax\",\n" +
                "\t\t\t\t\"Name\": \"幼托用地\",\n" +
                "\t\t\t\t\"Count\": 77,\n" +
                "\t\t\t\t\"Area\": 563401.25,\n" +
                "\t\t\t\t\"Ratio\": 0.0014,\n" +
                "\t\t\t\t\"CountRatio\": 0.0045,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"Code\": \"A\",\n" +
                "\t\t\"Name\": \"公共管理与公共服务设施用地\",\n" +
                "\t\t\"Count\": 817,\n" +
                "\t\t\"Area\": 17456149.11,\n" +
                "\t\t\"Ratio\": 0.0431,\n" +
                "\t\t\"CountRatio\": 0.0474,\n" +
                "\t\t\"ChildItems\": [{\n" +
                "\t\t\t\"Code\": \"A1\",\n" +
                "\t\t\t\"Name\": \"行政办公用地\",\n" +
                "\t\t\t\"Count\": 176,\n" +
                "\t\t\t\"Area\": 2344395.53,\n" +
                "\t\t\t\"Ratio\": 0.0058,\n" +
                "\t\t\t\"CountRatio\": 0.0102,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"A2\",\n" +
                "\t\t\t\"Name\": \"文化设施用地\",\n" +
                "\t\t\t\"Count\": 58,\n" +
                "\t\t\t\"Area\": 820442.2,\n" +
                "\t\t\t\"Ratio\": 0.002,\n" +
                "\t\t\t\"CountRatio\": 0.0034,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"A21\",\n" +
                "\t\t\t\t\"Name\": \"图书展览用地\",\n" +
                "\t\t\t\t\"Count\": 16,\n" +
                "\t\t\t\t\"Area\": 395287.35,\n" +
                "\t\t\t\t\"Ratio\": 0.001,\n" +
                "\t\t\t\t\"CountRatio\": 0.0009,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"A22\",\n" +
                "\t\t\t\t\"Name\": \"文化活动用地\",\n" +
                "\t\t\t\t\"Count\": 21,\n" +
                "\t\t\t\t\"Area\": 220549.77,\n" +
                "\t\t\t\t\"Ratio\": 0.0005,\n" +
                "\t\t\t\t\"CountRatio\": 0.0012,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"A3\",\n" +
                "\t\t\t\"Name\": \"教育科研用地\",\n" +
                "\t\t\t\"Count\": 215,\n" +
                "\t\t\t\"Area\": 9796540.92,\n" +
                "\t\t\t\"Ratio\": 0.0242,\n" +
                "\t\t\t\"CountRatio\": 0.0125,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"A31\",\n" +
                "\t\t\t\t\"Name\": \"高等院校用地\",\n" +
                "\t\t\t\t\"Count\": 38,\n" +
                "\t\t\t\t\"Area\": 2641378.55,\n" +
                "\t\t\t\t\"Ratio\": 0.0065,\n" +
                "\t\t\t\t\"CountRatio\": 0.0022,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"A32\",\n" +
                "\t\t\t\t\"Name\": \"中等专业学校用地\",\n" +
                "\t\t\t\t\"Count\": 12,\n" +
                "\t\t\t\t\"Area\": 617327.13,\n" +
                "\t\t\t\t\"Ratio\": 0.0015,\n" +
                "\t\t\t\t\"CountRatio\": 0.0007,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"A33\",\n" +
                "\t\t\t\t\"Name\": \"中小学用地\",\n" +
                "\t\t\t\t\"Count\": 140,\n" +
                "\t\t\t\t\"Area\": 5192667.21,\n" +
                "\t\t\t\t\"Ratio\": 0.0128,\n" +
                "\t\t\t\t\"CountRatio\": 0.0081,\n" +
                "\t\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\t\"Code\": \"A33a\",\n" +
                "\t\t\t\t\t\"Name\": \"小学用地\",\n" +
                "\t\t\t\t\t\"Count\": 39,\n" +
                "\t\t\t\t\t\"Area\": 976809.44,\n" +
                "\t\t\t\t\t\"Ratio\": 0.0024,\n" +
                "\t\t\t\t\t\"CountRatio\": 0.0023,\n" +
                "\t\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"Code\": \"A33b\",\n" +
                "\t\t\t\t\t\"Name\": \"初中用地\",\n" +
                "\t\t\t\t\t\"Count\": 16,\n" +
                "\t\t\t\t\t\"Area\": 489489.07,\n" +
                "\t\t\t\t\t\"Ratio\": 0.0012,\n" +
                "\t\t\t\t\t\"CountRatio\": 0.0009,\n" +
                "\t\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"Code\": \"A33c\",\n" +
                "\t\t\t\t\t\"Name\": \"高中用地\",\n" +
                "\t\t\t\t\t\"Count\": 16,\n" +
                "\t\t\t\t\t\"Area\": 827023.4,\n" +
                "\t\t\t\t\t\"Ratio\": 0.002,\n" +
                "\t\t\t\t\t\"CountRatio\": 0.0009,\n" +
                "\t\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t\t}]\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"A34\",\n" +
                "\t\t\t\t\"Name\": \"特殊教育用地\",\n" +
                "\t\t\t\t\"Count\": 1,\n" +
                "\t\t\t\t\"Area\": 9832.47,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0001,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"A35\",\n" +
                "\t\t\t\t\"Name\": \"科研用地\",\n" +
                "\t\t\t\t\"Count\": 22,\n" +
                "\t\t\t\t\"Area\": 1293928.43,\n" +
                "\t\t\t\t\"Ratio\": 0.0032,\n" +
                "\t\t\t\t\"CountRatio\": 0.0013,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"A4\",\n" +
                "\t\t\t\"Name\": \"体育用地\",\n" +
                "\t\t\t\"Count\": 35,\n" +
                "\t\t\t\"Area\": 1041598.81,\n" +
                "\t\t\t\"Ratio\": 0.0026,\n" +
                "\t\t\t\"CountRatio\": 0.002,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"A41\",\n" +
                "\t\t\t\t\"Name\": \"体育场馆用地\",\n" +
                "\t\t\t\t\"Count\": 30,\n" +
                "\t\t\t\t\"Area\": 980015.46,\n" +
                "\t\t\t\t\"Ratio\": 0.0024,\n" +
                "\t\t\t\t\"CountRatio\": 0.0017,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"A42\",\n" +
                "\t\t\t\t\"Name\": \"体育训练用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"A5\",\n" +
                "\t\t\t\"Name\": \"医疗卫生用地\",\n" +
                "\t\t\t\"Count\": 78,\n" +
                "\t\t\t\"Area\": 1811654.85,\n" +
                "\t\t\t\"Ratio\": 0.0045,\n" +
                "\t\t\t\"CountRatio\": 0.0045,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"A51\",\n" +
                "\t\t\t\t\"Name\": \"医院用地\",\n" +
                "\t\t\t\t\"Count\": 49,\n" +
                "\t\t\t\t\"Area\": 1327492.25,\n" +
                "\t\t\t\t\"Ratio\": 0.0033,\n" +
                "\t\t\t\t\"CountRatio\": 0.0028,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"A52\",\n" +
                "\t\t\t\t\"Name\": \"卫生防疫用地\",\n" +
                "\t\t\t\t\"Count\": 5,\n" +
                "\t\t\t\t\"Area\": 23038.6,\n" +
                "\t\t\t\t\"Ratio\": 0.0001,\n" +
                "\t\t\t\t\"CountRatio\": 0.0003,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"A53\",\n" +
                "\t\t\t\t\"Name\": \"特殊医疗用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"A54\",\n" +
                "\t\t\t\t\"Name\": \"修疗养用地\",\n" +
                "\t\t\t\t\"Count\": 21,\n" +
                "\t\t\t\t\"Area\": 377419.65,\n" +
                "\t\t\t\t\"Ratio\": 0.0009,\n" +
                "\t\t\t\t\"CountRatio\": 0.0012,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"A59\",\n" +
                "\t\t\t\t\"Name\": \"其他医疗卫生用地\",\n" +
                "\t\t\t\t\"Count\": 1,\n" +
                "\t\t\t\t\"Area\": 3652.57,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0001,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"A6\",\n" +
                "\t\t\t\"Name\": \"社会福利用地\",\n" +
                "\t\t\t\"Count\": 23,\n" +
                "\t\t\t\"Area\": 391209.47,\n" +
                "\t\t\t\"Ratio\": 0.001,\n" +
                "\t\t\t\"CountRatio\": 0.0013,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"A7\",\n" +
                "\t\t\t\"Name\": \"文物古迹用地\",\n" +
                "\t\t\t\"Count\": 138,\n" +
                "\t\t\t\"Area\": 119957.56,\n" +
                "\t\t\t\"Ratio\": 0.0003,\n" +
                "\t\t\t\"CountRatio\": 0.008,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"A8\",\n" +
                "\t\t\t\"Name\": \"外事用地\",\n" +
                "\t\t\t\"Count\": 0,\n" +
                "\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"A9\",\n" +
                "\t\t\t\"Name\": \"宗教用地\",\n" +
                "\t\t\t\"Count\": 10,\n" +
                "\t\t\t\"Area\": 110388.19,\n" +
                "\t\t\t\"Ratio\": 0.0003,\n" +
                "\t\t\t\"CountRatio\": 0.0006,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"Aa\",\n" +
                "\t\t\t\"Name\": \"社区综合服务设施用地\",\n" +
                "\t\t\t\"Count\": 84,\n" +
                "\t\t\t\"Area\": 1019961.58,\n" +
                "\t\t\t\"Ratio\": 0.0025,\n" +
                "\t\t\t\"CountRatio\": 0.0049,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"Code\": \"B\",\n" +
                "\t\t\"Name\": \"商业服务业设施用地\",\n" +
                "\t\t\"Count\": 1238,\n" +
                "\t\t\"Area\": 22104756.67,\n" +
                "\t\t\"Ratio\": 0.0546,\n" +
                "\t\t\"CountRatio\": 0.0719,\n" +
                "\t\t\"ChildItems\": [{\n" +
                "\t\t\t\"Code\": \"B1\",\n" +
                "\t\t\t\"Name\": \"商业用地\",\n" +
                "\t\t\t\"Count\": 863,\n" +
                "\t\t\t\"Area\": 15713939.01,\n" +
                "\t\t\t\"Ratio\": 0.0388,\n" +
                "\t\t\t\"CountRatio\": 0.0501,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"B11\",\n" +
                "\t\t\t\t\"Name\": \"零售商业用地\",\n" +
                "\t\t\t\t\"Count\": 518,\n" +
                "\t\t\t\t\"Area\": 8758770.78,\n" +
                "\t\t\t\t\"Ratio\": 0.0216,\n" +
                "\t\t\t\t\"CountRatio\": 0.0301,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"B12\",\n" +
                "\t\t\t\t\"Name\": \"批发市场用地\",\n" +
                "\t\t\t\t\"Count\": 74,\n" +
                "\t\t\t\t\"Area\": 2678336.17,\n" +
                "\t\t\t\t\"Ratio\": 0.0066,\n" +
                "\t\t\t\t\"CountRatio\": 0.0043,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"B13\",\n" +
                "\t\t\t\t\"Name\": \"餐饮用地\",\n" +
                "\t\t\t\t\"Count\": 35,\n" +
                "\t\t\t\t\"Area\": 287692.74,\n" +
                "\t\t\t\t\"Ratio\": 0.0007,\n" +
                "\t\t\t\t\"CountRatio\": 0.002,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"B14\",\n" +
                "\t\t\t\t\"Name\": \"旅馆用地\",\n" +
                "\t\t\t\t\"Count\": 57,\n" +
                "\t\t\t\t\"Area\": 818507.69,\n" +
                "\t\t\t\t\"Ratio\": 0.002,\n" +
                "\t\t\t\t\"CountRatio\": 0.0033,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"B2\",\n" +
                "\t\t\t\"Name\": \"商务用地\",\n" +
                "\t\t\t\"Count\": 226,\n" +
                "\t\t\t\"Area\": 4782677.28,\n" +
                "\t\t\t\"Ratio\": 0.0118,\n" +
                "\t\t\t\"CountRatio\": 0.0131,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"B21\",\n" +
                "\t\t\t\t\"Name\": \"金融保险用地\",\n" +
                "\t\t\t\t\"Count\": 34,\n" +
                "\t\t\t\t\"Area\": 527626.6,\n" +
                "\t\t\t\t\"Ratio\": 0.0013,\n" +
                "\t\t\t\t\"CountRatio\": 0.002,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"B22\",\n" +
                "\t\t\t\t\"Name\": \"艺术传媒用地\",\n" +
                "\t\t\t\t\"Count\": 10,\n" +
                "\t\t\t\t\"Area\": 118958.53,\n" +
                "\t\t\t\t\"Ratio\": 0.0003,\n" +
                "\t\t\t\t\"CountRatio\": 0.0006,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"B29\",\n" +
                "\t\t\t\t\"Name\": \"其他商务用地\",\n" +
                "\t\t\t\t\"Count\": 73,\n" +
                "\t\t\t\t\"Area\": 1971103.95,\n" +
                "\t\t\t\t\"Ratio\": 0.0049,\n" +
                "\t\t\t\t\"CountRatio\": 0.0042,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"B3\",\n" +
                "\t\t\t\"Name\": \"娱乐康体用地\",\n" +
                "\t\t\t\"Count\": 43,\n" +
                "\t\t\t\"Area\": 952111.23,\n" +
                "\t\t\t\"Ratio\": 0.0024,\n" +
                "\t\t\t\"CountRatio\": 0.0025,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"B31\",\n" +
                "\t\t\t\t\"Name\": \"娱乐用地\",\n" +
                "\t\t\t\t\"Count\": 41,\n" +
                "\t\t\t\t\"Area\": 927289.03,\n" +
                "\t\t\t\t\"Ratio\": 0.0023,\n" +
                "\t\t\t\t\"CountRatio\": 0.0024,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"B32\",\n" +
                "\t\t\t\t\"Name\": \"康体用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"B4\",\n" +
                "\t\t\t\"Name\": \"公用设施营业网点用地\",\n" +
                "\t\t\t\"Count\": 94,\n" +
                "\t\t\t\"Area\": 412795.16,\n" +
                "\t\t\t\"Ratio\": 0.001,\n" +
                "\t\t\t\"CountRatio\": 0.0055,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"B41\",\n" +
                "\t\t\t\t\"Name\": \"加油加气站用地\",\n" +
                "\t\t\t\t\"Count\": 72,\n" +
                "\t\t\t\t\"Area\": 260226.71,\n" +
                "\t\t\t\t\"Ratio\": 0.0006,\n" +
                "\t\t\t\t\"CountRatio\": 0.0042,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"B49\",\n" +
                "\t\t\t\t\"Name\": \"其他公用设施营业网点用地\",\n" +
                "\t\t\t\t\"Count\": 19,\n" +
                "\t\t\t\t\"Area\": 140218.99,\n" +
                "\t\t\t\t\"Ratio\": 0.0003,\n" +
                "\t\t\t\t\"CountRatio\": 0.0011,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"B4a\",\n" +
                "\t\t\t\t\"Name\": \"充电站用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"B9\",\n" +
                "\t\t\t\"Name\": \"其他服务设施用地\",\n" +
                "\t\t\t\"Count\": 7,\n" +
                "\t\t\t\"Area\": 150847.47,\n" +
                "\t\t\t\"Ratio\": 0.0004,\n" +
                "\t\t\t\"CountRatio\": 0.0004,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"Code\": \"M\",\n" +
                "\t\t\"Name\": \"工业用地\",\n" +
                "\t\t\"Count\": 851,\n" +
                "\t\t\"Area\": 76372693.17,\n" +
                "\t\t\"Ratio\": 0.1886,\n" +
                "\t\t\"CountRatio\": 0.0494,\n" +
                "\t\t\"ChildItems\": [{\n" +
                "\t\t\t\"Code\": \"M1\",\n" +
                "\t\t\t\"Name\": \"一类工业用地\",\n" +
                "\t\t\t\"Count\": 268,\n" +
                "\t\t\t\"Area\": 20131348.47,\n" +
                "\t\t\t\"Ratio\": 0.0497,\n" +
                "\t\t\t\"CountRatio\": 0.0156,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"M2\",\n" +
                "\t\t\t\"Name\": \"二类工业用地\",\n" +
                "\t\t\t\"Count\": 481,\n" +
                "\t\t\t\"Area\": 46067434.3,\n" +
                "\t\t\t\"Ratio\": 0.1137,\n" +
                "\t\t\t\"CountRatio\": 0.0279,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"M3\",\n" +
                "\t\t\t\"Name\": \"三类工业用地\",\n" +
                "\t\t\t\"Count\": 42,\n" +
                "\t\t\t\"Area\": 5392018.92,\n" +
                "\t\t\t\"Ratio\": 0.0133,\n" +
                "\t\t\t\"CountRatio\": 0.0024,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"Ma\",\n" +
                "\t\t\t\"Name\": \"生产研发用地\",\n" +
                "\t\t\t\"Count\": 60,\n" +
                "\t\t\t\"Area\": 4781891.48,\n" +
                "\t\t\t\"Ratio\": 0.0118,\n" +
                "\t\t\t\"CountRatio\": 0.0035,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"Code\": \"W\",\n" +
                "\t\t\"Name\": \"物流仓储用地\",\n" +
                "\t\t\"Count\": 141,\n" +
                "\t\t\"Area\": 10476687.64,\n" +
                "\t\t\"Ratio\": 0.0259,\n" +
                "\t\t\"CountRatio\": 0.0082,\n" +
                "\t\t\"ChildItems\": [{\n" +
                "\t\t\t\"Code\": \"W1\",\n" +
                "\t\t\t\"Name\": \"一类物流仓储用地\",\n" +
                "\t\t\t\"Count\": 97,\n" +
                "\t\t\t\"Area\": 7271938.8,\n" +
                "\t\t\t\"Ratio\": 0.018,\n" +
                "\t\t\t\"CountRatio\": 0.0056,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"W2\",\n" +
                "\t\t\t\"Name\": \"二类物流仓储用地\",\n" +
                "\t\t\t\"Count\": 38,\n" +
                "\t\t\t\"Area\": 2705521.16,\n" +
                "\t\t\t\"Ratio\": 0.0067,\n" +
                "\t\t\t\"CountRatio\": 0.0022,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"W3\",\n" +
                "\t\t\t\"Name\": \"三类物流仓储用地\",\n" +
                "\t\t\t\"Count\": 2,\n" +
                "\t\t\t\"Area\": 236280.42,\n" +
                "\t\t\t\"Ratio\": 0.0006,\n" +
                "\t\t\t\"CountRatio\": 0.0001,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"Code\": \"S\",\n" +
                "\t\t\"Name\": \"道路与交通设施用地\",\n" +
                "\t\t\"Count\": 2539,\n" +
                "\t\t\"Area\": 59140476.53,\n" +
                "\t\t\"Ratio\": 0.146,\n" +
                "\t\t\"CountRatio\": 0.1474,\n" +
                "\t\t\"ChildItems\": [{\n" +
                "\t\t\t\"Code\": \"S1\",\n" +
                "\t\t\t\"Name\": \"城市道路用地\",\n" +
                "\t\t\t\"Count\": 2239,\n" +
                "\t\t\t\"Area\": 56513221.44,\n" +
                "\t\t\t\"Ratio\": 0.1395,\n" +
                "\t\t\t\"CountRatio\": 0.13,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"S1a\",\n" +
                "\t\t\t\t\"Name\": \"快速路用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"S1b\",\n" +
                "\t\t\t\t\"Name\": \"主干路用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"S1c\",\n" +
                "\t\t\t\t\"Name\": \"次干路用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"S1d\",\n" +
                "\t\t\t\t\"Name\": \"支路用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"S2\",\n" +
                "\t\t\t\"Name\": \"城市轨道交通用地\",\n" +
                "\t\t\t\"Count\": 0,\n" +
                "\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"S3\",\n" +
                "\t\t\t\"Name\": \"交通枢纽用地\",\n" +
                "\t\t\t\"Count\": 22,\n" +
                "\t\t\t\"Area\": 973417.9,\n" +
                "\t\t\t\"Ratio\": 0.0024,\n" +
                "\t\t\t\"CountRatio\": 0.0013,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"S4\",\n" +
                "\t\t\t\"Name\": \"交通场站用地\",\n" +
                "\t\t\t\"Count\": 277,\n" +
                "\t\t\t\"Area\": 1647133.13,\n" +
                "\t\t\t\"Ratio\": 0.0041,\n" +
                "\t\t\t\"CountRatio\": 0.0161,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"S41\",\n" +
                "\t\t\t\t\"Name\": \"公共交通场站用地\",\n" +
                "\t\t\t\t\"Count\": 73,\n" +
                "\t\t\t\t\"Area\": 564251.17,\n" +
                "\t\t\t\t\"Ratio\": 0.0014,\n" +
                "\t\t\t\t\"CountRatio\": 0.0042,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"S42\",\n" +
                "\t\t\t\t\"Name\": \"社会停车场用地\",\n" +
                "\t\t\t\t\"Count\": 204,\n" +
                "\t\t\t\t\"Area\": 1082881.96,\n" +
                "\t\t\t\t\"Ratio\": 0.0027,\n" +
                "\t\t\t\t\"CountRatio\": 0.0118,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"S9\",\n" +
                "\t\t\t\"Name\": \"其他交通设施用地\",\n" +
                "\t\t\t\"Count\": 1,\n" +
                "\t\t\t\"Area\": 6704.06,\n" +
                "\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\"CountRatio\": 0.0001,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"Code\": \"U\",\n" +
                "\t\t\"Name\": \"公用设施用地\",\n" +
                "\t\t\"Count\": 268,\n" +
                "\t\t\"Area\": 2266056.07,\n" +
                "\t\t\"Ratio\": 0.0056,\n" +
                "\t\t\"CountRatio\": 0.0156,\n" +
                "\t\t\"ChildItems\": [{\n" +
                "\t\t\t\"Code\": \"U1\",\n" +
                "\t\t\t\"Name\": \"供应设施用地\",\n" +
                "\t\t\t\"Count\": 110,\n" +
                "\t\t\t\"Area\": 1098235.46,\n" +
                "\t\t\t\"Ratio\": 0.0027,\n" +
                "\t\t\t\"CountRatio\": 0.0064,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"U11\",\n" +
                "\t\t\t\t\"Name\": \"供水用地\",\n" +
                "\t\t\t\t\"Count\": 14,\n" +
                "\t\t\t\t\"Area\": 311515.88,\n" +
                "\t\t\t\t\"Ratio\": 0.0008,\n" +
                "\t\t\t\t\"CountRatio\": 0.0008,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"U12\",\n" +
                "\t\t\t\t\"Name\": \"供电用地\",\n" +
                "\t\t\t\t\"Count\": 48,\n" +
                "\t\t\t\t\"Area\": 356577.16,\n" +
                "\t\t\t\t\"Ratio\": 0.0009,\n" +
                "\t\t\t\t\"CountRatio\": 0.0028,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"U13\",\n" +
                "\t\t\t\t\"Name\": \"供燃气用地\",\n" +
                "\t\t\t\t\"Count\": 14,\n" +
                "\t\t\t\t\"Area\": 135164.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0003,\n" +
                "\t\t\t\t\"CountRatio\": 0.0008,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"U14\",\n" +
                "\t\t\t\t\"Name\": \"供热用地\",\n" +
                "\t\t\t\t\"Count\": 1,\n" +
                "\t\t\t\t\"Area\": 91323.84,\n" +
                "\t\t\t\t\"Ratio\": 0.0002,\n" +
                "\t\t\t\t\"CountRatio\": 0.0001,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"U15\",\n" +
                "\t\t\t\t\"Name\": \"通信用地\",\n" +
                "\t\t\t\t\"Count\": 20,\n" +
                "\t\t\t\t\"Area\": 92874.78,\n" +
                "\t\t\t\t\"Ratio\": 0.0002,\n" +
                "\t\t\t\t\"CountRatio\": 0.0012,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"U16\",\n" +
                "\t\t\t\t\"Name\": \"广播电视用地\",\n" +
                "\t\t\t\t\"Count\": 12,\n" +
                "\t\t\t\t\"Area\": 104314.08,\n" +
                "\t\t\t\t\"Ratio\": 0.0003,\n" +
                "\t\t\t\t\"CountRatio\": 0.0007,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"U2\",\n" +
                "\t\t\t\"Name\": \"环境设施用地\",\n" +
                "\t\t\t\"Count\": 98,\n" +
                "\t\t\t\"Area\": 777659.28,\n" +
                "\t\t\t\"Ratio\": 0.0019,\n" +
                "\t\t\t\"CountRatio\": 0.0057,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"U21\",\n" +
                "\t\t\t\t\"Name\": \"排水用地\",\n" +
                "\t\t\t\t\"Count\": 33,\n" +
                "\t\t\t\t\"Area\": 560104.51,\n" +
                "\t\t\t\t\"Ratio\": 0.0014,\n" +
                "\t\t\t\t\"CountRatio\": 0.0019,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"U22\",\n" +
                "\t\t\t\t\"Name\": \"环卫用地\",\n" +
                "\t\t\t\t\"Count\": 55,\n" +
                "\t\t\t\t\"Area\": 194941.75,\n" +
                "\t\t\t\t\"Ratio\": 0.0005,\n" +
                "\t\t\t\t\"CountRatio\": 0.0032,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"U3\",\n" +
                "\t\t\t\"Name\": \"安全设施用地\",\n" +
                "\t\t\t\"Count\": 53,\n" +
                "\t\t\t\"Area\": 317143.32,\n" +
                "\t\t\t\"Ratio\": 0.0008,\n" +
                "\t\t\t\"CountRatio\": 0.0031,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"U31\",\n" +
                "\t\t\t\t\"Name\": \"消防用地\",\n" +
                "\t\t\t\t\"Count\": 36,\n" +
                "\t\t\t\t\"Area\": 247569.03,\n" +
                "\t\t\t\t\"Ratio\": 0.0006,\n" +
                "\t\t\t\t\"CountRatio\": 0.0021,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"U32\",\n" +
                "\t\t\t\t\"Name\": \"防洪用地\",\n" +
                "\t\t\t\t\"Count\": 17,\n" +
                "\t\t\t\t\"Area\": 69574.29,\n" +
                "\t\t\t\t\"Ratio\": 0.0002,\n" +
                "\t\t\t\t\"CountRatio\": 0.001,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"U3a\",\n" +
                "\t\t\t\t\"Name\": \"人防用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"U9\",\n" +
                "\t\t\t\"Name\": \"其他公用设施用地\",\n" +
                "\t\t\t\"Count\": 6,\n" +
                "\t\t\t\"Area\": 44124.13,\n" +
                "\t\t\t\"Ratio\": 0.0001,\n" +
                "\t\t\t\"CountRatio\": 0.0003,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"Code\": \"G\",\n" +
                "\t\t\"Name\": \"绿地与广场用地\",\n" +
                "\t\t\"Count\": 6771,\n" +
                "\t\t\"Area\": 68907153.04,\n" +
                "\t\t\"Ratio\": 0.1701,\n" +
                "\t\t\"CountRatio\": 0.3931,\n" +
                "\t\t\"ChildItems\": [{\n" +
                "\t\t\t\"Code\": \"G1\",\n" +
                "\t\t\t\"Name\": \"公园绿地\",\n" +
                "\t\t\t\"Count\": 2865,\n" +
                "\t\t\t\"Area\": 25937023.68,\n" +
                "\t\t\t\"Ratio\": 0.064,\n" +
                "\t\t\t\"CountRatio\": 0.1663,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"G1a\",\n" +
                "\t\t\t\t\"Name\": \"综合公园\",\n" +
                "\t\t\t\t\"Count\": 122,\n" +
                "\t\t\t\t\"Area\": 2430492.34,\n" +
                "\t\t\t\t\"Ratio\": 0.006,\n" +
                "\t\t\t\t\"CountRatio\": 0.0071,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"G1b\",\n" +
                "\t\t\t\t\"Name\": \"专类公园\",\n" +
                "\t\t\t\t\"Count\": 15,\n" +
                "\t\t\t\t\"Area\": 286130.03,\n" +
                "\t\t\t\t\"Ratio\": 0.0007,\n" +
                "\t\t\t\t\"CountRatio\": 0.0009,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"G1c\",\n" +
                "\t\t\t\t\"Name\": \"街旁绿地\",\n" +
                "\t\t\t\t\"Count\": 613,\n" +
                "\t\t\t\t\"Area\": 4186069.65,\n" +
                "\t\t\t\t\"Ratio\": 0.0103,\n" +
                "\t\t\t\t\"CountRatio\": 0.0356,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"G2\",\n" +
                "\t\t\t\"Name\": \"防护绿地\",\n" +
                "\t\t\t\"Count\": 3764,\n" +
                "\t\t\t\"Area\": 42068213.49,\n" +
                "\t\t\t\"Ratio\": 0.1039,\n" +
                "\t\t\t\"CountRatio\": 0.2185,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"G3\",\n" +
                "\t\t\t\"Name\": \"广场用地\",\n" +
                "\t\t\t\"Count\": 121,\n" +
                "\t\t\t\"Area\": 834122.22,\n" +
                "\t\t\t\"Ratio\": 0.0021,\n" +
                "\t\t\t\"CountRatio\": 0.007,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"Code\": \"H\",\n" +
                "\t\t\"Name\": \"建设用地\",\n" +
                "\t\t\"Count\": 117,\n" +
                "\t\t\"Area\": 4723280.8,\n" +
                "\t\t\"Ratio\": 0.0117,\n" +
                "\t\t\"CountRatio\": 0.0068,\n" +
                "\t\t\"ChildItems\": [{\n" +
                "\t\t\t\"Code\": \"H1\",\n" +
                "\t\t\t\"Name\": \"城乡居民点建设用地\",\n" +
                "\t\t\t\"Count\": 65,\n" +
                "\t\t\t\"Area\": 2590184.01,\n" +
                "\t\t\t\"Ratio\": 0.0064,\n" +
                "\t\t\t\"CountRatio\": 0.0038,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"H11\",\n" +
                "\t\t\t\t\"Name\": \"城市建设用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"H12\",\n" +
                "\t\t\t\t\"Name\": \"镇建设用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"H13\",\n" +
                "\t\t\t\t\"Name\": \"乡建设用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"H14\",\n" +
                "\t\t\t\t\"Name\": \"村庄建设用地\",\n" +
                "\t\t\t\t\"Count\": 65,\n" +
                "\t\t\t\t\"Area\": 2590184.01,\n" +
                "\t\t\t\t\"Ratio\": 0.0064,\n" +
                "\t\t\t\t\"CountRatio\": 0.0038,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"H2\",\n" +
                "\t\t\t\"Name\": \"区域交通设施用地\",\n" +
                "\t\t\t\"Count\": 30,\n" +
                "\t\t\t\"Area\": 1863982.18,\n" +
                "\t\t\t\"Ratio\": 0.0046,\n" +
                "\t\t\t\"CountRatio\": 0.0017,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"H21\",\n" +
                "\t\t\t\t\"Name\": \"铁路用地\",\n" +
                "\t\t\t\t\"Count\": 5,\n" +
                "\t\t\t\t\"Area\": 63822.91,\n" +
                "\t\t\t\t\"Ratio\": 0.0002,\n" +
                "\t\t\t\t\"CountRatio\": 0.0003,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"H22\",\n" +
                "\t\t\t\t\"Name\": \"公路用地\",\n" +
                "\t\t\t\t\"Count\": 2,\n" +
                "\t\t\t\t\"Area\": 26472.83,\n" +
                "\t\t\t\t\"Ratio\": 0.0001,\n" +
                "\t\t\t\t\"CountRatio\": 0.0001,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"H23\",\n" +
                "\t\t\t\t\"Name\": \"港口用地\",\n" +
                "\t\t\t\t\"Count\": 22,\n" +
                "\t\t\t\t\"Area\": 1755039.87,\n" +
                "\t\t\t\t\"Ratio\": 0.0043,\n" +
                "\t\t\t\t\"CountRatio\": 0.0013,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"H24\",\n" +
                "\t\t\t\t\"Name\": \"机场用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"H25\",\n" +
                "\t\t\t\t\"Name\": \"管道运输用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"H3\",\n" +
                "\t\t\t\"Name\": \"区域公共设施用地\",\n" +
                "\t\t\t\"Count\": 14,\n" +
                "\t\t\t\"Area\": 148821.06,\n" +
                "\t\t\t\"Ratio\": 0.0004,\n" +
                "\t\t\t\"CountRatio\": 0.0008,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"H4\",\n" +
                "\t\t\t\"Name\": \"特殊用地\",\n" +
                "\t\t\t\"Count\": 8,\n" +
                "\t\t\t\"Area\": 120293.55,\n" +
                "\t\t\t\"Ratio\": 0.0003,\n" +
                "\t\t\t\"CountRatio\": 0.0005,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"H41\",\n" +
                "\t\t\t\t\"Name\": \"军事用地\",\n" +
                "\t\t\t\t\"Count\": 7,\n" +
                "\t\t\t\t\"Area\": 67856.16,\n" +
                "\t\t\t\t\"Ratio\": 0.0002,\n" +
                "\t\t\t\t\"CountRatio\": 0.0004,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"H42\",\n" +
                "\t\t\t\t\"Name\": \"安保用地\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"H5\",\n" +
                "\t\t\t\"Name\": \"采矿用地\",\n" +
                "\t\t\t\"Count\": 0,\n" +
                "\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"H9\",\n" +
                "\t\t\t\"Name\": \"其他建设用地\",\n" +
                "\t\t\t\"Count\": 0,\n" +
                "\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"Code\": \"E\",\n" +
                "\t\t\"Name\": \"非建设用地\",\n" +
                "\t\t\"Count\": 2062,\n" +
                "\t\t\"Area\": 33563837.97,\n" +
                "\t\t\"Ratio\": 0.0829,\n" +
                "\t\t\"CountRatio\": 0.1197,\n" +
                "\t\t\"ChildItems\": [{\n" +
                "\t\t\t\"Code\": \"E1\",\n" +
                "\t\t\t\"Name\": \"水域\",\n" +
                "\t\t\t\"Count\": 1942,\n" +
                "\t\t\t\"Area\": 27556905.67,\n" +
                "\t\t\t\"Ratio\": 0.068,\n" +
                "\t\t\t\"CountRatio\": 0.1127,\n" +
                "\t\t\t\"ChildItems\": [{\n" +
                "\t\t\t\t\"Code\": \"E11\",\n" +
                "\t\t\t\t\"Name\": \"自然水域\",\n" +
                "\t\t\t\t\"Count\": 326,\n" +
                "\t\t\t\t\"Area\": 5673846.52,\n" +
                "\t\t\t\t\"Ratio\": 0.014,\n" +
                "\t\t\t\t\"CountRatio\": 0.0189,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"E12\",\n" +
                "\t\t\t\t\"Name\": \"水库\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}, {\n" +
                "\t\t\t\t\"Code\": \"E13\",\n" +
                "\t\t\t\t\"Name\": \"坑塘沟渠\",\n" +
                "\t\t\t\t\"Count\": 0,\n" +
                "\t\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\t\"ChildItems\": null\n" +
                "\t\t\t}]\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"E2\",\n" +
                "\t\t\t\"Name\": \"农林用地\",\n" +
                "\t\t\t\"Count\": 117,\n" +
                "\t\t\t\"Area\": 5831658.26,\n" +
                "\t\t\t\"Ratio\": 0.0144,\n" +
                "\t\t\t\"CountRatio\": 0.0068,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"E9\",\n" +
                "\t\t\t\"Name\": \"其他非建设用地\",\n" +
                "\t\t\t\"Count\": 0,\n" +
                "\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"Code\": \"Y\",\n" +
                "\t\t\"Name\": \"其他用地\",\n" +
                "\t\t\"Count\": 82,\n" +
                "\t\t\"Area\": 12696252.75,\n" +
                "\t\t\"Ratio\": 0.0313,\n" +
                "\t\t\"CountRatio\": 0.0048,\n" +
                "\t\t\"ChildItems\": [{\n" +
                "\t\t\t\"Code\": \"Yb\",\n" +
                "\t\t\t\"Name\": \"发展备用地\",\n" +
                "\t\t\t\"Count\": 79,\n" +
                "\t\t\t\"Area\": 12436966.74,\n" +
                "\t\t\t\"Ratio\": 0.0307,\n" +
                "\t\t\t\"CountRatio\": 0.0046,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"Yt\",\n" +
                "\t\t\t\"Name\": \"弹性用地\",\n" +
                "\t\t\t\"Count\": 3,\n" +
                "\t\t\t\"Area\": 259286.01,\n" +
                "\t\t\t\"Ratio\": 0.0006,\n" +
                "\t\t\t\"CountRatio\": 0.0002,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}, {\n" +
                "\t\t\t\"Code\": \"Yk\",\n" +
                "\t\t\t\"Name\": \"空地\",\n" +
                "\t\t\t\"Count\": 0,\n" +
                "\t\t\t\"Area\": 0.0,\n" +
                "\t\t\t\"Ratio\": 0.0,\n" +
                "\t\t\t\"CountRatio\": 0.0,\n" +
                "\t\t\t\"ChildItems\": null\n" +
                "\t\t}]\n" +
                "\t}, {\n" +
                "\t\t\"Code\": \"合计\",\n" +
                "\t\t\"Name\": \"合计\",\n" +
                "\t\t\"Count\": 17225,\n" +
                "\t\t\"Area\": 404992605.43999994,\n" +
                "\t\t\"Ratio\": 1.0,\n" +
                "\t\t\"CountRatio\": 1.0,\n" +
                "\t\t\"ChildItems\": null\n" +
                "\t}]\n" +
                "}";
        // 生成 excel
        ExportExcel excel = new ExportExcel();
        // json数据在gis模块resources文件夹下
        //String json = "";
        JSONObject jsonObject = JSONObject.parseObject(json);
        List<String> headList = Arrays.asList("Code", "Name", "Count", "Area", "Ratio", "CountRatio");

        excel.initSheet("testSheet");
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        excel.sheetLevel(headList, jsonArray);

        excel.dataShiftPosition(5);
        String uuid = UUID.randomUUID().toString();
        String path = "c:/DIST/" + uuid + ".xls";
        excel.saveToPath(path);
        return path;
    }


    /**
     * url 不能null
     * @param url
     * @return
     */
    private String urlStr(String url){
        if (url == null){
            return "url 不能null";
        }
        return url;
    }
}
