package com.zja.file.minio.upload.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * UploadPart 数据传输
 *
 * @author: zhengja
 * @since: 2024/12/23 14:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("UploadPartRes")
public class UploadPartRes implements Serializable {
    @ApiModelProperty("上传ID")
    private String uploadId;
    @ApiModelProperty("上传分片编号")
    private int partNumber;
    @ApiModelProperty("唯一标识(MD5)")
    private String etag;
}