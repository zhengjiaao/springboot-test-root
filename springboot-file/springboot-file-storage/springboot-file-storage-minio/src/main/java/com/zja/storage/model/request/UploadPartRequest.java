package com.zja.storage.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 请求参数
 *
 * @author: zhengja
 * @since: 2024/12/23 11:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("UploadPartRequest 新增 或 更新 信息")
public class UploadPartRequest implements Serializable {
    @ApiModelProperty("当前对象名称")
    String objectName;
    @ApiModelProperty("上传ID")
    String uploadId;
    @ApiModelProperty("当前分片的编号,从1开始")
    int partNumber;
    @ApiModelProperty("分片数据块")
    private MultipartFile file;
}