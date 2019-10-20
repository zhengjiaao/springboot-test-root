package com.dist.base.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 资源文件
 *
 * @author yinxp@dist.com.cn
 */
@Data
@ApiModel(value = "资源文件")
public class ResourceFileVo implements Serializable{

    @ApiModelProperty(value = "资源文件id")
    private String id;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "路径")
    private String filePath;

    /*@ApiModelProperty(value = "文件后缀")
    private String fileSuffix;*/

}
