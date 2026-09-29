package com.zja.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 密钥视图（安全视图：不含任何密钥材料）
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "密钥视图（不含密钥材料）")
public class KeyView {

	@ApiModelProperty(value = "密钥标识")
	private String keyId;

	@ApiModelProperty(value = "归属应用appKey")
	private String appKey;

	@ApiModelProperty(value = "算法")
	private String algorithm;

	@ApiModelProperty(value = "算法类别：SYMMETRIC/ASYMMETRIC/MAC")
	private String category;

	@ApiModelProperty(value = "密钥别名")
	private String alias;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "公钥（仅非对称密钥返回）")
	private String publicKey;

	@ApiModelProperty(value = "状态：ENABLED/DISABLED")
	private String status;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;
}
