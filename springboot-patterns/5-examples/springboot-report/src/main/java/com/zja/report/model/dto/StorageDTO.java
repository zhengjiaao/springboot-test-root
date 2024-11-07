package com.zja.report.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 存储信息
 * @Author: zhengja
 * @Date: 2024-09-04 13:33
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("StorageDTO")
public class StorageDTO implements Serializable {

    @ApiModelProperty(value = "存储空间", notes = "必须的，一般是业务id，例如：项目id、流程id等")
    private String spaceId;

    @ApiModelProperty(value = "存储节点", notes = "必须的，与spaceId不同，你可以把nodeId当成是一个目录id")
    private String nodeId;

    @ApiModelProperty("文件id")
    private String fileId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件大小")
    private long fileSize;

    @ApiModelProperty("文件存储类型")
    private String storageType;

    @ApiModelProperty(value = "文件存储时间")
    private String createTime;
}
