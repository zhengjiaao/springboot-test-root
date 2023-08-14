package com.zja.controller;

import com.zja.utils.DocConversionPreviewUtil;
import com.zja.utils.MongoFileUtil;
import com.zja.utils.id.IdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-02 10:23
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：文件代理控制器
 */
@Api(tags = {"FileProxyController"})
@RequestMapping(value = "rest/file")
@RestController
public class FileProxyController {

    @Autowired
    private MongoFileUtil mongoFileUtil;

    @Autowired
    private DocConversionPreviewUtil docConversionPreviewUtil;

    //大文件上传测试：用Postman工具测试 文档预览 DocPreviewUtil
    @ApiOperation(value = "上传单个文件", notes = "返回 path")
    @PostMapping(value = "upload/v1")
    public String upload(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
        String mgdbId = IdUtil.mgdbId();
        mongoFileUtil.uploadByStream(mgdbId, file.getOriginalFilename(), file.getInputStream());
        return mgdbId;
    }

    @ApiOperation(value = "获取原始文件URL", notes = "仅浏览器支持的文件类型可以预览")
    @PostMapping(value = "url/v1/{path}")
    public String url(@PathVariable String path) throws IOException {
        return mongoFileUtil.getFileURL(path);
    }

    @ApiOperation(value = "获取转换后的文件URL", notes = "转换为浏览器支持预览的文件类型")
    @PostMapping(value = "url/v2/{path}")
    public String urlV2(@PathVariable String path) throws IOException {
        String fileLocalCachePath = mongoFileUtil.getFileLocalCachePath(path);
        String pdfPreviewURL = docConversionPreviewUtil.getPdfPreviewURL(fileLocalCachePath);
        return pdfPreviewURL;
    }


}
