package com.zja.controller.parts6.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 通用文件分片上传请求参数
 *
 * @author: zhengja
 * @since: 2026/04/23 10:00:00
 */
@Data
@ApiModel("FileChunkUploadRequest")
public class FileChunkUploadRequest implements Serializable {

    @ApiModelProperty(value = "文件唯一标识（MD5）", required = true, example = "d41d8cd98f00b204e9800998ecf8427e")
    @NotBlank(message = "文件唯一标识不能为空")
    private String fileIdentifier;

    @ApiModelProperty(value = "文件名", required = true, example = "document.zip")
    @NotBlank(message = "文件名不能为空")
    private String fileName;

    @ApiModelProperty(value = "当前分片序号（从1开始）", required = true, example = "1")
    @NotNull(message = "分片序号不能为空")
    private Integer chunkNumber;

    @ApiModelProperty(value = "总分片数", required = true, example = "10")
    @NotNull(message = "总分片数不能为空")
    private Integer totalChunks;
}
