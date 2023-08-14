package com.zja.controller;

import com.zja.common.LocalFileService;
import com.zja.util.exception.IllegalParameterException;
import com.zja.response.Result;
import com.zja.response.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * 本地资源文件
 * 上传文件到本地tomcat服务器，不是传到mongodb
 *  @author yinxp@dist.com.cn
 */
@Slf4j
@RestController
@Api(tags = "LocalResourceController")
public class LocalResourceController extends BaseController{

    @Resource
    private LocalFileService localFileService;


    @ApiOperation(value = "上传文件(管理端/公众端)", httpMethod = "POST",notes = "此接口仅上传至本地服务器下")
    @RequestMapping(value = "/rest/public/resource",method = RequestMethod.POST)
    public Result upload(
                @ApiParam(value = "上传的文件", required = false) @RequestParam(value = "file",required = false)MultipartFile multipartFile,
                @ApiParam(value = "文件名字，带文件后缀", required = false) @RequestParam(value = "fileName",required = false)String fileName) {

        if (multipartFile == null || multipartFile.isEmpty()) {
            log.error("上传文件时，参数file为空");
            throw new IllegalParameterException("上传文件为空，请重新上传");
        }

        try {
            Map<String, String> map = localFileService.save(fileName, multipartFile.getBytes(), request);
            return ResultUtil.success(map);
        } catch (IOException e) {
            log.error("文件保存失败");
            return ResultUtil.error();
        }
    }

    @ApiOperation(value = "删除文件(管理端)", httpMethod = "DELETE",notes = "删除本地资源")
    @RequestMapping(value = "/rest/public/resource",method = RequestMethod.DELETE)
    public Result delete(
                @ApiParam(value = "文件名称", required = true) @RequestParam(value = "fileName",required = true)String fileName,
                @ApiParam(value = "文件后缀", required = true) @RequestParam(value = "fileSuffix",required = true)String fileSuffix) {
        String file = fileName + "." + fileSuffix;
        try {
            localFileService.deleteByName(file,request);
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("文件删除失败");
            return ResultUtil.error();
        }
    }

}
