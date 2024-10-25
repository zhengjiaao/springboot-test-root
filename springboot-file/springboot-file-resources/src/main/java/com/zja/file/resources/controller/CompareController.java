package com.zja.file.resources.controller;

import com.alibaba.fastjson.JSONObject;
import com.zja.file.resources.util.ResourcesFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 演示读取资源文件（包含jar部署时，读取资源文件）
 *
 * @author: zhengja
 * @since: 2024/04/18 16:48
 */
@Validated
@RestController
@RequestMapping("/rest/location/scheme")
@Api(tags = {"比选管理页面"})
public class CompareController {

    @PostMapping("/compare")
    @ApiOperation("比选")
    public Object compare(@RequestBody JSONObject json) {
        System.out.println("json = " + json);
        // 读取文件内容
        return ResourcesFileUtil.readJSONObjectFromFile("mock/CompareResult.json");
    }

}