package com.zja.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 对称加密请求
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "对称加密请求（SM4/AES）")
public class SymmetricEncryptRequest {

	@ApiModelProperty(value = "对称密钥标识", required = true)
	private String keyId;

	@ApiModelProperty(value = "明文（UTF-8文本）", required = true, example = "待加密的业务数据")
	private String plainText;
}
