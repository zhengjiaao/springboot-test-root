package com.zja.service;

import com.zja.common.Constants;
import com.zja.common.exception.BusinessException;
import com.zja.dto.request.AppRegisterRequest;
import com.zja.dto.response.AppRegisterResponse;
import com.zja.entity.AppEntity;
import com.zja.store.PlatformStore;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 三方应用管理服务：注册、停启用、查询
 *
 * @author: zhengja
 */
@Service
public class AppManageService {

	private static final SecureRandom RANDOM = new SecureRandom();

	private final PlatformStore platformStore;
	private final AuditService auditService;

	public AppManageService(PlatformStore platformStore, AuditService auditService) {
		this.platformStore = platformStore;
		this.auditService = auditService;
	}

	/**
	 * 注册三方应用，生成appKey/appSecret
	 *
	 * <p>appSecret仅在注册响应中出现一次，平台端仅保存密文/原文由密管系统托管。</p>
	 */
	public AppRegisterResponse register(AppRegisterRequest request) {
		if (request == null || isBlank(request.getAppName())) {
			throw BusinessException.badRequest("appName不能为空");
		}

		AppEntity app = new AppEntity();
		app.setId(UUID.randomUUID().toString().replace("-", ""));
		app.setAppKey(generateAppKey());
		app.setAppSecret(generateAppSecret());
		app.setAppName(request.getAppName().trim());
		app.setContact(request.getContact());
		app.setRemark(request.getRemark());
		app.setStatus(Constants.STATUS_ENABLED);
		app.setCreateTime(new Date());
		platformStore.saveApp(app);

		auditService.record(app.getAppKey(), "APP_REGISTER", null, null, true, "注册三方应用：" + app.getAppName(), 0);

		AppRegisterResponse response = new AppRegisterResponse();
		response.setAppKey(app.getAppKey());
		response.setAppSecret(app.getAppSecret());
		response.setAppName(app.getAppName());
		response.setStatus(app.getStatus());
		response.setCreateTime(app.getCreateTime());
		return response;
	}

	/**
	 * 初始化演示应用（由DemoDataInitializer调用，凭证来自配置文件）
	 */
	public AppRegisterResponse initDemoApp(String appKey, String appSecret, String appName) {
		AppEntity app = new AppEntity();
		app.setId(UUID.randomUUID().toString().replace("-", ""));
		app.setAppKey(appKey);
		app.setAppSecret(appSecret);
		app.setAppName(appName);
		app.setContact("平台演示");
		app.setRemark("演示应用，由平台启动时自动初始化，生产环境请置为 false 并删除");
		app.setStatus(Constants.STATUS_ENABLED);
		app.setCreateTime(new Date());
		platformStore.saveApp(app);

		auditService.record(appKey, "APP_REGISTER", null, null, true, "初始化演示应用：" + appName, 0);

		AppRegisterResponse response = new AppRegisterResponse();
		response.setAppKey(app.getAppKey());
		response.setAppSecret(app.getAppSecret());
		response.setAppName(app.getAppName());
		response.setStatus(app.getStatus());
		response.setCreateTime(app.getCreateTime());
		return response;
	}

	/** 应用列表 */
	public List<AppEntity> listApps() {
		return platformStore.listApps();
	}

	/** 应用详情（不含appSecret） */
	public AppEntity getApp(String appKey) {
		AppEntity app = platformStore.findApp(appKey);
		if (app == null) {
			throw BusinessException.notFound("应用不存在：" + appKey);
		}
		return app;
	}

	/** 停用应用：停用后所有在线接入请求将被拒绝 */
	public AppEntity disable(String appKey) {
		AppEntity app = requireApp(appKey);
		app.setStatus(Constants.STATUS_DISABLED);
		platformStore.saveApp(app);
		auditService.record(appKey, "APP_DISABLE", null, null, true, "停用应用", 0);
		return app;
	}

	/** 启用应用 */
	public AppEntity enable(String appKey) {
		AppEntity app = requireApp(appKey);
		app.setStatus(Constants.STATUS_ENABLED);
		platformStore.saveApp(app);
		auditService.record(appKey, "APP_ENABLE", null, null, true, "启用应用", 0);
		return app;
	}

	private AppEntity requireApp(String appKey) {
		AppEntity app = platformStore.findApp(appKey);
		if (app == null) {
			throw BusinessException.notFound("应用不存在：" + appKey);
		}
		return app;
	}

	/** 生成appKey：app-前缀 + 16位随机hex */
	private String generateAppKey() {
		byte[] bytes = new byte[8];
		RANDOM.nextBytes(bytes);
		return "app-" + Hex.encodeHexString(bytes);
	}

	/** 生成appSecret：32字节随机数的hex（64位字符串） */
	private String generateAppSecret() {
		byte[] bytes = new byte[32];
		RANDOM.nextBytes(bytes);
		return Hex.encodeHexString(bytes);
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
}
