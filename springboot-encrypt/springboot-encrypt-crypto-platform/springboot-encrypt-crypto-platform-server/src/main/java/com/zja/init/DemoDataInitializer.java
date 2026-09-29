package com.zja.init;

import com.zja.dto.request.KeyCreateRequest;
import com.zja.dto.response.AppRegisterResponse;
import com.zja.dto.response.KeyCreateResponse;
import com.zja.service.AppManageService;
import com.zja.service.KeyManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 演示数据初始化：启动时创建演示应用与示例密钥，方便Swagger直接调试
 *
 * <p>安全约束：启用演示应用但未配置凭证时启动失败（fail-fast）；使用公开演示凭证时输出安全告警；
 * 密钥信息在日志中脱敏展示。生产环境务必将 crypto.platform.init-demo-app 置为 false，
 * 三方应用一律通过 /api/app/register 申请。</p>
 *
 * @author: zhengja
 */
@Slf4j
@Component
public class DemoDataInitializer implements CommandLineRunner {

	/** 公开的演示凭证：出现该值说明未做生产加固，启动时输出告警 */
	private static final String PUBLIC_DEMO_SECRET = "demo-app-secret-123456";

	@Value("${crypto.platform.init-demo-app:false}")
	private boolean initDemoApp;
	@Value("${crypto.platform.demo-app-key:}")
	private String demoAppKey;
	@Value("${crypto.platform.demo-app-secret:}")
	private String demoAppSecret;

	private final AppManageService appManageService;
	private final KeyManageService keyManageService;

	public DemoDataInitializer(AppManageService appManageService, KeyManageService keyManageService) {
		this.appManageService = appManageService;
		this.keyManageService = keyManageService;
	}

	@Override
	public void run(String... args) {
		if (!initDemoApp) {
			return;
		}
		// fail-fast：启用演示应用但未配置凭证时终止启动，避免平台带着空凭证运行
		if (isBlank(demoAppKey) || isBlank(demoAppSecret)) {
			throw new IllegalStateException("已启用演示应用（crypto.platform.init-demo-app=true），"
					+ "请配置 crypto.platform.demo-app-key 与 crypto.platform.demo-app-secret 后再启动");
		}
		if (PUBLIC_DEMO_SECRET.equals(demoAppSecret)) {
			log.warn("[密码服务平台] 演示应用正在使用公开的演示凭证，仅限本地演示环境；"
					+ "生产环境必须关闭 init-demo-app 或改用保密凭证");
		}

		AppRegisterResponse demoApp = appManageService.initDemoApp(demoAppKey, demoAppSecret, "演示应用");
		log.info("[密码服务平台] 演示应用已初始化：appKey={}, appSecret={}（生产环境请关闭 init-demo-app）",
				demoApp.getAppKey(), maskSecret(demoApp.getAppSecret()));

		// 为演示应用预置两把示例密钥：SM4（对称）+ SM2（非对称）
		KeyCreateRequest sm4KeyRequest = new KeyCreateRequest();
		sm4KeyRequest.setAlgorithm("SM4");
		sm4KeyRequest.setAlias("demo-sm4-数据加密密钥");
		KeyCreateResponse sm4Key = keyManageService.createKey(demoAppKey, sm4KeyRequest);

		KeyCreateRequest sm2KeyRequest = new KeyCreateRequest();
		sm2KeyRequest.setAlgorithm("SM2");
		sm2KeyRequest.setAlias("demo-sm2-签名密钥对");
		KeyCreateResponse sm2Key = keyManageService.createKey(demoAppKey, sm2KeyRequest);

		log.info("[密码服务平台] 演示密钥已创建：SM4密钥={}，SM2密钥对={}", sm4Key.getKeyId(), sm2Key.getKeyId());
		log.info("[密码服务平台] Swagger文档：http://localhost:8086/swagger-ui/index.html");
	}

	/** 密钥日志脱敏：仅保留前4位 */
	private String maskSecret(String secret) {
		if (secret == null || secret.length() <= 4) {
			return "****";
		}
		return secret.substring(0, 4) + "****";
	}

	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}
