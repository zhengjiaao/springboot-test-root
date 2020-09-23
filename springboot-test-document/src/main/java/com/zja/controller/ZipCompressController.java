package com.zja.controller;

import com.zja.util.zip.ZipCompressUtil;
import com.zja.util.zip.ZipDeCompressUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/16 13:17
 */
@Api(tags = {"ZipCompressController"},description = "文档解压缩操作")
@RestController
@RequestMapping(value = "/zip")
public class ZipCompressController {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(ZipCompressController.class);

    @ApiOperation(value = "文件压缩",httpMethod = "GET")
    @RequestMapping(value = "v1/compress",method = RequestMethod.GET)
    public void compress(@ApiParam(value = "压缩源路径",required = true) @RequestParam String srcFilePath,
                         @ApiParam(value = "压缩目的路径",required = true) @RequestParam String destFilePath){
        //文件压缩,自动判断是文件还是文件夹
        //destFilPath 不能存在
        //ZipCompressUtil.compress("D:\\doc\\CAD","D:\\doc\\bb\\");
        ZipCompressUtil.compress(srcFilePath,destFilePath);
    }

    @ApiOperation(value = "文件解压",httpMethod = "GET")
    @RequestMapping(value = "v1/unzip",method = RequestMethod.GET)
    public Object unZip(@ApiParam(value = "压缩文件路径",required = true) @RequestParam String srcFilePath,
                         @ApiParam(value = "解压路径",required = true) @RequestParam String destDirPath){
        //文件压缩,返回解压后的文件路径,编码
        //srcFilePath
        String unZip = ZipDeCompressUtil.deCompress(srcFilePath,destDirPath);
        log.info(unZip);
        return unZip;
    }

}
