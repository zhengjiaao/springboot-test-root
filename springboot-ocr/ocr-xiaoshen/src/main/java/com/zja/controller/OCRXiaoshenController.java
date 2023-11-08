/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 10:34
 * @Since:
 */
package com.zja.controller;

import com.zja.service.OCRXiaoshenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * OCR服务
 *
 * @author: zhengja
 * @since: 2023/11/08 10:34
 */
@Validated
@RestController
@RequestMapping("/rest/ocr/")
@Api(tags = {"OCR服务页面"})
public class OCRXiaoshenController {

    @Autowired
    OCRXiaoshenService service;

    private static final String ROOT_DIR = "D:\\file";

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    public String upload(@ApiParam("上传文件（可以从多种不同的文件类型（例如 PDF文件、Office文件、图像文件、XLS 和 超文本标记语言文件等）中检测并提取元数据和文本）") @RequestPart(value = "file") MultipartFile file) throws IOException {

        if (file.isEmpty() || file.getSize() <= 0) {
            return "未选择要上传的文件！";
        }

        System.out.println("上传文件：" + file.getOriginalFilename());

        String fileId = UUID.randomUUID().toString();
        String filePath = ROOT_DIR + File.separator + fileId + File.separator + file.getOriginalFilename();

        File tmpFile = new File(filePath);
        if (!tmpFile.getParentFile().exists()) {
            tmpFile.getParentFile().mkdirs();
        }

        //存储到本地临时
        file.transferTo(new File(filePath));

        return fileId;
    }

    @GetMapping("/autoExtractContent")
    @ApiOperation(value = "OCR自动识别文件格式进行提取文本内容", notes = "已启用提取pdf、word等中图片文本内容")
    public void autoExtractContent(@RequestParam String fileId) {

        String fileParentPath = ROOT_DIR + File.separator + fileId;
        File firstFile = getFirstFile(fileParentPath);

        String content = service.autoExtractContent(firstFile.getAbsolutePath());

        //将提取的结果 存储到固定的 result.txt 文件
        String resultFilePath = fileParentPath + File.separator + "result.txt";

        storeResult(resultFilePath, content);
    }

    @GetMapping("/download/result")
    @ApiOperation(value = "下载OCR提取文本的结果")
    public void downloadResult(HttpServletResponse response,
                               @RequestParam String fileId) throws IOException {
        String resultFilePath = ROOT_DIR + File.separator + fileId + File.separator + "result.txt";
        File file = new File(resultFilePath);
        if (!file.exists()) {
            throw new RuntimeException("不存在结果文件.");
        }

        FileInputStream inputStream = FileUtils.openInputStream(file);
        byte[] bytes = toByteArray(inputStream);
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode("result.txt", "UTF-8"));
        response.setContentLength(bytes.length);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.close();
    }

    private File getFirstFile(String fileParentPath) {
        File tmpFileDir = new File(fileParentPath);
        if (!tmpFileDir.exists()) {
            throw new RuntimeException("文件未上传.");
        }

        File[] files = tmpFileDir.listFiles();
        if (null == files) {
            throw new RuntimeException("文件未上传.");
        }

        File file = files[0];
        if (!file.exists()) {
            throw new RuntimeException("文件未上传.");
        }

        return file;
    }


    private void storeResult(String filePath, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadFile(HttpServletResponse response, String filename) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("file/" + filename);

        byte[] bytes = toByteArray(inputStream);
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode(filename, "UTF-8"));
        response.setContentLength(bytes.length);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.close();
    }

    private static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}