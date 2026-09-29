package com.zja.example;

import com.zja.dto.response.DecryptResponse;
import com.zja.dto.response.DigestResponse;
import com.zja.dto.response.EncryptResponse;
import com.zja.dto.response.SignResponse;
import com.zja.sdk.offline.OfflineCryptoSdk;
import com.zja.sdk.offline.OfflineKey;

/**
 * 离线SDK使用示例（密钥自管、本地运算，适用内网隔离/专网前置机等无法在线调用的场景）
 *
 * <p>无需网络与服务平台在线，演示：密钥本地生成 → 全算法运算 → 密钥导出/导入（模拟持久化重启恢复）。
 * 离线SDK与平台编码格式完全一致：平台加密的数据可在离线端解密，反之亦然。</p>
 *
 * <p>密评提示：离线模式密钥为三方自管，需自行满足密钥的生成、存储、备份、销毁等全生命周期管控要求；
 * 若密评要求集中密钥管理，请优先选择在线SDK/HTTP接口接入。</p>
 *
 * @author: zhengja
 */
public class OfflineSdkExample {

	public static void main(String[] args) {
		OfflineCryptoSdk sdk = new OfflineCryptoSdk();

		// 1. 本地生成SM4密钥并完成加解密
		OfflineKey sm4Key = sdk.createKey("SM4", "示例-本地数据密钥");
		EncryptResponse encrypted = sdk.symmetricEncrypt(sm4Key.getKeyId(), "离线机密数据-中文123");
		DecryptResponse decrypted = sdk.symmetricDecrypt(sm4Key.getKeyId(), encrypted.getCipherText());
		System.out.println("[1] SM4本地加解密还原：" + decrypted.getPlainText());

		// 2. 本地生成SM2密钥对并完成签名验签
		OfflineKey sm2Key = sdk.createKey("SM2", "示例-本地签名密钥对");
		String payload = "离线待签名数据";
		SignResponse sign = sdk.sign(sm2Key.getKeyId(), payload);
		boolean verified = sdk.verify(sm2Key.getKeyId(), payload, sign.getSignature()).isVerified();
		System.out.println("[2] SM2签名算法：" + sign.getAlgorithm() + "，验签结果：" + verified);

		// 3. 摘要（SM3标准向量）与HMAC-SM3消息认证码
		DigestResponse digest = sdk.digest("SM3", "abc");
		System.out.println("[3] SM3(\"abc\") = " + digest.getDigest());
		OfflineKey macKey = sdk.createKey("HMAC_SM3", "示例-本地MAC密钥");
		System.out.println("[4] HMAC-SM3 = " + sdk.mac(macKey.getKeyId(), "完整性校验数据").getMac());

		// 5. 密钥导出/导入（模拟持久化：三方自行安全存储导出结果，重启后导入恢复）
		//    注意：getKey返回对象含密钥材料，生产环境务必加密存储并限制访问
		OfflineKey exported = sdk.getKey(sm4Key.getKeyId());
		OfflineKey imported = new OfflineKey();
		imported.setKeyId(exported.getKeyId());
		imported.setAlgorithm(exported.getAlgorithm());
		imported.setCategory(exported.getCategory());
		imported.setAlias(exported.getAlias());
		imported.setSecretKeyBase64(exported.getSecretKeyBase64());
		imported.setCreateTime(exported.getCreateTime());

		OfflineCryptoSdk restored = new OfflineCryptoSdk();
		restored.importKey(imported);
		String crossPlain = restored.symmetricDecrypt(imported.getKeyId(), encrypted.getCipherText()).getPlainText();
		System.out.println("[5] 新SDK实例导入密钥后解密原密文：" + crossPlain);

		System.out.println("离线SDK示例运行完成（全程无需网络连通）");
	}
}
