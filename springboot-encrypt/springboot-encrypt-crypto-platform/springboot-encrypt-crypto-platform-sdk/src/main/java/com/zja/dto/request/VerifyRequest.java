package com.zja.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 验签请求
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "公钥验签请求")
public class VerifyRequest {

	@ApiModelProperty(value = "非对称密钥对标识", required = true)
	private String keyId;

	@ApiModelProperty(value = "原始数据（UTF-8文本）", required = true, example = "待验签的报文")
	private String data;

	@ApiModelProperty(value = "签名值（Base64）", required = true)
	private String signature;
}
