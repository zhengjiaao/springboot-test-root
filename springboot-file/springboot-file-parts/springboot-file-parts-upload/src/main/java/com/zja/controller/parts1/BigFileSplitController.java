package com.zja.controller.parts1;

import com.zja.model.BigFileSplit;
import com.zja.util.BigFileSplitUtil;
import com.zja.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/9/6 14:42
 */
@Slf4j
@Api(tags = {"BigFileSplitController"}, description = "本地大文件分片、下载、合并")
@RestController
@RequestMapping(value = "file/split")
public class BigFileSplitController extends BaseController {

    //文件分片位置：分片后的文件路径  D:/FileTest/文件分片位置
    @Value("${source.fileFragmentationLocation}")
    private String fileFragmentationLocation;

    //分片工具类
    @Autowired
    private BigFileSplitUtil bigFileSplitUtil;

    @ApiOperation(value = "文件分片", notes = "分片大小交给前端决定，接口返回分片文件列表信息-->调用下载分片接口", httpMethod = "GET")
    @RequestMapping(value = "v1/getFileInfo", method = RequestMethod.GET)
    public Object getFileInfo(@ApiParam(value = "离线包文件相对路径", required = true) @RequestParam String pathName,
                              @ApiParam(value = "分片大小默20M=20*1024*1024", defaultValue = "20971520", required = true) @RequestParam long blockSize) {

        BigFileSplit split = new BigFileSplit();
        split.setFilePath(pathName);
        //获取文件名-不保留后缀
        String fileName = FileUtil.getFileNameExcludeSuffix(pathName);
        split.setDestPath(fileName);
        split.setBlockSize(blockSize);

        BigFileSplit bigFileSplit = bigFileSplitUtil.split(split);
        return bigFileSplit;
    }

    @ApiOperation(value = "下载文件分片版本1-支持前端异步多线程下载", notes = "建议使用，前端可以获取到分片的文件长度,做下载进度条用", httpMethod = "GET")
    @RequestMapping(value = "v1/downloadLocalFile", method = RequestMethod.GET)
    public void downloadLocalFile(@ApiParam(value = "分片列表中的相对路径", required = true) @RequestParam("splitFileName") String splitFileName) throws IOException {

        String fileName = splitFileName.substring(splitFileName.lastIndexOf("/") + 1);
        log.info(fileName +"_ 线程: _"+ Thread.currentThread().getName());

        String filePath = fileFragmentationLocation + File.separator + splitFileName;
        File file = new File(filePath);
        if (file.exists()) {
            InputStream in = null;
            ServletOutputStream out =null;
            try {
                byte[] buffer = FileUtil.File2byte(filePath);
                in = new ByteArrayInputStream(buffer);
                this.response.reset(); //必要的清除 response 缓存
                this.response.setContentType("application/force-download");
                this.response.addHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                //前端可以获取到分片的文件长度,做下载进度条用
                this.response.setContentLength(buffer.length);
                out = this.response.getOutputStream();
                StreamUtils.copy(in, out);
                log.info("分片下载-成功: ",fileName);
            } catch (Exception e) {
                log.error("分片下载-失败: " + fileName + " 错误", e);
            }finally {
                if (in != null) {
                    in.close();
                }
                if (out !=null){
                    out.flush();
                    out.close();
                }
            }
        } else {
            throw new RuntimeException("沒有找到指定文件");
        }
    }


    @ApiOperation(value = "下载文件分片版本2-推荐使用-前端异步多线程下载", notes = "建议使用这种方式，前端可以获取到分片的文件长度,做下载进度条用", httpMethod = "GET")
    @RequestMapping(value = "v2/downloadLocalFile", method = RequestMethod.GET)
    public void downloadLocalFile2(@ApiParam(value = "离线包分片路径", required = true) @RequestParam("splitFileName") String splitFileName, HttpServletResponse httpServletResponse) {

        //文件名称
        String fileName = splitFileName.substring(splitFileName.lastIndexOf("/") + 1);
        log.info(fileName +"_ 线程: _"+ Thread.currentThread().getName());
        //分片文件绝对路径
        String filePath = fileFragmentationLocation + File.separator + splitFileName;
        File file = new File(filePath);
        if (file.exists()) {
            try {
                //获取文件字节
                byte[] buffer = FileUtil.File2byte(filePath);
                httpServletResponse.setContentType("application/force-download");
                httpServletResponse.addHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                //前端可以获取到分片的文件长度,做下载进度条用
                httpServletResponse.setContentLength(buffer.length);
                ServletOutputStream outputStream = httpServletResponse.getOutputStream();
                outputStream.write(buffer);
                outputStream.flush();
                outputStream.close();
                log.info("分片下载-成功: ",fileName);
            } catch (Exception e) {
                log.error("分片下载-失败: " + fileName + " 错误", e);
            }
        } else {
            throw new RuntimeException("沒有找到指定分片文件: "+fileName);
        }
    }
}
