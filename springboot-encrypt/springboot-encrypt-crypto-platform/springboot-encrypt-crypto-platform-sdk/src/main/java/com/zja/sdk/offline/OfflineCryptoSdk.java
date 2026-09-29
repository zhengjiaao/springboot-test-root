package com.zja.sdk.offline;

import com.zja.common.enums.AlgorithmType;
import com.zja.common.enums.KeyCategory;
import com.zja.core.AesCbcUtil;
import com.zja.core.HmacUtil;
import com.zja.core.RsaCryptoUtil;
import com.zja.core.Sm2CryptoUtil;
import com.zja.core.Sm3DigestUtil;
import com.zja.core.Sm4CbcUtil;
import com.zja.dto.response.DecryptResponse;
import com.zja.dto.response.DigestResponse;
import com.zja.dto.response.EncryptResponse;
import com.zja.dto.response.MacResponse;
import com.zja.dto.response.SignResponse;
import com.zja.dto.response.VerifyResponse;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 密码服务平台离线SDK（面向三方系统，本地运算、密钥自管）
 *
 * <p>适用场景：三方系统与平台间网络不通（如内网隔离、专网前置机），无法在线调用平台接口。
 * SDK在本地完成与平台完全一致的密码运算（同算法、同编码格式）：</p>
 * <ul>
 *   <li>密钥由三方系统自行生成并在本地保管（可持久化OfflineKey后重启加载）；</li>
 *   <li>也支持导入平台通过安全渠道（加密信封/线下介质）分发的密钥材料；</li>
 *   <li>因编码格式与平台一致，平台加密的数据可在离线端解密，反之亦然。</li>
 * </ul>
 *
 * <p>密评提示：离线接入模式下密钥为自管，需自行满足密钥的生成、存储、备份、销毁等全生命周期管控要求；
 * 若密评要求集中密钥管理，请优先选择在线SDK/HTTP接口接入。</p>
 *
 * <pre>
 * OfflineCryptoSdk sdk = new OfflineCryptoSdk();
 * OfflineKey key = sdk.createKey("SM4", "本地数据加密密钥");
 * String cipher = sdk.symmetricEncrypt(key.getKeyId(), "机密数据");
 * </pre>
 *
 * @author: zhengja
 */
public class OfflineCryptoSdk {

	/** 本地密钥库：keyId -> OfflineKey */
	private final Map<String, OfflineKey> localKeyStore = new ConcurrentHashMap<>();

	// ===== 密钥管理（本地自管） =====

	/**
	 * 在本地生成密钥（密钥材料保存在本SDK实例中）
	 *
	 * @param algorithm 算法：SM4/AES_128/AES_256/SM2/RSA_2048/HMAC_SM3/HMAC_SHA_256
	 */
	public OfflineKey createKey(String algorithm, String alias) {
		AlgorithmType type = requireAlgorithm(algorithm);
		if (!type.isKeyCreatable()) {
			throw new IllegalArgumentException("算法" + type + "为摘要算法，无需密钥");
		}

		OfflineKey key = new OfflineKey();
		key.setKeyId("offline-key-" + UUID.randomUUID().toString().replace("-", ""));
		key.setAlgorithm(type.name());
		key.setCategory(type.getCategory().name());
		key.setAlias(alias == null || alias.trim().isEmpty() ? type.name() + "-default" : alias.trim());
		key.setCreateTime(new Date());

		switch (type) {
			case SM4:
				key.setSecretKeyBase64(Sm4CbcUtil.generateKeyBase64());
				break;
			case AES_128:
				key.setSecretKeyBase64(AesCbcUtil.generateKeyBase64(128));
				break;
			case AES_256:
				key.setSecretKeyBase64(AesCbcUtil.generateKeyBase64(256));
				break;
			case HMAC_SM3:
			case HMAC_SHA_256:
				key.setSecretKeyBase64(HmacUtil.generateKeyBase64());
				break;
			case SM2: {
				Map<String, String> keyPair = Sm2CryptoUtil.generateKeyPairBase64();
				key.setPublicKeyBase64(keyPair.get(Sm2CryptoUtil.PUBLIC_KEY));
				key.setPrivateKeyBase64(keyPair.get(Sm2CryptoUtil.PRIVATE_KEY));
				break;
			}
			case RSA_2048: {
				Map<String, String> keyPair = RsaCryptoUtil.generateKeyPairBase64();
				key.setPublicKeyBase64(keyPair.get(Sm2CryptoUtil.PUBLIC_KEY));
				key.setPrivateKeyBase64(keyPair.get(Sm2CryptoUtil.PRIVATE_KEY));
				break;
			}
			default:
				throw new IllegalArgumentException("不支持的算法：" + algorithm);
		}
		localKeyStore.put(key.getKeyId(), key);
		return key;
	}

	/**
	 * 导入密钥材料（如平台通过安全渠道分发的密钥，或从本地持久化文件加载）
	 */
	public OfflineKey importKey(OfflineKey key) {
		if (key == null || key.getKeyId() == null || key.getAlgorithm() == null) {
			throw new IllegalArgumentException("导入的密钥缺少keyId或algorithm");
		}
		requireAlgorithm(key.getAlgorithm());
		localKeyStore.put(key.getKeyId(), key);
		return key;
	}

	/** 获取密钥（含密钥材料，注意保管） */
	public OfflineKey getKey(String keyId) {
		return requireKey(keyId);
	}

	/** 全部密钥列表 */
	public List<OfflineKey> listKeys() {
		return localKeyStore.values().stream()
				.sorted((a, b) -> Long.compare(a.getCreateTime().getTime(), b.getCreateTime().getTime()))
				.collect(Collectors.toList());
	}

	/** 删除本地密钥 */
	public void removeKey(String keyId) {
		localKeyStore.remove(keyId);
	}

	// ===== 对称加解密（SM4/AES） =====

	public EncryptResponse symmetricEncrypt(String keyId, String plainText) {
		OfflineKey key = requireCategory(keyId, KeyCategory.SYMMETRIC);
		String cipherText = key.getAlgorithm().equals(AlgorithmType.SM4.name())
				? Sm4CbcUtil.encrypt(key.getSecretKeyBase64(), plainText)
				: AesCbcUtil.encrypt(key.getSecretKeyBase64(), plainText);
		return buildEncryptResponse(key, cipherText);
	}

	public DecryptResponse symmetricDecrypt(String keyId, String cipherText) {
		OfflineKey key = requireCategory(keyId, KeyCategory.SYMMETRIC);
		String plainText = key.getAlgorithm().equals(AlgorithmType.SM4.name())
				? Sm4CbcUtil.decrypt(key.getSecretKeyBase64(), cipherText)
				: AesCbcUtil.decrypt(key.getSecretKeyBase64(), cipherText);
		return buildDecryptResponse(key, plainText);
	}

	// ===== 非对称加解密（SM2/RSA） =====

	public EncryptResponse asymmetricEncrypt(String keyId, String plainText) {
		OfflineKey key = requireCategory(keyId, KeyCategory.ASYMMETRIC);
		String cipherText = key.getAlgorithm().equals(AlgorithmType.SM2.name())
				? Sm2CryptoUtil.encryptByPublicKey(key.getPublicKeyBase64(), plainText)
				: RsaCryptoUtil.encryptByPublicKey(key.getPublicKeyBase64(), plainText);
		return buildEncryptResponse(key, cipherText);
	}

	public DecryptResponse asymmetricDecrypt(String keyId, String cipherText) {
		OfflineKey key = requireCategory(keyId, KeyCategory.ASYMMETRIC);
		String plainText = key.getAlgorithm().equals(AlgorithmType.SM2.name())
				? Sm2CryptoUtil.decryptByPrivateKey(key.getPrivateKeyBase64(), cipherText)
				: RsaCryptoUtil.decryptByPrivateKey(key.getPrivateKeyBase64(), cipherText);
		return buildDecryptResponse(key, plainText);
	}

	// ===== 摘要 / MAC / 签名（与平台同格式） =====

	public DigestResponse digest(String algorithm, String data) {
		AlgorithmType type = requireAlgorithm(algorithm);
		if (type.getCategory() != KeyCategory.DIGEST) {
			throw new IllegalArgumentException("非法摘要算法：" + algorithm + "，可选：SM3/MD5/SHA_256");
		}
		String digest;
		switch (type) {
			case SM3:
				digest = Sm3DigestUtil.digestHex(data);
				break;
			case MD5:
				digest = DigestUtils.md5Hex(data);
				break;
			case SHA_256:
				digest = DigestUtils.sha256Hex(data);
				break;
			default:
				throw new IllegalArgumentException("非法摘要算法：" + algorithm);
		}
		DigestResponse response = new DigestResponse();
		response.setAlgorithm(type.name());
		response.setDigest(digest);
		return response;
	}

	public MacResponse mac(String keyId, String data) {
		OfflineKey key = requireCategory(keyId, KeyCategory.MAC);
		String mac = key.getAlgorithm().equals(AlgorithmType.HMAC_SM3.name())
				? HmacUtil.hmacSm3Hex(key.getSecretKeyBase64(), data)
				: HmacUtil.hmacSha256Hex(key.getSecretKeyBase64(), data);
		MacResponse response = new MacResponse();
		response.setKeyId(key.getKeyId());
		response.setAlgorithm(key.getAlgorithm());
		response.setMac(mac);
		return response;
	}

	public SignResponse sign(String keyId, String data) {
		OfflineKey key = requireCategory(keyId, KeyCategory.ASYMMETRIC);
		boolean isSm2 = key.getAlgorithm().equals(AlgorithmType.SM2.name());
		String signature = isSm2
				? Sm2CryptoUtil.signByPrivateKey(key.getPrivateKeyBase64(), data)
				: RsaCryptoUtil.signByPrivateKey(key.getPrivateKeyBase64(), data);
		SignResponse response = new SignResponse();
		response.setKeyId(key.getKeyId());
		response.setAlgorithm(isSm2 ? "SM3withSM2" : "SHA256withRSA");
		response.setSignature(signature);
		return response;
	}

	public VerifyResponse verify(String keyId, String data, String signature) {
		OfflineKey key = requireCategory(keyId, KeyCategory.ASYMMETRIC);
		boolean verified = key.getAlgorithm().equals(AlgorithmType.SM2.name())
				? Sm2CryptoUtil.verifyByPublicKey(key.getPublicKeyBase64(), data, signature)
				: RsaCryptoUtil.verifyByPublicKey(key.getPublicKeyBase64(), data, signature);
		VerifyResponse response = new VerifyResponse();
		response.setKeyId(key.getKeyId());
		response.setAlgorithm(key.getAlgorithm());
		response.setVerified(verified);
		return response;
	}

	// ===== 内部方法 =====

	private OfflineKey requireKey(String keyId) {
		OfflineKey key = keyId == null ? null : localKeyStore.get(keyId);
		if (key == null) {
			throw new IllegalArgumentException("本地密钥不存在：" + keyId);
		}
		return key;
	}

	private OfflineKey requireCategory(String keyId, KeyCategory expected) {
		OfflineKey key = requireKey(keyId);
		if (!expected.name().equals(key.getCategory())) {
			throw new IllegalArgumentException("密钥算法类别不匹配：需要" + expected + "，当前为" + key.getCategory() + "（" + key.getAlgorithm() + "）");
		}
		return key;
	}

	private AlgorithmType requireAlgorithm(String algorithm) {
		AlgorithmType type = AlgorithmType.ofName(algorithm);
		if (type == null) {
			throw new IllegalArgumentException("不支持的算法：" + algorithm
					+ "，可选值：SM4/AES_128/AES_256/SM2/RSA_2048/HMAC_SM3/HMAC_SHA_256/SM3/MD5/SHA_256");
		}
		return type;
	}

	private EncryptResponse buildEncryptResponse(OfflineKey key, String cipherText) {
		EncryptResponse response = new EncryptResponse();
		response.setKeyId(key.getKeyId());
		response.setAlgorithm(key.getAlgorithm());
		response.setCipherText(cipherText);
		return response;
	}

	private DecryptResponse buildDecryptResponse(OfflineKey key, String plainText) {
		DecryptResponse response = new DecryptResponse();
		response.setKeyId(key.getKeyId());
		response.setAlgorithm(key.getAlgorithm());
		response.setPlainText(plainText);
		return response;
	}
}
