package com.zja.parts.download.controller.parts5.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件信息查询结果
 *
 * @Author: zhengja
 * @Date: 2024-09-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("文件信息查询结果")
public class FileInfoResult {

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件总大小(字节)")
    private long fileSize;

    @ApiModelProperty("建议分片大小(字节)")
    private long chunkSize;

    @ApiModelProperty("总分片数")
    private long totalChunks;

    @ApiModelProperty("文件MD5校验值(可选，用于下载后完整性校验)")
    private String md5;

    @ApiModelProperty("文件是否存在")
    private boolean exists;
}
