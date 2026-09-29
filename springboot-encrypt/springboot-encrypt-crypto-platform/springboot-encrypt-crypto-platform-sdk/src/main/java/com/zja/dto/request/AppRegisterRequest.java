package com.zja.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 应用注册请求
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "三方应用注册请求")
public class AppRegisterRequest {

	@ApiModelProperty(value = "应用名称", required = true, example = "业务系统A")
	private String appName;

	@ApiModelProperty(value = "负责人/联系方式", example = "zhangsan / 138xxxx")
	private String contact;

	@ApiModelProperty(value = "备注", example = "密改接入：订单数据加密")
	private String remark;
}
