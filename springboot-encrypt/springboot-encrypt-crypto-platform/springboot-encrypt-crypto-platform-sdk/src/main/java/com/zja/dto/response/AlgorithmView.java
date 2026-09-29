package com.zja.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 算法视图（平台能力矩阵条目，供控制台渲染算法列表）
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "算法视图")
public class AlgorithmView {

	@ApiModelProperty(value = "算法名称：如SM4/SM2/SM3")
	private String name;

	@ApiModelProperty(value = "算法类别：SYMMETRIC/ASYMMETRIC/DIGEST/MAC")
	private String category;

	@ApiModelProperty(value = "是否支持在平台创建托管密钥")
	private boolean keyCreatable;

	@ApiModelProperty(value = "算法说明")
	private String description;
}
