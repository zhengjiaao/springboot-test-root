package com.zja.controller.parts6.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分片上传状态VO
 *
 * @author: zhengja
 * @since: 2026/04/23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("ChunkUploadStatusVO")
public class ChunkUploadStatusVO implements Serializable {

    @ApiModelProperty("文件唯一标识（MD5）")
    private String fileIdentifier;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("总分片数")
    private Integer totalChunks;

    @ApiModelProperty("已上传的分片序号列表")
    private List<Integer> uploadedChunks;

    @ApiModelProperty("已上传分片数量")
    private Integer uploadedCount;

    @ApiModelProperty("是否全部上传完成")
    private Boolean isComplete;
}
