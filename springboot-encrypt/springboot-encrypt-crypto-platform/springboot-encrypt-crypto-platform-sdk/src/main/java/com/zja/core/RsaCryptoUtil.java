package com.zja.core;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * RSA 国际非对称算法：密钥对生成、公钥加密（OAEP-SHA256）、私钥解密、私钥签名（SHA256withRSA）、公钥验签
 *
 * <p>密改对标：RSA → SM2，三方系统可直接换用平台 SM2 密钥完成替换。</p>
 *
 * @author: zhengja
 */
public final class RsaCryptoUtil {

	public static final String PUBLIC_KEY = "publicKey";
	public static final String PRIVATE_KEY = "privateKey";

	private static final int KEY_SIZE = 2048;
	private static final String CIPHER_TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
	private static final String SIGN_ALGORITHM = "SHA256withRSA";

	private RsaCryptoUtil() {
	}

	/**
	 * 生成 RSA-2048 密钥对
	 *
	 * @return {publicKey: X.509编码Base64公钥, privateKey: PKCS#8编码Base64私钥}
	 */
	public static Map<String, String> generateKeyPairBase64() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(KEY_SIZE);
			KeyPair keyPair = generator.generateKeyPair();
			Map<String, String> keyPairMap = new LinkedHashMap<>();
			keyPairMap.put(PUBLIC_KEY, Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
			keyPairMap.put(PRIVATE_KEY, Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
			return keyPairMap;
		} catch (Exception e) {
			throw new IllegalStateException("生成RSA密钥对失败", e);
		}
	}

	/**
	 * 公钥加密（OAEP-SHA256填充，仅适合加密小段数据，2048密钥上限约190字节）
	 *
	 * @param publicKeyBase64 X.509编码Base64公钥
	 * @param plainText       明文
	 * @return Base64密文
	 */
	public static String encryptByPublicKey(String publicKeyBase64, String plainText) {
		try {
			PublicKey publicKey = toPublicKey(publicKeyBase64);
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("RSA加密失败：" + e.getMessage(), e);
		}
	}

	/**
	 * 私钥解密
	 *
	 * @param privateKeyBase64 PKCS#8编码Base64私钥
	 * @param cipherTextBase64 公钥加密产生的Base64密文
	 * @return 明文
	 */
	public static String decryptByPrivateKey(String privateKeyBase64, String cipherTextBase64) {
		try {
			PrivateKey privateKey = toPrivateKey(privateKeyBase64);
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] plain = cipher.doFinal(Base64.getDecoder().decode(cipherTextBase64));
			return new String(plain, StandardCharsets.UTF_8);
		} catch (IllegalStateException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("RSA解密失败，请检查密文与密钥是否匹配", e);
		}
	}

	/**
	 * 私钥签名（SHA256withRSA）
	 *
	 * @param privateKeyBase64 PKCS#8编码Base64私钥
	 * @param data             待签名数据
	 * @return Base64签名值
	 */
	public static String signByPrivateKey(String privateKeyBase64, String data) {
		try {
			Signature signature = Signature.getInstance(SIGN_ALGORITHM);
			signature.initSign(toPrivateKey(privateKeyBase64));
			signature.update(data.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(signature.sign());
		} catch (Exception e) {
			throw new IllegalStateException("RSA签名失败：" + e.getMessage(), e);
		}
	}

	/**
	 * 公钥验签（SHA256withRSA）
	 *
	 * @param publicKeyBase64 X.509编码Base64公钥
	 * @param data            原始数据
	 * @param signBase64      Base64签名值
	 * @return 验签结果
	 */
	public static boolean verifyByPublicKey(String publicKeyBase64, String data, String signBase64) {
		try {
			Signature signature = Signature.getInstance(SIGN_ALGORITHM);
			signature.initVerify(toPublicKey(publicKeyBase64));
			signature.update(data.getBytes(StandardCharsets.UTF_8));
			return signature.verify(Base64.getDecoder().decode(signBase64));
		} catch (Exception e) {
			return false;
		}
	}

	private static PublicKey toPublicKey(String publicKeyBase64) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(publicKeyBase64);
		return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(bytes));
	}

	private static PrivateKey toPrivateKey(String privateKeyBase64) throws Exception {
		byte[] bytes = Base64.getDecoder().decode(privateKeyBase64);
		return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(bytes));
	}
}
