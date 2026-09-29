package com.zja.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 摘要计算请求（无需密钥）
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "摘要计算请求（SM3/MD5/SHA_256）")
public class DigestRequest {

	@ApiModelProperty(value = "摘要算法：SM3/MD5/SHA_256", required = true, example = "SM3")
	private String algorithm;

	@ApiModelProperty(value = "原始数据（UTF-8文本）", required = true, example = "待摘要的数据")
	private String data;
}
