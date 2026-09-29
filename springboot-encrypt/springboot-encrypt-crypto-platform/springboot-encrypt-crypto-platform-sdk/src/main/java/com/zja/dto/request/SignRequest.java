package com.zja.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 签名请求
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "私钥签名请求（SM2密钥→SM3withSM2签名；RSA密钥→SHA256withRSA签名）")
public class SignRequest {

	@ApiModelProperty(value = "非对称密钥对标识", required = true)
	private String keyId;

	@ApiModelProperty(value = "待签名数据（UTF-8文本）", required = true, example = "待签名的报文")
	private String data;
}
