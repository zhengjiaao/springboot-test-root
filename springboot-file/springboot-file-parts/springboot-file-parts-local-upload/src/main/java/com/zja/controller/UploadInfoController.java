package com.zja.controller;

import com.zja.model.UploadInfo;
import com.zja.util.UploadInfoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**前端 大文件分片上传->后端接收分片->合并分片
 * @author: zhengja
 * @since: 2019/9/10 11:28
 */
@Api(tags = {"FileUploadController"}, description = "大文件分片上传")
@RestController
@RequestMapping(value = "rest/file")
public class UploadInfoController {

    @Value("${source.fileFragmentationLocation}")
    private String upload_path;

    /****************************** 文件上传 ********************************/

    //大文件分片上传Fragmentation
    @ApiOperation(value = "大文件断点上传", notes = "资源上传接口，swagger不能模拟分片功能，需百度的webuploader或者plupload等支持分片功能的上传组件支持", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "batchNo", value = "当前上传的批次编号", required = true, dataType = "String"),
            @ApiImplicitParam(name = "chunkCount", value = "总分片数/默1 不分片", required = false, dataType = "String", defaultValue = "1"),
            @ApiImplicitParam(name = "chunkNo", value = "当前分片序号", required = false, dataType = "String", defaultValue = "1"),
            @ApiImplicitParam(name = "fileName", value = "文件名", required = true, dataType = "String")
    })
    @RequestMapping(value = "v1/upload", method = RequestMethod.POST)
    public UploadInfo uploadFile(
            String batchNo,
            @RequestParam(defaultValue = "1") Integer chunkCount,
            @RequestParam(defaultValue = "1") String chunkNo,
            @RequestParam String fileName,
            @RequestParam(value = "file", required = true) MultipartFile file) throws Exception {
        Assert.notNull(file, "文件不能为空");
        return fileUpload(batchNo, chunkCount, chunkNo, fileName, file, upload_path);
    }

    /**
     * 合成的文件存放路径 ${upload_path}/batchNo/batchNo.后缀
     * 分片文件存放路径 ${upload_path}/batchNo/batchNo+_+chunkNo
     *
     * @param batchNo     批次号
     * @param chunkCount  总片数
     * @param chunkNo     当前块编号
     * @param file        文件对象
     * @param upload_path 上传路径
     * @return
     * @throws Exception
     */
    private static UploadInfo fileUpload(String batchNo,
                                         Integer chunkCount,
                                         String chunkNo,
                                         String fileName,
                                         MultipartFile file,
                                         String upload_path) throws Exception {
        String newFileName = batchNo + "_" + chunkNo;
        String filePath = upload_path + File.separator + batchNo;
        String ext = UploadInfoUtil.getExt(fileName);
        UploadInfo uploadInfo;
        //如果是分块
        if (chunkCount != null && chunkCount > 1 && chunkNo != null) {
            uploadInfo = new UploadInfo.Builder(chunkCount, chunkNo, batchNo)
                    .fileName(fileName)
                    .filePath(filePath)
                    .fileSize(file.getSize())
                    .ext(ext)
                    .newFileName(newFileName)
                    .build();

            if (!UploadInfoUtil.uploadFileList.contains(uploadInfo)) {
                UploadInfoUtil.saveFile(filePath, newFileName, file);
                UploadInfoUtil.uploaded(uploadInfo);
            }
        } else {
            newFileName = batchNo + ext;
            uploadInfo = new UploadInfo.Builder(1, "1", batchNo)
                    .fileSize(file.getSize())
                    .filePath(filePath)
                    .fileName(fileName)
                    .newFileName(newFileName)
                    .ext(ext)
                    .build();
            if (!UploadInfoUtil.uploadFileList.contains(uploadInfo)) {
                UploadInfoUtil.saveFile(filePath, newFileName, file);
            }
        }
        return uploadInfo;
    }



}
