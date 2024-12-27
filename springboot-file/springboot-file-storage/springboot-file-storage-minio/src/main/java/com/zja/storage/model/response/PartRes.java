package com.zja.storage.model.response;

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
 * @since: 2024/12/23 14:28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("PartRes 新增 或 更新 信息")
public class PartRes implements Serializable {

    @ApiModelProperty("块编号")
    private int partNumber;

    @ApiModelProperty("块唯一标识(MD5值)")
    private String etag;

    @ApiModelProperty("最后修改时间")
    private String lastModified;

    @ApiModelProperty("块大小")
    private Long size;
}