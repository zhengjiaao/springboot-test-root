package com.zja.common.enums;

import lombok.Getter;

/**
 * 平台支持的密码算法
 *
 * <p>密改对标关系：DES/AES → SM4，RSA → SM2，MD5/SHA → SM3，HMAC-SHA256 → HMAC-SM3。</p>
 *
 * @author: zhengja
 */
@Getter
public enum AlgorithmType {

	// ===== 对称加密（密钥：16字节SM4 / 16或32字节AES） =====
	SM4(KeyCategory.SYMMETRIC, true, "国密对称加密，CBC模式，密文前16位为随机IV"),
	AES_128(KeyCategory.SYMMETRIC, true, "国际对称加密128位密钥，CBC模式"),
	AES_256(KeyCategory.SYMMETRIC, true, "国际对称加密256位密钥，CBC模式"),

	// ===== 非对称加密（密钥对） =====
	SM2(KeyCategory.ASYMMETRIC, true, "国密非对称加密/签名（SM3withSM2），仅适合加密小段数据如会话密钥"),
	RSA_2048(KeyCategory.ASYMMETRIC, true, "国际非对称加密/签名，OAEP(SHA-256)填充，仅适合加密小段数据"),

	// ===== 摘要（无需密钥） =====
	SM3(KeyCategory.DIGEST, false, "国密摘要算法，输出256位"),
	MD5(KeyCategory.DIGEST, false, "国际摘要算法（已不安全，仅供过渡期校验）"),
	SHA_256(KeyCategory.DIGEST, false, "国际摘要算法，输出256位"),

	// ===== 消息认证码 =====
	HMAC_SM3(KeyCategory.MAC, true, "国密HMAC-SM3消息认证码"),
	HMAC_SHA_256(KeyCategory.MAC, true, "国际HMAC-SHA256消息认证码");

	/** 算法类别 */
	private final KeyCategory category;
	/** 是否支持在平台创建托管密钥（摘要算法无需密钥） */
	private final boolean keyCreatable;
	/** 算法说明 */
	private final String description;

	AlgorithmType(KeyCategory category, boolean keyCreatable, String description) {
		this.category = category;
		this.keyCreatable = keyCreatable;
		this.description = description;
	}

	/** 按名称安全解析，非法算法名返回null */
	public static AlgorithmType ofName(String name) {
		if (name == null) {
			return null;
		}
		for (AlgorithmType type : values()) {
			if (type.name().equalsIgnoreCase(name.trim())) {
				return type;
			}
		}
		return null;
	}
}
