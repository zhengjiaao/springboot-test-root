package com.zja.sdk.offline;

import lombok.Data;

import java.util.Date;

/**
 * 离线SDK本地密钥（密钥自管模式：密钥材料由三方系统自己持有）
 *
 * @author: zhengja
 */
@Data
public class OfflineKey {

	/** 密钥标识 */
	private String keyId;
	/** 算法名（与平台AlgorithmType一致） */
	private String algorithm;
	/** 算法类别：SYMMETRIC/ASYMMETRIC/MAC */
	private String category;
	/** 密钥别名 */
	private String alias;
	/** 对称/MAC密钥材料（Base64） */
	private String secretKeyBase64;
	/** 非对称公钥（Base64） */
	private String publicKeyBase64;
	/** 非对称私钥（Base64） */
	private String privateKeyBase64;
	/** 创建时间 */
	private Date createTime;
}
