package com.zja.example;

import com.zja.dto.response.DecryptResponse;
import com.zja.dto.response.DigestResponse;
import com.zja.dto.response.EncryptResponse;
import com.zja.dto.response.KeyCreateResponse;
import com.zja.dto.response.SignResponse;
import com.zja.dto.response.VerifyResponse;
import com.zja.sdk.online.CryptoPlatformClient;
import com.zja.sdk.online.OnlineSdkException;

/**
 * 在线SDK使用示例（密钥由平台集中托管，推荐接入方式）
 *
 * <p>演示三方系统通过在线SDK接入密码服务平台的完整流程：
 * SDK自动完成接入签名（HMAC-SM3）、时间戳与防重放，业务代码只需关注密码运算本身。</p>
 *
 * <p>运行前提：密码服务平台已启动（默认 http://localhost:8086）。
 * 演示应用凭证由平台启动时初始化（见 server 模块 application.yml：demo-app/demo-app-secret-123456）。</p>
 *
 * <p>执行方式（仓库根目录）：</p>
 * <pre>
 * mvn -pl springboot-encrypt/springboot-encrypt-crypto-platform/springboot-encrypt-crypto-platform-example ^
 *     compile exec:java -Dexec.mainClass=com.zja.example.OnlineSdkExample
 * </pre>
 *
 * @author: zhengja
 */
public class OnlineSdkExample {

	public static void main(String[] args) {
		// 支持通过命令行参数覆盖默认连接信息：baseUrl appKey appSecret
		String baseUrl = args.length > 0 ? args[0] : "http://localhost:8086";
		String appKey = args.length > 1 ? args[1] : "demo-app";
		String appSecret = args.length > 2 ? args[2] : "demo-app-secret-123456";

		CryptoPlatformClient client = new CryptoPlatformClient(baseUrl, appKey, appSecret);

		try {
			// 1. 创建SM4托管密钥（密钥材料不出平台，接口只返回keyId）
			KeyCreateResponse sm4Key = client.createKey("SM4", "示例-数据加密密钥");
			System.out.println("[1] 创建SM4托管密钥：" + sm4Key.getKeyId());

			// 2. 对称加解密（适合大批量数据；密文=Base64(随机IV+密文)）
			String plainText = "示例机密数据-订单号12345";
			EncryptResponse encrypted = client.symmetricEncrypt(sm4Key.getKeyId(), plainText);
			DecryptResponse decrypted = client.symmetricDecrypt(sm4Key.getKeyId(), encrypted.getCipherText());
			System.out.println("[2] SM4加密后解密还原：" + decrypted.getPlainText());

			// 3. 创建SM2密钥对并完成签名验签（私钥不出平台）
			KeyCreateResponse sm2Key = client.createKey("SM2", "示例-签名密钥对");
			String payload = "待签名业务报文";
			SignResponse sign = client.sign(sm2Key.getKeyId(), payload);
			VerifyResponse verify = client.verify(sm2Key.getKeyId(), payload, sign.getSignature());
			System.out.println("[3] SM2签名算法：" + sign.getAlgorithm() + "，验签结果：" + verify.isVerified());

			// 4. 摘要计算（无需密钥；SM3标准向量：SM3("abc")=66c7f0f4...b8f4ba8e0）
			DigestResponse digest = client.digest("SM3", "abc");
			System.out.println("[4] SM3(\"abc\") = " + digest.getDigest());

			// 5. 销毁示例密钥（演示清理；生产环境请按密钥全生命周期规范操作）
			client.destroyKey(sm4Key.getKeyId());
			client.destroyKey(sm2Key.getKeyId());
			System.out.println("[5] 示例密钥已销毁，在线SDK示例运行完成");
		} catch (OnlineSdkException e) {
			System.err.println("调用密码服务平台失败：" + e.getMessage());
			System.err.println("请确认平台已启动（server模块执行 mvn spring-boot:run），"
					+ "或通过参数指定连接信息：baseUrl appKey appSecret");
		}
	}
}
