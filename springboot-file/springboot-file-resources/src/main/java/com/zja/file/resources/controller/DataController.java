package com.zja.file.resources.controller;

import com.alibaba.fastjson.JSONObject;
import com.zja.file.resources.util.ResourcesFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 接口层（一般与页面、功能对应）
 *
 * @author: zhengja
 * @since: 2024/11/25 14:09
 */
@Validated
@RestController
@RequestMapping("/")
@Api(tags = {"数据管理页面"})
public class DataController {

    @PostMapping("/rest/dtsp/CZTLXKJGHSSJCXT/rule")
    @ApiOperation("创建压盖分析任务")
    public Object createAnalysisTask(@RequestParam String gk, @RequestBody JSONObject json) {
        System.out.println("gk = " + gk);
        System.out.println("json = " + json);
        // 读取文件内容
        JSONObject jsonObject = ResourcesFileUtil.readJSONObjectFromFile("mock/createAnalysisTask.json");
        return jsonObject;
    }

    @GetMapping("/rest/dtsp/result/findResultByTaskId")
    @ApiOperation("获取压盖分析任务结果")
    public Object getAnalysisTaskResults(@RequestParam String gk, @RequestParam String id) {
        System.out.println("gk = " + gk);
        System.out.println("id = " + id);
        // 读取文件内容
        return ResourcesFileUtil.readJSONObjectFromFile("mock/getAnalysisTaskResults.json");
    }

}