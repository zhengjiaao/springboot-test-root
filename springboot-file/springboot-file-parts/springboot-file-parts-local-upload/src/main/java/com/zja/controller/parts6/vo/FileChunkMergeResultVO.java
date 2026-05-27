package com.zja.controller.parts6.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zja.controller.parts6.config.JacksonBase64Serializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文件分片合并结果视图对象
 *
 * @author: zhengja
 * @since: 2026/04/23 10:00:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("FileChunkMergeResultVO")
public class FileChunkMergeResultVO implements Serializable {

    @ApiModelProperty("合并结果是否成功")
    private Boolean success;

    @ApiModelProperty("文件唯一标识（MD5）")
    private String fileIdentifier;

    @ApiModelProperty("合并后的文件名")
    private String fileName;

    @ApiModelProperty("文件大小（字节）")
    private Long fileSize;

    @ApiModelProperty(value = "文件访问路径（Base64编码）")
    @JsonSerialize(using = JacksonBase64Serializer.class)
    private String filePath;

    @ApiModelProperty("消息")
    private String message;

    @ApiModelProperty("文件是否存在")
    private Boolean fileExists;
}
