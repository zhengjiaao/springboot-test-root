package com.zja.controller.parts6.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 分片上传状态查询请求
 */
@Data
@ApiModel("ChunkUploadStatusRequest")
public class ChunkUploadStatusRequest implements Serializable {

    @ApiModelProperty(value = "文件唯一标识（MD5）", required = true, example = "d41d8cd98f00b204e9800998ecf8427e")
    @NotBlank(message = "文件唯一标识不能为空")
    private String fileIdentifier;

    @ApiModelProperty(value = "文件名", required = true, example = "document.pdf")
    @NotBlank(message = "文件名不能为空")
    private String fileName;

    @ApiModelProperty(value = "总分片数", required = true, example = "10")
    @NotNull(message = "总分片数不能为空")
    private Integer totalChunks;
}
