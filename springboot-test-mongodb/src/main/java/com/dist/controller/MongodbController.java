package com.dist.controller;

import com.dist.base.constant.Constants;
import com.dist.base.dto.ResourceFileDto;
import com.dist.common.MongoService;
import com.dist.common.RemoteTomcatService;
import com.dist.util.exception.IllegalParameterException;
import com.dist.response.Result;
import com.dist.response.ResultUtil;
import com.dist.utils.MultipartFileUtil;
import com.dist.utils.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/6/25 16:29
 */
@RestController
@Api(tags = "WEB-MongodbController",description = "资源文件")
@Slf4j
public class MongodbController extends BaseController{

    @Resource
    private MongoService mongoService;

    @Resource
    private RemoteTomcatService remoteTomcatService;

    @ApiOperation(value = "上传文件(公众/管理)", httpMethod = "POST")
    @RequestMapping(value = "/rest/public/resource/upload",method = RequestMethod.POST)
    public Result<ResourceFileDto> uploadMongo(
            @ApiParam(value = "上传的文件", required = true) @RequestParam(value = "file",required = false)MultipartFile multipartFile) {

        ResourceFileDto dto = new ResourceFileDto();

        if (ObjectUtil.isNull(multipartFile) || multipartFile.isEmpty()) {
            log.error("上传文件时，参数file为空");
            throw new IllegalParameterException("上传文件为空，请重新上传");
        }
        // 校验文件类型
        if (!MultipartFileUtil.isLegalFileType(multipartFile)) {
            log.error("上传文件时，文件类型不合法");
            throw new IllegalParameterException("文件类型不合法，请上传(jpg,png,jpeg,pdf)格式文件");
        }
        // 校验大小,pdf文件不参与大小校验
        if (!Constants.UPLOAD_FILE_LEGAL_SUFFIX.equalsIgnoreCase(MultipartFileUtil.getFileSuffix(multipartFile))
                && !MultipartFileUtil.isLegalFileSize(multipartFile)) {
            log.error("上传文件时，文件大小不合法");
            throw new IllegalParameterException("文件大小不合法，请上传不大于3MB的文件");
        }
        String fileName = multipartFile.getOriginalFilename();
        String fileSuffix = MultipartFileUtil.getFileSuffix(multipartFile);
        try {
            String path = mongoService.save(fileName, fileSuffix, multipartFile.getBytes());
            if (!StringUtils.isNoneEmpty(path)) {
                return ResultUtil.fail();
            }
            /*DistThreadManager.MyCacheThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    remoteTomcatService.transter(multipartFile, path, fileSuffix);
//                    localTomcatService.save(path, fileSuffix, multipartFile.getBytes(),getBaseURL(),getContextPath(localTomcatService.localFilePath));
                }
            });*/
            dto.setFilePath(path);
        } catch (IOException e) {
            log.error("MultipartFile存储mongo出错"+e);
            return ResultUtil.error("文件保存出错，请稍后再试");
        }
        dto.setFileName(fileName);
        dto.setFileSuffix(fileSuffix);
        //上传信息保存oracle数据库中
        /*ResourceFileDto resourceFileDto = resourceFileService.upload(dto);
        if (ObjectUtil.isNull(resourceFileDto)) {
            return ResultUtil.fail();
        }*/
        //return ResultUtil.success(dto2vo(resourceFileDto));


        return ResultUtil.success(dto);
    }

    @ApiOperation(value = "加载图片(公众/管理)", httpMethod = "GET")
    @RequestMapping(value = "/rest/public/resource/load/{id}",method = RequestMethod.GET)
    public void load(
            @ApiParam(value = "资源文件的id", required = true)@PathVariable(value = "id") String id) {

        /*ResourceFileDto resourceFileDto = resourceFileService.getResourceById(id);
        if (ObjectUtil.isNull(resourceFileDto)) {
            log.error("加载文件时，入参id:{}无查找到文件",id);
            response.setStatus(Constants.FILE_LOAD_ERROR);
            return ;
        }
        String path = file2path(resourceFileDto);*/
        String path=id;
        OutputStream os = null;
        try {
            byte[] file = mongoService.getFileByPath(path);
            MagicMatch match = Magic.getMagicMatch(file);
            String mimeType = match.getMimeType();
            // 设置响应的类型格式为图片格式
            response.setContentType(mimeType);
            os = response.getOutputStream();
            os.write(file);
            os.flush();
        } catch (Exception e) {
            log.error("加载图片失败",e);
            response.setStatus(Constants.FILE_LOAD_ERROR);
            return;
        } finally {
            //resourceFileDto = null;
            if (ObjectUtil.isNonNull(os)) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("关闭OutputStream失败",e);
                    response.setStatus(Constants.FILE_LOAD_ERROR);
                }
            }
        }
        return;
    }

    @ApiOperation(value = "删除资源文件(管理)", httpMethod = "DELETE")
    @RequestMapping(value = "/rest/private/resource/delete",method = RequestMethod.DELETE)
    public Result delete(
            @ApiParam(value = "资源文件的id", required = true)@RequestParam(value = "id",required = true) String id) {
        this.mongoService.delete(id);
        return  ResultUtil.success();
    }

    @ApiOperation(value = "下载资源文件(公众/管理)", httpMethod = "GET")
    @RequestMapping(value = "/rest/private/resource/downLoad",method = RequestMethod.GET)
    public void downLoad(
            @ApiParam(value = "资源文件的id", required = true)@RequestParam(value = "id",required = true) String id,
            @ApiParam(value = "资源文件新名称", required = true)@RequestParam(value = "fileName",required = true) String fileName){

        /*ResourceFileDto resourceFileDto = resourceFileService.getResourceById(id);
        if (ObjectUtil.isNull(resourceFileDto)) {
            log.error("下载文件时，入参id:{}无查找到文件",id);
            return;
        }
        String path = file2path(resourceFileDto);*/
        String path =id;
        //String fileName = resourceFileDto.getFileName();
        OutputStream os = null;
        try {
            byte[] file = mongoService.getFileByPath(path);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8") );
            response.setContentLength(file.length);
            os = response.getOutputStream();
            os.write(file);
            os.flush();
        } catch (IOException e) {
            log.error("下载资源文件失败");
        } finally {
            //resourceFileDto = null;
            if (ObjectUtil.isNonNull(os)) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("关闭OutputStream失败");
                }
            }
        }
    }

    @ApiOperation(value = "下载资源文件二进制文件流形式(公众/管理)", httpMethod = "GET")
    @RequestMapping(value = "/rest/private/resource/down",method = RequestMethod.GET)
    public ResponseEntity<byte[]> down(
            @ApiParam(value = "资源文件的id", required = true)@RequestParam(value = "id",required = true) String id,
            @ApiParam(value = "资源文件新名称", required = true)@RequestParam(value = "fileName",required = true) String fileName){

        /*ResourceFileDto resourceFileDto = resourceFileService.getResourceById(id);
        if (ObjectUtil.isNull(resourceFileDto)) {
            log.error("下载文件时，入参id:{}无查找到文件",id);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        String path = file2path(resourceFileDto);*/
        String path =id;
        byte[] file = mongoService.getFileByPath(path);
        //String fileName = resourceFileDto.getFileName();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",fileName);
        return new ResponseEntity<byte[]>(file,headers, HttpStatus.CREATED);
    }
}
