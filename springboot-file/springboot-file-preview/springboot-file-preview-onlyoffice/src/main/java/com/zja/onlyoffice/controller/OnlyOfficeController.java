package com.zja.onlyoffice.controller;

import com.alibaba.fastjson.JSON;
import com.zja.onlyoffice.fegin.OnlyOfficeAdditionalFeign;
import com.zja.onlyoffice.fegin.OnlyOfficeFeign;
import com.zja.onlyoffice.fegin.model.ConversionArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 * @Author: zhengja
 * @Date: 2025-01-22 13:25
 */
@RestController
@RequestMapping("/rest/onlyoffice")
public class OnlyOfficeController {

    @Autowired
    private OnlyOfficeAdditionalFeign additionalFeign;

    // https://example.com/url-to-example-document.docx
    @PostMapping("/converter")
    public ResponseEntity<String> converter(@RequestParam("url") String url, @RequestParam("filename") String filename, @RequestParam("outputType") String outputType) throws IOException {

        // 构建 ConversionArgs 对象
        ConversionArgs args = ConversionArgs.builder()
                .async(false)
                .key("Khirz6zTPdfd7")
                .url(url) // 需要实际上传文件到 OnlyOffice 文件URL，例如：https://example.com/path/to/file.docx
                .filetype(Objects.requireNonNull(filename).substring(filename.lastIndexOf('.') + 1))
                .outputtype(outputType) // 输出文件类型，例如：pdf
                .title(filename)
                .build();

        System.out.println(JSON.toJSONString(args));
        // {"async":false,"filetype":"docx","key":"Khirz6zTPdfd7","outputtype":"pdf","title":"example-document.docx","url":"https://example.com/url-to-example-document.docx"}

        // 调用 OnlyOffice 进行转换
        Object result = additionalFeign.converter(args);
        System.out.println(result);
        // {fileUrl=http://192.168.159.148:8999/cache/files/data/conv_Khirz6zTPdfd7_513/output.pdf/example-document.pdf?md5=GiPEnEzeE2jiHz-KRlhZ7Q&expires=1737534751&filename=example-document.pdf, fileType=pdf, percent=100, endConvert=true}

        // 返回转换结果或提供下载链接
        return ResponseEntity.ok("文件已成功转换.");
    }
}
