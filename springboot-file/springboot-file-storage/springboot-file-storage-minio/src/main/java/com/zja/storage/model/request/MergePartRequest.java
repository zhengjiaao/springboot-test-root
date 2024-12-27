package com.zja.storage.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 请求参数
 *
 * @author: zhengja
 * @since: 2024/12/23 13:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("MergePartRequest 新增 或 更新 信息")
public class MergePartRequest implements Serializable {

    @ApiModelProperty("上传ID")
    private String uploadId;

    @ApiModelProperty("对象名称(文件名称)")
    private String objectName;

    @ApiModelProperty("分片数量")
    private Integer totalChunks;

    @ApiModelProperty("文件大小")
    private Long fileSize;

    @ApiModelProperty("文件类型")
    private String contentType;
}