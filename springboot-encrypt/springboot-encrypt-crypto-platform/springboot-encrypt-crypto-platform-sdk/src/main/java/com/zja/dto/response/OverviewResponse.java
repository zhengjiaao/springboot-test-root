package com.zja.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 平台概览响应（运维观测统计，不含任何敏感数据）
 *
 * @author: zhengja
 */
@Data
@ApiModel(description = "平台概览响应")
public class OverviewResponse {

	@ApiModelProperty(value = "应用总数")
	private int appCount;

	@ApiModelProperty(value = "启用状态应用数")
	private int enabledAppCount;

	@ApiModelProperty(value = "托管密钥总数")
	private int keyCount;

	@ApiModelProperty(value = "审计日志当前条数")
	private int auditLogCount;

	@ApiModelProperty(value = "审计日志保留上限（条）")
	private int auditLogCapacity;

	@ApiModelProperty(value = "nonce防重放缓存当前条数")
	private int nonceCacheSize;

	@ApiModelProperty(value = "nonce防重放缓存容量上限（条）")
	private int nonceCacheCapacity;

	@ApiModelProperty(value = "接入签名时间戳校验窗口（分钟）")
	private long authTimestampWindowMinutes;

	@ApiModelProperty(value = "请求体大小上限（字节）")
	private long maxBodySize;

	@ApiModelProperty(value = "平台支持的算法矩阵")
	private List<AlgorithmView> algorithms;
}
