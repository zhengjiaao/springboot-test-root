package com.zja.report.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 存储节点(类似文件夹)
 * @Author: zhengja
 * @Date: 2024-09-04 16:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "storage_nodes")
@ApiModel("StorageNodeDTO")
public class StorageNodeDTO implements Serializable {

    @Id
    @ApiModelProperty(value = "存储节点id")
    private String id;

    @ApiModelProperty(value = "存储节点名称")
    private String name;

    @ApiModelProperty(value = "父节点id", notes = "根节点的父节点为空")
    private String parentId;

    @ApiModelProperty(value = "节点所属的存储空间", notes = "必须的，一般是业务id，例如：项目id、流程id等")
    private String spaceId;

    @ApiModelProperty(value = "节点创建时间")
    private String createTime;
}
