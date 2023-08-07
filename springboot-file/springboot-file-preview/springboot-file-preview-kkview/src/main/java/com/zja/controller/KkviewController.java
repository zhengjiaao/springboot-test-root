/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-03 11:22
 * @Since:
 */
package com.zja.controller;

import cn.hutool.core.codec.Base64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kkview 文档预览
 *
 * @author: zhengja
 * @since: 2023/08/03 11:22
 */
@Validated
@RestController
@RequestMapping("/rest/kkview")
@Api(tags = {"Kkview 文档预览页面"})
public class KkviewController {

    //文件url
    //测试文件：docx.docx、jpg.jpg、pdf.pdf、txt.txt、zip.zip
//    private static final String FILE_URL = "http://localhost:19000/file/docx.docx";
    private static final String FILE_URL = "http://localhost:19000/file/pdf.pdf";

    //kkfileView 预览地址
    private static final String KKFILEVIEW_URL = "http://127.0.0.1:8012/onlinePreview?url=";

    @GetMapping("/get/preview/url/v1")
    @ApiOperation(value = "获取预览Url", notes = "预览组件kkfileView")
    public String getPreviewUrlV1() {

        //拼接预览地址，返回给前端进行预览
        return KKFILEVIEW_URL + Base64.encode(FILE_URL);
    }

    @GetMapping("/get/preview/url/v2")
    @ApiOperation(value = "获取预览Url", notes = "预览组件kkfileView")
    public String getPreviewUrlV2() {

        //拼接预览地址，返回给前端进行预览
        //添加指定预览名称
        return KKFILEVIEW_URL + Base64.encode(FILE_URL) + "&fullfilename=test.pdf";
    }

}