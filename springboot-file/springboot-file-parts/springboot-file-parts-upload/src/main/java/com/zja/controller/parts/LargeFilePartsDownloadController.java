package com.zja.controller.parts;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 大文件：一般借助浏览器自带的下载功能，若是移动端APP应用内部下载，则需要进行分片下载
 *
 * @author: zhengja
 * @since: 2024/01/22 13:22
 */
@Api(value = "大文件分片下载", tags = {"LargeFilePartsDownloadController"})
@RestController
@RequestMapping(value = "large/file/parts/download")
public class LargeFilePartsDownloadController {

}
