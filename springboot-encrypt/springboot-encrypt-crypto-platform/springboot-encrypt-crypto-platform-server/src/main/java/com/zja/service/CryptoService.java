package com.zja.service;

import com.zja.common.enums.AlgorithmType;
import com.zja.common.enums.KeyCategory;
import com.zja.common.exception.BusinessException;
import com.zja.core.AesCbcUtil;
import com.zja.core.HmacUtil;
import com.zja.core.RsaCryptoUtil;
import com.zja.core.Sm2CryptoUtil;
import com.zja.core.Sm3DigestUtil;
import com.zja.core.Sm4CbcUtil;
import com.zja.dto.request.AsymmetricDecryptRequest;
import com.zja.dto.request.AsymmetricEncryptRequest;
import com.zja.dto.request.DigestRequest;
import com.zja.dto.request.MacRequest;
import com.zja.dto.request.SignRequest;
import com.zja.dto.request.SymmetricDecryptRequest;
import com.zja.dto.request.SymmetricEncryptRequest;
import com.zja.dto.request.VerifyRequest;
import com.zja.dto.response.DecryptResponse;
import com.zja.dto.response.DigestResponse;
import com.zja.dto.response.EncryptResponse;
import com.zja.dto.response.MacResponse;
import com.zja.dto.response.SignResponse;
import com.zja.dto.response.VerifyResponse;
import com.zja.entity.KeyEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

/**
 * 密码运算服务：加解密、摘要、MAC、签名验签（密钥不出平台，运算在平台侧完成）
 *
 * <p>所有运算前先做密钥归属/状态/类别校验，运算结果全部写审计。</p>
 *
 * @author: zhengja
 */
@Slf4j
@Service
public class CryptoService {

	private final KeyManageService keyManageService;
	private final AuditService auditService;

	public CryptoService(KeyManageService keyManageService, AuditService auditService) {
		this.keyManageService = keyManageService;
		this.auditService = auditService;
	}

	// ===== 对称加解密（SM4/AES） =====

	public EncryptResponse symmetricEncrypt(String appKey, SymmetricEncryptRequest request) {
		requireText(request == null ? null : request.getPlainText(), "plainText");
		long start = System.currentTimeMillis();
		KeyEntity key = keyManageService.loadOwnedEnabledKey(appKey, request.getKeyId(), KeyCategory.SYMMETRIC, "SYMMETRIC_ENCRYPT");
		try {
			String cipherText = encryptByAlgorithm(key, request.getPlainText());
			auditService.record(appKey, "SYMMETRIC_ENCRYPT", key.getKeyId(), key.getAlgorithm().name(), true, "对称加密", System.currentTimeMillis() - start);
			return buildEncryptResponse(key, cipherText);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			return failAndThrow(appKey, key, "SYMMETRIC_ENCRYPT", "对称加密失败：" + e.getMessage(), start);
		}
	}

	public DecryptResponse symmetricDecrypt(String appKey, SymmetricDecryptRequest request) {
		requireText(request == null ? null : request.getCipherText(), "cipherText");
		long start = System.currentTimeMillis();
		KeyEntity key = keyManageService.loadOwnedEnabledKey(appKey, request.getKeyId(), KeyCategory.SYMMETRIC, "SYMMETRIC_DECRYPT");
		try {
			String plainText = decryptByAlgorithm(key, request.getCipherText());
			auditService.record(appKey, "SYMMETRIC_DECRYPT", key.getKeyId(), key.getAlgorithm().name(), true, "对称解密", System.currentTimeMillis() - start);
			return buildDecryptResponse(key, plainText);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			return failAndThrow(appKey, key, "SYMMETRIC_DECRYPT", "对称解密失败：" + e.getMessage(), start);
		}
	}

	// ===== 非对称加解密（SM2/RSA） =====

	public EncryptResponse asymmetricEncrypt(String appKey, AsymmetricEncryptRequest request) {
		requireText(request == null ? null : request.getPlainText(), "plainText");
		long start = System.currentTimeMillis();
		KeyEntity key = keyManageService.loadOwnedEnabledKey(appKey, request.getKeyId(), KeyCategory.ASYMMETRIC, "ASYMMETRIC_ENCRYPT");
		try {
			String cipherText = key.getAlgorithm() == AlgorithmType.SM2
					? Sm2CryptoUtil.encryptByPublicKey(key.getPublicKeyBase64(), request.getPlainText())
					: RsaCryptoUtil.encryptByPublicKey(key.getPublicKeyBase64(), request.getPlainText());
			auditService.record(appKey, "ASYMMETRIC_ENCRYPT", key.getKeyId(), key.getAlgorithm().name(), true, "非对称公钥加密", System.currentTimeMillis() - start);
			return buildEncryptResponse(key, cipherText);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			return failAndThrow(appKey, key, "ASYMMETRIC_ENCRYPT", "非对称加密失败：" + e.getMessage(), start);
		}
	}

	public DecryptResponse asymmetricDecrypt(String appKey, AsymmetricDecryptRequest request) {
		requireText(request == null ? null : request.getCipherText(), "cipherText");
		long start = System.currentTimeMillis();
		KeyEntity key = keyManageService.loadOwnedEnabledKey(appKey, request.getKeyId(), KeyCategory.ASYMMETRIC, "ASYMMETRIC_DECRYPT");
		try {
			String plainText = key.getAlgorithm() == AlgorithmType.SM2
					? Sm2CryptoUtil.decryptByPrivateKey(key.getPrivateKeyBase64(), request.getCipherText())
					: RsaCryptoUtil.decryptByPrivateKey(key.getPrivateKeyBase64(), request.getCipherText());
			auditService.record(appKey, "ASYMMETRIC_DECRYPT", key.getKeyId(), key.getAlgorithm().name(), true, "非对称私钥解密", System.currentTimeMillis() - start);
			return buildDecryptResponse(key, plainText);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			return failAndThrow(appKey, key, "ASYMMETRIC_DECRYPT", "非对称解密失败：" + e.getMessage(), start);
		}
	}

	// ===== 摘要（SM3/MD5/SHA_256，无需密钥） =====

	public DigestResponse digest(String appKey, DigestRequest request) {
		requireText(request == null ? null : request.getData(), "data");
		if (request.getAlgorithm() == null) {
			throw BusinessException.badRequest("algorithm不能为空");
		}
		long start = System.currentTimeMillis();
		AlgorithmType algorithm = AlgorithmType.ofName(request.getAlgorithm());
		if (algorithm == null || algorithm.getCategory() != KeyCategory.DIGEST) {
			throw BusinessException.badRequest("非法摘要算法：" + request.getAlgorithm() + "，可选值：SM3/MD5/SHA_256");
		}
		try {
			String digest = digestByAlgorithm(algorithm, request.getData());
			auditService.record(appKey, "DIGEST", null, algorithm.name(), true, "计算摘要", System.currentTimeMillis() - start);
			DigestResponse response = new DigestResponse();
			response.setAlgorithm(algorithm.name());
			response.setDigest(digest);
			return response;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			auditService.record(appKey, "DIGEST", null, algorithm.name(), false, "摘要计算失败：" + e.getMessage(), System.currentTimeMillis() - start);
			throw BusinessException.error("摘要计算失败：" + e.getMessage());
		}
	}

	// ===== 消息认证码（HMAC-SM3/HMAC-SHA256） =====

	public MacResponse mac(String appKey, MacRequest request) {
		requireText(request == null ? null : request.getData(), "data");
		long start = System.currentTimeMillis();
		KeyEntity key = keyManageService.loadOwnedEnabledKey(appKey, request.getKeyId(), KeyCategory.MAC, "MAC");
		try {
			String macValue = key.getAlgorithm() == AlgorithmType.HMAC_SM3
					? HmacUtil.hmacSm3Hex(key.getSecretKeyBase64(), request.getData())
					: HmacUtil.hmacSha256Hex(key.getSecretKeyBase64(), request.getData());
			auditService.record(appKey, "MAC", key.getKeyId(), key.getAlgorithm().name(), true, "计算消息认证码", System.currentTimeMillis() - start);
			MacResponse response = new MacResponse();
			response.setKeyId(key.getKeyId());
			response.setAlgorithm(key.getAlgorithm().name());
			response.setMac(macValue);
			return response;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			return failAndThrow(appKey, key, "MAC", "MAC计算失败：" + e.getMessage(), start);
		}
	}

	// ===== 数字签名（SM2→SM3withSM2 / RSA→SHA256withRSA） =====

	public SignResponse sign(String appKey, SignRequest request) {
		requireText(request == null ? null : request.getData(), "data");
		long start = System.currentTimeMillis();
		KeyEntity key = keyManageService.loadOwnedEnabledKey(appKey, request.getKeyId(), KeyCategory.ASYMMETRIC, "SIGN");
		try {
			String signature = key.getAlgorithm() == AlgorithmType.SM2
					? Sm2CryptoUtil.signByPrivateKey(key.getPrivateKeyBase64(), request.getData())
					: RsaCryptoUtil.signByPrivateKey(key.getPrivateKeyBase64(), request.getData());
			auditService.record(appKey, "SIGN", key.getKeyId(), key.getAlgorithm().name(), true, "私钥签名", System.currentTimeMillis() - start);
			SignResponse response = new SignResponse();
			response.setKeyId(key.getKeyId());
			response.setAlgorithm(key.getAlgorithm() == AlgorithmType.SM2 ? "SM3withSM2" : "SHA256withRSA");
			response.setSignature(signature);
			return response;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			return failAndThrow(appKey, key, "SIGN", "签名失败：" + e.getMessage(), start);
		}
	}

	public VerifyResponse verify(String appKey, VerifyRequest request) {
		requireText(request == null ? null : request.getData(), "data");
		requireText(request == null ? null : request.getSignature(), "signature");
		long start = System.currentTimeMillis();
		KeyEntity key = keyManageService.loadOwnedEnabledKey(appKey, request.getKeyId(), KeyCategory.ASYMMETRIC, "VERIFY");
		try {
			boolean verified = key.getAlgorithm() == AlgorithmType.SM2
					? Sm2CryptoUtil.verifyByPublicKey(key.getPublicKeyBase64(), request.getData(), request.getSignature())
					: RsaCryptoUtil.verifyByPublicKey(key.getPublicKeyBase64(), request.getData(), request.getSignature());
			auditService.record(appKey, "VERIFY", key.getKeyId(), key.getAlgorithm().name(), verified, "公钥验签，结果：" + verified, System.currentTimeMillis() - start);
			VerifyResponse response = new VerifyResponse();
			response.setKeyId(key.getKeyId());
			response.setAlgorithm(key.getAlgorithm().name());
			response.setVerified(verified);
			return response;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			return failAndThrow(appKey, key, "VERIFY", "验签失败：" + e.getMessage(), start);
		}
	}

	// ===== 算法分发 =====

	private String encryptByAlgorithm(KeyEntity key, String plainText) {
		switch (key.getAlgorithm()) {
			case SM4:
				return Sm4CbcUtil.encrypt(key.getSecretKeyBase64(), plainText);
			case AES_128:
			case AES_256:
				return AesCbcUtil.encrypt(key.getSecretKeyBase64(), plainText);
			default:
				throw BusinessException.badRequest("非法对称算法：" + key.getAlgorithm());
		}
	}

	private String decryptByAlgorithm(KeyEntity key, String cipherText) {
		switch (key.getAlgorithm()) {
			case SM4:
				return Sm4CbcUtil.decrypt(key.getSecretKeyBase64(), cipherText);
			case AES_128:
			case AES_256:
				return AesCbcUtil.decrypt(key.getSecretKeyBase64(), cipherText);
			default:
				throw BusinessException.badRequest("非法对称算法：" + key.getAlgorithm());
		}
	}

	private String digestByAlgorithm(AlgorithmType algorithm, String data) {
		switch (algorithm) {
			case SM3:
				return Sm3DigestUtil.digestHex(data);
			case MD5:
				// 仅用于兼容历史系统（MD5已不安全），安全场景请使用SM3/SHA_256
				return DigestUtils.md5Hex(data);
			case SHA_256:
				return DigestUtils.sha256Hex(data);
			default:
				throw BusinessException.badRequest("非法摘要算法：" + algorithm);
		}
	}

	// ===== 辅助 =====

	private EncryptResponse buildEncryptResponse(KeyEntity key, String cipherText) {
		EncryptResponse response = new EncryptResponse();
		response.setKeyId(key.getKeyId());
		response.setAlgorithm(key.getAlgorithm().name());
		response.setCipherText(cipherText);
		return response;
	}

	private DecryptResponse buildDecryptResponse(KeyEntity key, String plainText) {
		DecryptResponse response = new DecryptResponse();
		response.setKeyId(key.getKeyId());
		response.setAlgorithm(key.getAlgorithm().name());
		response.setPlainText(plainText);
		return response;
	}

	private <T> T failAndThrow(String appKey, KeyEntity key, String action, String message, long start) {
		auditService.record(appKey, action, key.getKeyId(), key.getAlgorithm().name(), false, message, System.currentTimeMillis() - start);
		log.warn("密码运算失败：appKey={}, action={}, keyId={}, message={}", appKey, action, key.getKeyId(), message);
		throw BusinessException.error(message);
	}

	private void requireText(String value, String field) {
		if (value == null || value.isEmpty()) {
			throw BusinessException.badRequest(field + "不能为空");
		}
	}
}
