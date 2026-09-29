package com.zja.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 密钥创建响应（仅非对称密钥返回公钥，密钥材料永不返回）
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "密钥创建响应")
public class KeyCreateResponse {

	@ApiModelProperty(value = "密钥标识（后续加解密/签名时使用）")
	private String keyId;

	@ApiModelProperty(value = "算法")
	private String algorithm;

	@ApiModelProperty(value = "算法类别：SYMMETRIC/ASYMMETRIC/MAC")
	private String category;

	@ApiModelProperty(value = "密钥别名")
	private String alias;

	@ApiModelProperty(value = "公钥（仅非对称密钥返回，可分发给业务方加密/验签）")
	private String publicKey;

	@ApiModelProperty(value = "状态：ENABLED")
	private String status;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;
}
