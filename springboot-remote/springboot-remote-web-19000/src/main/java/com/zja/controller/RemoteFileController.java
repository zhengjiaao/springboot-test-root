package com.zja.controller;

import com.zja.response.ResponseData;
import com.zja.response.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-07-17 16:07
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：文件接口
 */
@Api(value = "提供远程文件测试接口", description = "提供测试接口给remote")
@RequestMapping(value = "rest/file")
@RestController
public class RemoteFileController {

    @ApiOperation(value = "post-上传单文件", httpMethod = "POST")
    @RequestMapping(value = "/post/upload/v1", method = RequestMethod.POST)
    public ResponseData uploadMongo(@ApiParam(value = "上传的文件", required = true) @RequestParam(value = "file", required = false) MultipartFile file) throws InterruptedException {
        System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
//        Thread.sleep(120000);
        return ResponseUtil.success(true);
    }

    @ApiOperation(value = "post-上传单文件-支持重命名", httpMethod = "POST")
    @RequestMapping(value = "/post/upload/v2", method = RequestMethod.POST)
    public ResponseData uploadMongoV2(
            @ApiParam(value = "上传的文件", required = false) @RequestParam(value = "file", required = false) MultipartFile file,
            @ApiParam(value = "文件名字(带后缀)", required = false) @RequestParam(value = "fileNewName", required = false) String fileNewName) {
        System.out.println("上传文件fileNewName：" + fileNewName + "  大小：" + file.getSize());
        return ResponseUtil.success(true);
    }

    @ApiOperation(value = "post-上传多文件", httpMethod = "POST")
    @RequestMapping(value = "/post/upload/v3", method = RequestMethod.POST)
    public ResponseData uploadMongo(@ApiParam(value = "上传的文件", required = true) @RequestParam(value = "files", required = false) MultipartFile[] files) {
        if (files.length<=0){
            return ResponseUtil.fail("请选择要上传的文件！");
        }
        for (int i= 0;i<files.length;i++){
            MultipartFile file = files[i];
            System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());
        }
        return ResponseUtil.success(true);
    }

}
