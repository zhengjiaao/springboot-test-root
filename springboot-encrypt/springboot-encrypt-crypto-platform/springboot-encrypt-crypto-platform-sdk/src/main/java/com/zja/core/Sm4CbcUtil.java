package com.zja.core;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

/**
 * SM4 国密对称加密：SM4/CBC/PKCS5Padding，密文格式 = IV(16字节) + 密文体
 *
 * <p>每次加密生成随机IV并拼在密文前部，解密时自动取出，保证相同明文每次密文不同（语义安全）。</p>
 *
 * @author: zhengja
 */
public final class Sm4CbcUtil {

	/** SM4 密钥长度：128位 */
	public static final int KEY_SIZE = 16;
	/** CBC分组长度 */
	private static final int IV_SIZE = 16;
	private static final String TRANSFORMATION = "SM4/CBC/PKCS5Padding";

	private static final SecureRandom RANDOM = new SecureRandom();
	private static volatile boolean providerRegistered = false;

	private Sm4CbcUtil() {
	}

	private static void ensureBcProvider() {
		if (!providerRegistered) {
			synchronized (Sm4CbcUtil.class) {
				if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
					Security.addProvider(new BouncyCastleProvider());
				}
				providerRegistered = true;
			}
		}
	}

	/** 生成16字节SM4密钥 */
	public static byte[] generateKey() {
		byte[] key = new byte[KEY_SIZE];
		RANDOM.nextBytes(key);
		return key;
	}

	/** 生成16字节SM4密钥并返回Base64串 */
	public static String generateKeyBase64() {
		return Base64.getEncoder().encodeToString(generateKey());
	}

	/**
	 * SM4 CBC 加密
	 *
	 * @param keyBase64 Base64密钥（16字节）
	 * @param plainText 明文
	 * @return Base64密文（前16字节为随机IV）
	 */
	public static String encrypt(String keyBase64, String plainText) {
		try {
			ensureBcProvider();
			byte[] key = Base64.getDecoder().decode(keyBase64);
			requireValidKeyLength(key);
			byte[] iv = new byte[IV_SIZE];
			RANDOM.nextBytes(iv);

			Cipher cipher = Cipher.getInstance(TRANSFORMATION, BouncyCastleProvider.PROVIDER_NAME);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "SM4"), new IvParameterSpec(iv));
			byte[] cipherBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

			byte[] out = new byte[IV_SIZE + cipherBytes.length];
			System.arraycopy(iv, 0, out, 0, IV_SIZE);
			System.arraycopy(cipherBytes, 0, out, IV_SIZE, cipherBytes.length);
			return Base64.getEncoder().encodeToString(out);
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("SM4加密失败：" + e.getMessage(), e);
		}
	}

	/**
	 * SM4 CBC 解密
	 *
	 * @param keyBase64        Base64密钥（16字节）
	 * @param cipherTextBase64 encrypt返回的Base64密文
	 * @return 明文
	 */
	public static String decrypt(String keyBase64, String cipherTextBase64) {
		try {
			ensureBcProvider();
			byte[] key = Base64.getDecoder().decode(keyBase64);
			requireValidKeyLength(key);
			byte[] in = Base64.getDecoder().decode(cipherTextBase64);
			if (in.length <= IV_SIZE) {
				throw new IllegalArgumentException("密文长度非法");
			}

			IvParameterSpec iv = new IvParameterSpec(in, 0, IV_SIZE);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION, BouncyCastleProvider.PROVIDER_NAME);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "SM4"), iv);
			byte[] plainBytes = cipher.doFinal(in, IV_SIZE, in.length - IV_SIZE);
			return new String(plainBytes, StandardCharsets.UTF_8);
		} catch (IllegalStateException | IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException("SM4解密失败，请检查密文与密钥是否匹配", e);
		}
	}

	/** 校验SM4密钥长度：固定16字节（128位） */
	private static void requireValidKeyLength(byte[] key) {
		if (key.length != KEY_SIZE) {
			throw new IllegalArgumentException("SM4密钥长度非法：应为" + KEY_SIZE + "字节，当前" + key.length + "字节");
		}
	}
}
