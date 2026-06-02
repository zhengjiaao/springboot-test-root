package com.zja.parts.download.controller.parts5.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件列表条目
 *
 * @Author: zhengja
 * @Date: 2024-09-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("文件列表条目")
public class FileItemResult {

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件大小(字节)")
    private long fileSize;

    @ApiModelProperty("格式化后的文件大小(如 1.5GB)")
    private String fileSizeFormatted;

    @ApiModelProperty("最后修改时间")
    private long lastModified;

    @ApiModelProperty("文件MD5校验值(可选)")
    private String md5;
}
