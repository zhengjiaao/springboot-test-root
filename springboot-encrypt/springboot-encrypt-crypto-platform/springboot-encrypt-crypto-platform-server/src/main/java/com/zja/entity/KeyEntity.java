package com.zja.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zja.common.enums.AlgorithmType;
import lombok.Data;

import java.util.Date;

/**
 * 平台托管密钥
 *
 * <p>密钥材料仅保存在平台内部，任何对外视图（KeyView）都不得携带密钥材料。</p>
 *
 * @author: zhengja
 */
@Data
public class KeyEntity {

	/** 密钥标识（对外公开） */
	private String keyId;
	/** 归属应用appKey（密钥按应用隔离） */
	private String appKey;
	/** 算法 */
	private AlgorithmType algorithm;
	/** 密钥别名（便于业务识别） */
	private String alias;
	/** 备注 */
	private String remark;
	/** 对称/MAC密钥材料（Base64） */
	@JsonIgnore
	private String secretKeyBase64;
	/** 非对称公钥（X.509/SM2 Base64，可对外分发） */
	private String publicKeyBase64;
	/** 非对称私钥（PKCS#8/SM2 Base64，仅平台内部使用） */
	@JsonIgnore
	private String privateKeyBase64;
	/** 状态：ENABLED / DISABLED / DESTROYED */
	private String status;
	/** 创建时间 */
	private Date createTime;
}
