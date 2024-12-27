package com.zja.storage.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 请求参数
 *
 * @author: zhengja
 * @since: 2024/12/23 16:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("ChunkUploadRes 新增 或 更新 信息")
public class ChunkUploadRes implements Serializable {

    @ApiModelProperty("名称")
    private String uploadId;

    @ApiModelProperty("分片上传URL+序号")
    private List<String> chunkUploadUrls;

}