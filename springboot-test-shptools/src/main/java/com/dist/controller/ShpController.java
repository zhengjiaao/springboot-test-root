package com.zja.controller;

import com.zja.dto.ShpInfo;
import com.zja.service.ShpService;
import com.zja.util.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**shp接口请求处理器
 * @author zhengja@dist.com.cn
 * @data 2019/8/28 12:31
 */
@CrossOrigin  //解决跨域问题
@RequestMapping("rest/shp")
@RestController
@Api(tags = {"ShpController"},description = "shp文件读写测试")
public class ShpController {

    @Autowired
    private ShpService shpService;

    @ApiOperation(value = "写一个shp文件",httpMethod = "POST")
    @RequestMapping(value = "v1/write/Shp",method = RequestMethod.POST)
    public ResponseResult write(@ApiParam(value = "参考 Model") @RequestBody ShpInfo shpInfo) throws  Exception{
       return  shpService.writeShp(shpInfo);
    }

    @ApiOperation(value = "查询一个shp文件",httpMethod = "GET")
    @RequestMapping(value = "v1/query/Shp",method = RequestMethod.GET)
    public ResponseResult query(@ApiParam(value = "文件绝对路径",required = true) @RequestParam(value = "path",required = true) String shpFilePath,
                                 @ApiParam(value = "指定显示多少条shp特征【features】",required = false,defaultValue = "10") @RequestParam(value = "limit",required = false,defaultValue = "10") Integer limit ) throws  Exception{
            return  shpService.getShpDatas(shpFilePath,limit);
    }

    @GetMapping("/show")
    @ApiOperation(value = "将shp文件转换成png图片",notes = "将shp文件转换成png图片，图片或写入文件或通过response输出到界面【比如，客户端浏览器】",httpMethod = "GET")
    @RequestMapping(value = "v1/show/Shp",method = RequestMethod.GET)
    public  void show(@ApiParam(value = "shp文件路径",required = true) @RequestParam(value = "path",required = true) String path,
                      @ApiParam(value = "如果imagePath不等于空，则shp文件转成图片文件存储进行存",required = false) @RequestParam(value = "imagePath",required = false) String imagePath,
                      @ApiParam(value = "渲染颜色",required = false) @RequestParam(value = "color",required = false) String color,
                      HttpServletResponse response) throws  Exception{

        // 设置响应消息的类型
        response.setContentType("image/png");

        // 设置页面不缓存
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        shpService.showShp(path, imagePath,color ,response);
    }

}
