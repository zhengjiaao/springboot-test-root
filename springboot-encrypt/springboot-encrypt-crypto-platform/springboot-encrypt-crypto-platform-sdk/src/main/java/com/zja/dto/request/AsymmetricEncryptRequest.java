package com.zja.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 非对称加密请求
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "非对称加密请求（SM2/RSA公钥加密，仅适合小段数据）")
public class AsymmetricEncryptRequest {

	@ApiModelProperty(value = "非对称密钥对标识", required = true)
	private String keyId;

	@ApiModelProperty(value = "明文（UTF-8文本，建议不超过150字节）", required = true, example = "会话密钥")
	private String plainText;
}
