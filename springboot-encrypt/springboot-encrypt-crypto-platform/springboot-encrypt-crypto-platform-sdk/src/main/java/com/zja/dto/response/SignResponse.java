package com.zja.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 签名响应
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "签名响应")
public class SignResponse {

	@ApiModelProperty(value = "密钥标识")
	private String keyId;

	@ApiModelProperty(value = "签名算法：SM2密钥为SM3withSM2，RSA密钥为SHA256withRSA")
	private String algorithm;

	@ApiModelProperty(value = "签名值（Base64）")
	private String signature;
}
