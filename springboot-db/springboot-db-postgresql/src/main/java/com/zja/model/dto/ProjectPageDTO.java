package com.zja.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Project 数据传输
 *
 * @author: zhengja
 * @since: 2024/09/27 9:34
 */
@Data
@ApiModel("ProjectPageDTO")
public class ProjectPageDTO implements Serializable {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("最后一次更新时间")
    private LocalDateTime lastModifiedDate;

    @ApiModelProperty("批复时间")
    private Date approvalTime;
}