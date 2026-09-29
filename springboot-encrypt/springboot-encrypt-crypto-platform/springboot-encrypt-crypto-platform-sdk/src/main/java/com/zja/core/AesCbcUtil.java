package com.zja.core;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES 国际对称加密：AES/CBC/PKCS5Padding，密文格式 = IV(16字节) + 密文体
 *
 * <p>与 Sm4CbcUtil 保持相同的密文结构，便于三方系统在"国际算法 → 国密算法"切换时无缝替换。</p>
 *
 * @author: zhengja
 */
public final class AesCbcUtil {

	public static final int IV_SIZE = 16;
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

	private static final SecureRandom RANDOM = new SecureRandom();

	private AesCbcUtil() {
	}

	/** 生成AES密钥并返回Base64串（bits=128或256） */
	public static String generateKeyBase64(int bits) {
		if (bits != 128 && bits != 192 && bits != 256) {
			throw new IllegalArgumentException("AES密钥位数非法，可选：128/192/256");
		}
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(bits);
			return Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());
		} catch (Exception e) {
			throw new IllegalStateException("生成AES密钥失败", e);
		}
	}

	/**
	 * AES CBC 加密
	 *
	 * @param keyBase64 Base64密钥（16或32字节）
	 * @param plainText 明文
	 * @return Base64密文（前16字节为随机IV）
	 */
	public static String encrypt(String keyBase64, String plainText) {
		try {
			byte[] key = Base64.getDecoder().decode(keyBase64);
			requireValidKeyLength(key);
			byte[] iv = new byte[IV_SIZE];
			RANDOM.nextBytes(iv);

			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
			byte[] cipherBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

			byte[] out = new byte[IV_SIZE + cipherBytes.length];
			System.arraycopy(iv, 0, out, 0, IV_SIZE);
			System.arraycopy(cipherBytes, 0, out, IV_SIZE, cipherBytes.length);
			return Base64.getEncoder().encodeToString(out);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("AES加密失败：" + e.getMessage(), e);
		}
	}

	/**
	 * AES CBC 解密
	 *
	 * @param keyBase64        Base64密钥（16或32字节）
	 * @param cipherTextBase64 encrypt返回的Base64密文
	 * @return 明文
	 */
	public static String decrypt(String keyBase64, String cipherTextBase64) {
		try {
			byte[] key = Base64.getDecoder().decode(keyBase64);
			requireValidKeyLength(key);
			byte[] in = Base64.getDecoder().decode(cipherTextBase64);
			if (in.length <= IV_SIZE) {
				throw new IllegalArgumentException("密文长度非法");
			}

			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(in, 0, IV_SIZE));
			byte[] plainBytes = cipher.doFinal(in, IV_SIZE, in.length - IV_SIZE);
			return new String(plainBytes, StandardCharsets.UTF_8);
		} catch (IllegalStateException | IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("AES解密失败，请检查密文与密钥是否匹配", e);
		}
	}

	/** 校验AES密钥长度：16/24/32字节（128/192/256位） */
	private static void requireValidKeyLength(byte[] key) {
		if (key.length != 16 && key.length != 24 && key.length != 32) {
			throw new IllegalArgumentException("AES密钥长度非法：应为16/24/32字节，当前" + key.length + "字节");
		}
	}
}
