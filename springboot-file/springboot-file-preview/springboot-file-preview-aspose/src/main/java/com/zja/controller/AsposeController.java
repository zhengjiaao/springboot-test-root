/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-09-04 17:12
 * @Since:
 */
package com.zja.controller;

import com.aspose.words.License;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * @author: zhengja
 * @since: 2023/09/04 17:12
 */
@Validated
@RestController
@RequestMapping("/rest/aspose")
@Api(tags = {"页面"})
public class AsposeController {

    @PostMapping(value = "/post/upload/v1")
    @ApiOperation(value = "post-上传单文件", notes = "返回 true")
    public void postFile(@ApiParam("上传文件") @RequestPart(value = "file") MultipartFile file, HttpServletResponse response) throws Exception {
        System.out.println("上传文件：" + file.getOriginalFilename() + "  大小：" + file.getSize());

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);

        ByteArrayOutputStream byteArrayOutputStream = docToPdf(file.getInputStream());

        //直接下载
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode("test.pdf", "UTF-8"));

        //直接预览
        //inline：内联，文件以内联的方式处理（即网页或者页面的一部分）
//        response.setContentType("application/pdf");
//        response.addHeader("Content-Disposition", "inline;filename=" +
//                URLEncoder.encode("test.pdf", "UTF-8"));

        response.setContentLength(byteArrayOutputStream.size());
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(byteArrayOutputStream.toByteArray());

        byteArrayOutputStream.close();
        outputStream.close();
    }

    private static ByteArrayOutputStream docToPdf(InputStream wordStream) {
        try {
            long old = System.currentTimeMillis();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            com.aspose.words.Document doc = new com.aspose.words.Document(wordStream);
            doc.save(outputStream, com.aspose.words.SaveFormat.PDF);
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
            return outputStream;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}